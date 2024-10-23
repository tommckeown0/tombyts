package com.example.tombyts.ui

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/")
    suspend fun getResponse(): Response<String>

    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("/movies")
    suspend fun getMovies(@Header("Authorization") token: String): Response<List<Movie>>

    @GET("/movies/{movieTitle}") // New route for fetching movie details
    suspend fun getMovieDetails(
        @Path("movieTitle") movieTitle: String,
        @Header("Authorization") token: String
    ): Response<MovieDetails>
}