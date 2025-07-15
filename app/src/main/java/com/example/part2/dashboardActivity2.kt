package com.example.part2

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class dashboardActivity2 : AppCompatActivity() {

    lateinit var goCategory: ImageButton
    lateinit var goItems: ImageButton
    lateinit var goAchievements: ImageButton
    lateinit var goPhotoGallery: ImageButton
    lateinit var goHistory: ImageButton
    lateinit var goVideo: ImageButton
    lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard2)
        val viewModel = ViewModelProvider(this).get(DataViewModel::class.java)
        val model = viewModel.model

        goCategory = findViewById(R.id.btnCategory)
        goItems = findViewById(R.id.btnItems)
        goAchievements = findViewById((R.id.btnAchievements))
        goPhotoGallery = findViewById(R.id.photoGalleryimgBtn)
        goHistory = findViewById(R.id.btnHistory)
        status = findViewById(R.id.txtAchievementStatus)
        goVideo=findViewById(R.id.btnVideo)

        status.background = ColorDrawable(Color.TRANSPARENT)
        if (model.gemstoneName.size >= 1 && model.gemstoneName.size < 3) {
            status.text = "Achievement Status: Starter"
            status.background = ColorDrawable(Color.parseColor("#fcf803"))
        }
        if (model.gemstoneName.size >= 3 && model.gemstoneName.size < 10) {
            status.text = "Achievement Status: Collector"
            status.background = ColorDrawable(Color.parseColor("#84fc03"))
        }
        if (model.gemstoneName.size >= 10) {
            status.text = "Achievement Status:Packrat"
            status.background = ColorDrawable(Color.parseColor("#0ffc03"))
        }

        goCategory.setOnClickListener() {
            var intent = Intent(this, categories::class.java)
            startActivity(intent)
        }
        goItems.setOnClickListener() {
            var intent = Intent(this, Items::class.java)
            startActivity(intent)
        }
        goAchievements.setOnClickListener {
            var intent = Intent(this, Achievements::class.java)
            startActivity(intent)
        }
        goPhotoGallery.setOnClickListener() {
            var intent = Intent(this, image_gallery::class.java)
            startActivity(intent)
        }
        goHistory.setOnClickListener() {
            var intent = Intent(this, History::class.java)
            startActivity(intent)
        }
        goVideo.setOnClickListener() {
            var intent = Intent(this, VideoGallary::class.java)
            startActivity(intent)
        }
    }
}