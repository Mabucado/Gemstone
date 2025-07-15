package com.example.part2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class image_gallery : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_gallery)
        val viewModel = ViewModelProvider(this).get(DataViewModel::class.java)
       val model = viewModel.model

        recyclerView=findViewById(R.id.galleryRecyclerView)
        val adapter = ImageAdapter(model.gemstoneName.values.map { it.image })
        recyclerView.layoutManager= GridLayoutManager(this,3)
        recyclerView.adapter = adapter
    }
}