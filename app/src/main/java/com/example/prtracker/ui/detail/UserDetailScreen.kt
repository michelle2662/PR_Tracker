package com.example.prtracker.ui.detail

import HomeViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prtracker.data.room.models.LiftWithPR

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    userId: Int,

    ) {
    val homeViewModel: HomeViewModel = viewModel()
    val user = homeViewModel.state.collectAsState().value.users.find { it.id == userId }
    var showPopUp by remember { mutableStateOf(false)} // -> STATE

    user?.let {user ->
        val userPRsWithLifts by homeViewModel.getUserPRsWithLifts(userId).collectAsState(initial = emptyList())

        Scaffold(
            topBar ={
                TopAppBar(
                    modifier = Modifier.height(56.dp),
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                   title = { Text("${user.first_name} ${user.last_name} ", style = MaterialTheme.typography.titleLarge) },

                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showPopUp = true
                    })
                 {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                }
            }
        ){
//
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                //verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(userPRsWithLifts) { liftWithPR ->
                    PRItem(liftWithPR)
                }
            }
        }

        if (showPopUp){
            AddNewPR(
                onDismiss = {showPopUp = false},
                onConfirm = { liftName, weight, repetitions ->
                    homeViewModel.insertPR(userId, liftName, weight, repetitions)
                }
            )
        }
    } ?: run {
        Text(text = "User not found", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun AddNewPR(
    onDismiss: () -> Unit,
    onConfirm:(String, Double, Int ) -> Unit

){
    var liftName by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var repetitions by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    val weightValue = weight.toDoubleOrNull() ?: 0.0
                    val repetitionsValue = repetitions.toIntOrNull() ?: 0
                    onConfirm(liftName, weightValue, repetitionsValue)
                    onDismiss()
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            IconButton(onClick = { onDismiss() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        },
        text = {
            Column(){
                TextField (
                    value = liftName,
                    onValueChange = {liftName = it},
                    label = {Text("Lift")}
                )

                TextField(
                    value = weight,
                    onValueChange = {
                        weight = it
                    },
                    label = {
                        Text("lb")
                    }
                )

                TextField(
                    value = repetitions,
                    onValueChange = {
                        repetitions = it
                    },
                    label = {
                        Text("Reps")
                    }
                )

            }

        }

    )



}

@Composable
fun PRItem(liftWithPR: LiftWithPR) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "${liftWithPR.lift.lift}: ${liftWithPR.pr.weight} lb" , style = MaterialTheme.typography.bodyLarge)
            Text(text = "Repetitions: ${liftWithPR.pr.repetitions}", style = MaterialTheme.typography.bodyLarge)
            //Text(text = "Date: ${liftWithPR.pr.date}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
