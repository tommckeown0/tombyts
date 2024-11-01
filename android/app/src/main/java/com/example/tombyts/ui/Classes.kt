package com.example.tombyts.ui

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Classes {
    object ApiProvider {
        private const val BASE_URL = "https://10.0.2.2:3001/" // Your backendURL

        val apiService: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}