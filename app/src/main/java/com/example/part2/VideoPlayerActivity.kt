package com.example.part2

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class VideoPlayerActivity : AppCompatActivity() {
    lateinit var playerView: PlayerView
    private lateinit var exoPlayer: SimpleExoPlayer
    @OptIn(UnstableApi::class) override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)


        playerView = findViewById(R.id.player_view)

        val videoUri = Uri.parse(intent.getStringExtra("videoUri"))
        exoPlayer = SimpleExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        val mediaSource = ProgressiveMediaSource.Factory(DefaultDataSourceFactory(this, Util.getUserAgent(this, "Part2")))
            .createMediaSource(videoUri)

        exoPlayer.prepare(mediaSource)
        exoPlayer.playWhenReady = true
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

}