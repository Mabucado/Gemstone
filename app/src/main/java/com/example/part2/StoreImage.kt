package com.example.part2

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import java.nio.ByteBuffer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Context
import android.net.Uri


class StoreImage : DialogFragment() {
    var imageBitmap: Uri? = null
    var REQUEST_IMAGE_CAPTURE=100
    val CAMERA_PERMISSION_CODE = 100


    interface Capturing {
        fun openCamera()
        fun uploadImage()
    }

    private var capturing: Capturing? = null



    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_store_image, container, false)
        var capture = view.findViewById<Button>(R.id.btnCapture)
        var upload = view.findViewById<Button>(R.id.btnUpload)




        capture?.setOnClickListener {
           val takePictureIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA)
            button=1
            dismiss()
            Log.i("button", "capture was is clicked")
        }

        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
            // Request the camera permission
           requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE) }
        else {
            // Permission is already granted, proceed with your app logic
        }


            upload?.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE_GALLERY)
                button=2
                dismiss()
                Log.i("button", "upload is clicked")
            }


        return view
    }





    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Capturing) {
            capturing = context
        } else {
            throw RuntimeException("$context must implement OnImageBitmapListener")
        }
    }

   override fun onDetach() {
        super.onDetach()
       capturing = null
    }
    companion object {
        const val REQUEST_CODE_GALLERY = 1
        const val REQUEST_CODE_CAMERA = 2
        var button:Int=0
    }

}