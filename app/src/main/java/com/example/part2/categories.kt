package com.example.part2

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import java.io.InputStream
import java.util.zip.Inflater
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storageMetadata
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Objects
import java.util.UUID


class categories : AppCompatActivity(),StoreImage.Capturing {
    lateinit var addCategory: Button
    lateinit var homeButton: Button
    lateinit var categoryName: TextView
    lateinit var categoryListView: GridView
    var categoryNamesList: MutableList<String> = mutableListOf<String>()

    var REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_PICK = 2
    val CAMERA_PERMISSION_CODE = 100
    private val REQUEST_CODE = 100

    lateinit var model: Model

    lateinit var images: IntArray
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        val viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        addCategory = findViewById(R.id.btnaddCategory)
        categoryName = findViewById(R.id.edtCategory)
        categoryListView = findViewById(R.id.categoryItems)
        homeButton = findViewById(R.id.btnHome)


        model = viewModel.model
        val adapter = ImageTextAdapter(this, model.gemstoneCategory)
        categoryListView.adapter = adapter
        checkAndRequestPermissions(this)






        addCategory.setOnClickListener() {
            var addItem = categoryName.text.toString()
            if (addItem.isNotEmpty()) {
                val adapterV = ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_dropdown_item_1line,
                    categoryNamesList
                )



                categoryNamesList.add(addItem)
                val showPopup = StoreImage()
                showPopup.show(supportFragmentManager, "showpopup")

            }
        }
        homeButton.setOnClickListener {
            val intent = Intent(this, dashboardActivity2::class.java)
            startActivity(intent)
        }



    }
    fun checkAndRequestPermissions(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
        }
    }


    override fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, 10)
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the camera permission
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {
            // Permission is already granted, proceed with your app logic
        }
    }

    override fun uploadImage() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.i("override", "request override")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
        when (requestCode) {
            REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, proceed with accessing the content provider
                } else {
                    // Permission denied, handle accordingly
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("request", requestCode.toString())
        Log.i("result", resultCode.toString())
        Log.i("expectedResult", "${StoreImage.REQUEST_CODE_GALLERY} OR ${StoreImage.REQUEST_CODE_CAMERA}")
        if (StoreImage.button == 2 && data != null) {


            // Image was selected from the gallery
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let {
                // Handle the image (e.g., upload it to Firebase Storage)
                storeSave(selectedImageUri)
            }
        }
        try{
            val extras = data!!.extras
            if (StoreImage.button == 1 && extras != null) {
                // Image was captured using the camera
                val photo: Bitmap = data!!.extras?.get("data") as Bitmap
                // Convert Bitmap to Uri (You may need to save it locally and get the Uri)
                val imageUri = saveBitmapToUri(photo)
                imageUri?.let {
                    // Handle the image (e.g., upload it to Firebase Storage)
                    storeSave(imageUri)
                }
            }
        }catch(e:Exception){
        }



    }
    fun storeSave(image:Uri){
        val addItem = categoryName.text.toString()
        model.gemstoneCategory[addItem] = image
        // Assuming `uri` is the URI you received
        grantUriPermission(packageName, image, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        for ((key, uri) in model.gemstoneCategory) {
            uri?.let {
                val storageRef = FirebaseStorage.getInstance().reference.child("images/${UUID.randomUUID()}")
                val uploadTask = storageRef.putFile(uri)

                uploadTask.addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        // Store the download URL in Firestore
                        val gemstoneData = mapOf("uri" to downloadUrl.toString())
                        db.collection("gemstoneCategory").document(key).set(gemstoneData)
                            .addOnSuccessListener {
                                Log.d("Firestore", "DocumentSnapshot successfully written for $key")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "Error writing document for $key", e)
                            }
                    }
                }.addOnFailureListener { exception ->
                    Log.w("Storage", "Error uploading image for $key", exception)
                }
            }
        }
        // Notify adapter of data change
        val adapter = ImageTextAdapter(this, model.gemstoneCategory)
        categoryListView.adapter = adapter
    }
    private fun saveBitmapToUri(bitmap: Bitmap): Uri? {
        // Save the bitmap to a file and return its Uri
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "captured_image.jpg")
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.fromFile(file)
    }
    }



