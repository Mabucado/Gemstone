package com.example.part2

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.part2.StoreImage.Companion.REQUEST_CODE_CAMERA
import com.example.part2.StoreImage.Companion.REQUEST_CODE_GALLERY
import com.example.part2.StoreImage.Companion.button
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.UUID


lateinit var add:Button
lateinit var nameEntry:TextView
lateinit var descriptionEntry: TextView
lateinit var categoryEntry: TextView
lateinit var placeEntry:TextView
lateinit var valueEntry: TextView
private val REQUEST_VIDEO_CAPTURE = 3
private val REQUEST_VIDEO_SELECT = 4
var REQUEST_IMAGE_CAPTURE = 1
val REQUEST_IMAGE_PICK = 2
val CAMERA_PERMISSION_CODE = 100
private val REQUEST_CODE = 100
lateinit var model: Model
var imageSaved:Int=0

class itemsForm : AppCompatActivity(),StoreImage.Capturing {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_form)

        val viewModel = ViewModelProvider(this).get(DataViewModel::class.java)
        model = viewModel.model


        add = findViewById(R.id.AddBtn)
        nameEntry = findViewById(R.id.edtNameItem)
        descriptionEntry = findViewById(R.id.edtSurnameItem)
        categoryEntry = findViewById(R.id.edtEmailItem)
        placeEntry = findViewById(R.id.edtPlaceItem)
        valueEntry = findViewById(R.id.edtValueItem)
        val model = viewModel.model
        checkAndRequestPermissions(this)

        add.setOnClickListener {

            val showPopup = StoreImage()
            showPopup.show(supportFragmentManager, "showpopup")

        }

    }

    fun checkAndRequestPermissions(activity: Activity) {
        if (ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
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
        Log.i("expectedResult", "$REQUEST_CODE_GALLERY OR $REQUEST_CODE_CAMERA")
        var selectedImageUri: Uri?=null
        if (button == 2 && data != null) {


            // Image was selected from the gallery
             selectedImageUri = data.data
            selectedImageUri?.let {
                // Handle the image (e.g., upload it to Firebase Storage)
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_VIDEO_SELECT)
                button=0
                imageSaved=2


            }
        }
        try{
        val extras = data!!.extras
            var imageUri:Uri? =null
        if (button == 1 && extras != null) {
            // Image was captured using the camera
            val photo: Bitmap = data!!.extras?.get("data") as Bitmap
            // Convert Bitmap to Uri (You may need to save it locally and get the Uri)
             imageUri = saveBitmapToUri(photo)
            imageUri?.let {
                // Handle the image (e.g., upload it to Firebase Storage)
                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                startActivityForResult(intent, REQUEST_VIDEO_CAPTURE)
                button=0
                imageSaved=1

            }
        }
            if(imageSaved==1){
                var videoUri: Uri = data?.data!!
                storeSave(imageUri,videoUri)
            }
        }catch(e:Exception){
        }

        if(imageSaved==2){
            var videoUri: Uri = data?.data!!
            storeSave(selectedImageUri,videoUri)
        }
    }


    fun storeSave(image:Uri?,video:Uri?){
        var name= nameEntry.text.toString()
        var des= descriptionEntry.text.toString()
        var cat= categoryEntry.text.toString()
        var place= placeEntry.text.toString()
        var value= valueEntry.text.toString().toInt()
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())




            // Add the selected image URI to gemstoneCategory map
            model.gemstoneName[name]=Gemstone(des,cat,place,value,dateFormat.format(calendar.time),image,video)
        Log.i("image",image.toString())
            // Assuming `uri` is the URI you received
            grantUriPermission(packageName, image, Intent.FLAG_GRANT_READ_URI_PERMISSION)


            for ((gemstoneId, gemstone) in model.gemstoneName) {
                val storageReference: StorageReference = FirebaseStorage.getInstance().getReference()
                val imageReference = storageReference.child("images/${gemstone.image!!.lastPathSegment}")
                val videoReference = storageReference.child("videos/${gemstone.video!!.lastPathSegment}")

                imageReference.putFile(gemstone.image!!)
                    .addOnSuccessListener {
                        videoReference.putFile(gemstone.video!!)
                            .addOnSuccessListener{
                            imageReference.downloadUrl.addOnSuccessListener { uri ->
                                val updatedGemstone = gemstone.copy(image = Uri.parse(uri.toString()))
                                 val secUpdate = updatedGemstone.copy(video=Uri.parse(uri.toString()))
                                db.collection("gemstoneName").document(gemstoneId)
                                    .set(secUpdate)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d("Firestore", "User $gemstoneId added successfully")
                                        var intent = Intent(this, Items::class.java)

                                        startActivity(intent)
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(TAG, "Error adding document", e)
                                    }

                            }
                        }.addOnFailureListener { exception ->
                                Log.w("Storage", "Error uploading video", exception)
                            }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("Storage", "Error uploading image", exception)
                    }



            }

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