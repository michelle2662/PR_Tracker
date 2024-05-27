package com.example.prtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.prtracker.ui.home.UserScreen
import com.example.prtracker.ui.theme.PRTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PRTrackerTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        UserScreen(
            modifier = Modifier.padding(innerPadding),
            onNavigate = { userId ->
                // Handle navigation based on the userId
                // For now, we'll just print the userId
                println("Navigating to user with ID: $userId")
            }
        )
    }
}
