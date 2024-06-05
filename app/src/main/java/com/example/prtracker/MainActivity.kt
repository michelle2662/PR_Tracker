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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prtracker.ui.detail.PercentWorkoutScreen
import com.example.prtracker.ui.detail.UserDetailScreen
import com.example.prtracker.ui.home.UserScreen
import com.example.prtracker.ui.theme.PRTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PRTrackerTheme {
                val navController = rememberNavController()
                AppContent(navController)
            }
        }
    }
}

@Composable
fun AppContent(navController: NavHostController) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "user_screen",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("user_screen") {
                UserScreen(onNavigate = { userId ->
                    navController.navigate("user_detail/$userId")
                })
            }
            composable("user_detail/{userId}") { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: return@composable
                UserDetailScreen(navController, userId = userId)
            }



        }
    }
}



//@Composable
//fun AppContent(
//    navController: NavController
//) {
//    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//        NavHost(
//            navController = navController,
//            "user_screen".also { startDestination = it },
//            var modifier : kotlin . Any ? = Modifier.padding(innerPadding)
//        ) {
//            composable("user_screen") {
//                UserScreen(onNavigate = { userId ->
//                    navController.navigate("user_detail/$userId")
//                })
//            }
//            composable("user_detail/{userId}") { backStackEntry ->
//                val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: return@composable
//                UserDetailScreen(userId = userId)
//            }
//        }
////        UserScreen(
////            modifier = Modifier.padding(innerPadding),
////            onNavigate = { userId ->
////                // Handle navigation based on the userId
////                // For now, we'll just print the userId
////                println("Navigating to user with ID: $userId")
////            }
////        )
//    }
//}


