package com.tombyts.ui

import android.os.Bundle
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TombytsTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(modifier = Modifier.fillMaxSize(),
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
    Button(onClick = {showSnackbar = true
    }) {
        Text("Click me")
    }

    if (showSnackbar){
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