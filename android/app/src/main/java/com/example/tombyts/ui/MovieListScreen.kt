package com.example.tombyts.ui

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun MovieListScreen(token: String, navController: NavController) {
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
            Text(text = "Movieslist")
            var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }
            var isLoading by remember { mutableStateOf(true) }
            var error by remember { mutableStateOf<String?>(null) }
            val apiService = remember { // Create ApiService instance
                Retrofit.Builder()
                    .baseUrl("https://10.0.2.2:3001/") // Replace with your backend URL
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
            }

            LaunchedEffect(Unit) {
                try {
                    val response = apiService.getMovies("Bearer $token") // Include token in headers
                    if (response.isSuccessful) {
                        movies = response.body() ?: emptyList()
                    } else {
                        error = "Failed to fetch movies"
                        Log.e("API Error", "Failed to fetch movies: ${response.code()}")
                    }
                } catch (e: Exception) {
                    error = "Error: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
            if (isLoading) {
                CircularProgressIndicator()
            } else if (error != null) {
                Text("Error: $error")
            } else {
                LazyColumn {
                    items(movies) { movie ->
//                        TextButton(onClick = {
//                            val encodedTitle = Uri.encode(movie.title)
//                            navController.navigate("moviePlayer/${encodedTitle}/${token}")
//                        }) {
//                            Text(text = movie.title)
//                        }
                        TextButton(onClick = {
                            val encodedTitle = Uri.encode(movie.title)
                            navController.navigate("moviePlayer/${encodedTitle}/${token}")
                        }) {
                            Text(text = movie.title)
                        }
                    }
                }
            }
        }
    }
    // ... (Display movies or loading/error state)
}