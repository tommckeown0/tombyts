package com.example.tombyts.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.tombyts.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MoviePlayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private var moviePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_player)

        playerView = findViewById(R.id.playerView)

        // Get movie path and token from intent extras
        val movieTitle = intent.getStringExtra("movieTitle")
        val token = intent.getStringExtra("token")

        // Fetch movie path using coroutines
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response =
                    Classes.ApiProvider.apiService.getMovieDetails(movieTitle!!, "Bearer $token")
                if (response.isSuccessful) {
                    val movieDetails = response.body()
                    moviePath = movieDetails?.path?.let { Uri.encode(it) }
                    initializePlayer(moviePath, token) // Initialize player after fetching path
                } else {
                    Log.d("API Error", "Failed to fetch movie details: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.d("API Error", "Error: ${e.message}")
            }
        }
    }

    private fun initializePlayer(moviePath: String?, token: String?) {
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        if (moviePath != null && token != null) {
            val mediaItem = MediaItem.fromUri(Uri.parse("https://10.0.2.2:3001/media/$moviePath"))
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}