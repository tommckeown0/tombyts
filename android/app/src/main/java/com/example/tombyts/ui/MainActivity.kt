package com.example.tombyts.ui

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TombytsTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(navController, snackbarHostState) // Pass snackbarHostState
                    }
                    composable(
                        "movieList/{token}",
                        arguments = listOf(navArgument("token") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val token = backStackEntry.arguments?.getString("token") ?: ""
                        MovieListScreen(token)
                    }
                }
            }
        }
    }
}

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
fun MovieListScreen(token: String) {
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
                        Text(text = movie.title) // Display movie title
                    }
                }
            }
        }
    }
    // ... (Display movies or loading/error state)
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