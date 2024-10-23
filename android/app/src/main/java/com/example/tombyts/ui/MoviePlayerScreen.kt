package com.example.tombyts.ui

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun MoviePlayerScreen(movieTitle: String, token: String) {
    var moviePath by remember { mutableStateOf<String?>(null) }

    val apiService = remember { // Create ApiService instance
        Retrofit.Builder()
            .baseUrl("https://10.0.2.2:3001/") // Replace with your backend URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    LaunchedEffect(movieTitle) {
        try {
            val response = apiService.getMovieDetails(movieTitle, "Bearer $token")
            if (response.isSuccessful) {
                val movieDetails = response.body()
                moviePath = movieDetails?.path
            } else {
                // Handle API error
            }
        } catch (e: Exception) {
            // Handle network or other errors
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Movie Path: $moviePath")
            if (moviePath != null) { // **Change 1: Check if moviePath is available**
                AndroidView( // **Change 2: Add AndroidView composable**
                    factory = { context ->
                        VideoView(context).apply {
                            setVideoURI(Uri.parse("https://10.0.2.2:3001/media/$moviePath")) // **Change 3: Set video URI**
                            start() // Start playback
                        }
                    },
                    modifier = Modifier.fillMaxSize() // **Change 4: Set modifier for size**
                )
            } else {
                CircularProgressIndicator() // **Change 5: Show loading indicator**
            }
        }
    }
}

