package com.example.tombyts.ui

data class LoginRequest(val username: String, val password: String)

data class LoginResponse(val message: String, val token: String?)