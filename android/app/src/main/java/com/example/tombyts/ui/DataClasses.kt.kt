package com.example.tombyts.ui

data class LoginRequest(val username: String, val password: String)

data class LoginResponse(val message: String, val token: String?)

data class Movie(val id: String, val title: String, val path: String)