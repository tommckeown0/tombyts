package com.example.tombyts.ui

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/")
    suspend fun getResponse(): Response<String>
}