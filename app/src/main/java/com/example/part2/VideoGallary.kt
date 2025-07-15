package com.example.part2

import android.media.ThumbnailUtils
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.part2.ui.login.HistoryList

class VideoGallary : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_gallary)
        val viewModel = ViewModelProvider(this).get(DataViewModel::class.java)
        val model = viewModel.model
        recyclerView=findViewById(R.id.recyclerView)

        val videoList = mutableListOf<VideoItem>()
        for (gemstone in model.gemstoneName) {
            videoList.add(
                VideoItem(
                    title =gemstone.key,
                    videoUri = gemstone.value.video ?:Uri.EMPTY,
                    thumbnailUri = gemstone.value.video?.path?.let {
                        ThumbnailUtils.createVideoThumbnail(
                            it, MediaStore.Video.Thumbnails.MINI_KIND)
                    } as Uri?
                ))

    }
        val videoAdapter = VideoAdapter(videoList.toList())
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns
        recyclerView.adapter = videoAdapter
}
}