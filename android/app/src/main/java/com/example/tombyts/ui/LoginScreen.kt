package com.example.tombyts.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun LoginScreen(navController: NavController, snackbarHostState: SnackbarHostState) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var username by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            Text("Welcome to Tombyts")
            OutlinedTextField(
                value = username,
                onValueChange = { username = "tom" },
                label = { Text("Username") },
                placeholder = { Text("Enter your username") }
            )

            var passwordVisible by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = password,
                onValueChange = { password = "password" },
                label = { Text("Password") },
                placeholder = { Text("Enter your password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Check
                    else Icons.Filled.CheckCircle
                    // Please provide localized description for accessibility
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            )
            LoginButton(username, password, navController)
        }
    }
}

@Composable
fun LoginButton(username: String, password: String, navController: NavController) {
    var apiResponse by remember { mutableStateOf("") } // State to hold API response
    val coroutineScope = rememberCoroutineScope()
    val apiService = remember { // Create ApiService instance
        Retrofit.Builder()
            .baseUrl("https://10.0.2.2:3001/") // Replace with your backend URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    val context = LocalContext.current

    Button(onClick = {
        coroutineScope.launch {
            try {
                val loginRequest = LoginRequest(username, password)
                val response = apiService.login(loginRequest)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val token = loginResponse?.token
                    if (token != null) {
                        val sharedPreferences =
                            context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("auth_token", token)
                        editor.apply()

                        navController.navigate("movieList/$token") // Pass token as argument
                    } else {
                        apiResponse = "Login failed: Token is null"
                    }
                } else {
                    apiResponse = "Login failed: ${response.code()} ${response.message()}"
                }
            } catch (e: Exception) {
                apiResponse = "API Exception: ${e.message}"
                // Handle exception
                // ...
            }
        }
    }) {
        Text("Login")
    }

    // Display API response (optional)
    Text(apiResponse)
}