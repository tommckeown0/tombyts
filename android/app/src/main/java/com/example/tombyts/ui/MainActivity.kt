package com.example.tombyts.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tombyts.ui.theme.TombytsTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TombytsTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Existing content
                        Greeting(name = "Tom")
                        SimpleButton(snackbarHostState)
                        SimpleButton2(snackbarHostState)

                        // Login fields
                        var username by remember { mutableStateOf("") }
                        var password by remember { mutableStateOf("") }

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                            placeholder = { Text("Enter your username") },
                            modifier = Modifier.padding(8.dp) // Add padding
                        )

                        var passwordVisible by remember { mutableStateOf(false) }
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            placeholder = { Text("Enter your password") },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Filled.Check
                                else Icons.Filled.CheckCircle
                                val description =
                                    if (passwordVisible) "Hide password" else "Show password"
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(imageVector = image, contentDescription = description)
                                }
                            },
                            modifier = Modifier.padding(8.dp) // Add padding
                        )
                        LoginButton(username, password)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello again $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TombytsTheme {
        Greeting("Android")
    }
}

@Composable
fun LoginScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Input fields and buttons will go here
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            placeholder = { Text("Enter your username") }
        )

        var passwordVisible by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
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
    }
}

@Composable
fun LoginButton(username: String, password: String) {
    var apiResponse by remember { mutableStateOf("") } // State to hold API response
    val coroutineScope = rememberCoroutineScope()
    val apiService = remember { // Create ApiService instance
        Retrofit.Builder()
            .baseUrl("https://10.0.2.2:3001/") // Replace with your backend URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    Button(onClick = {
        coroutineScope.launch {
            try {
                val loginRequest = LoginRequest(username, password)
                val response = apiService.login(loginRequest)
                if (response.isSuccessful) {
                    apiResponse = response.body()?.message ?: "Login successful"
                    //Display token in apiResponse
                    // Handle successful login (e.g., store token, navigate to home screen)
                    // ...
                } else {
                    apiResponse = "Login failed: ${response.code()}"
                    // Handle login failure (e.g., show error message)
                    // ...
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleButton(snackbarHostState: SnackbarHostState) {
    var showSnackbar by remember { mutableStateOf(false) }
    Button(
        onClick = { showSnackbar = true },
        modifier = Modifier
            .testTag("simpleButton")
            .semantics { contentDescription = "Simple Button" },
    ) {
        Text("Click me")
    }

    if (showSnackbar) {
        LaunchedEffect(Unit) {
            val result = snackbarHostState.showSnackbar(
                message = "Hello, world!",
                duration = SnackbarDuration.Short
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    // Action was performed, do something
                }

                SnackbarResult.Dismissed -> {
                    showSnackbar = false// Action was dismissed, do something
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleButton2(snackbarHostState: SnackbarHostState) {
    var apiResponse by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }

    val retrofit = Retrofit.Builder()
        .baseUrl("https://10.0.2.2:3001/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    Button(
        onClick = {
            CoroutineScope(Dispatchers.Main).launch { // Use CoroutineScope directly
                try {
                    val response = apiService.getResponse()
                    if (response.isSuccessful) {
                        apiResponse = response.body() ?: "No response body"
                        //showSnackbar = true
                    } else {
                        apiResponse = "API Error: ${response.code()}"
                        //showSnackbar = true
                    }
                } catch (e: Exception) {
                    apiResponse = "API Exception: ${e.message}"
                    //showSnackbar = true
                    Log.e("API Error", e.message, e)
                }

                val result = snackbarHostState.showSnackbar(
                    message = apiResponse,
                    duration = SnackbarDuration.Short
                )
            }
        },
        modifier = Modifier
            .testTag("simpleButton2")
            .semantics { contentDescription = "API Button" }
    ) {
        Text("API call")
    }

    if (showSnackbar) {
        LaunchedEffect(apiResponse) { // Trigger when apiResponse changes
            val result = snackbarHostState.showSnackbar(
                message = apiResponse,
                duration = SnackbarDuration.Short
            )
            // ... handle Snackbar result if needed
        }
    }
}