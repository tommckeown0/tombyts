package com.tombyts.ui

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
import com.tombyts.ui.theme.TombytsTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.lifecycle.lifecycleScope
import com.tombyts.ui.ApiService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope

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
                    Column( // Use Column within Scaffold content
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Greeting(name = "Tom")
                        SimpleButton(snackbarHostState)
                        SimpleButton2(snackbarHostState)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleButton(snackbarHostState: SnackbarHostState) {
    var showSnackbar by remember { mutableStateOf(false) }
    Button(onClick = {
        showSnackbar = true
    }) {
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

    Button(onClick = {
        CoroutineScope(Dispatchers.Main).launch { // Use CoroutineScope directly
            try {
                val response = apiService.getResponse()
                if (response.isSuccessful) {
                    apiResponse = response.body() ?: "No response body"
                    showSnackbar = true
                } else {
                    apiResponse = "API Error: ${response.code()}"
                    showSnackbar = true
                }
            } catch (e: Exception) {
                apiResponse = "API Exception: ${e.message}"
                showSnackbar = true
                Log.e("API Error", e.message, e)
            }
        }
    }) {
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