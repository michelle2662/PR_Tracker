package com.example.prtracker.ui.detail

import HomeViewModel
import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prtracker.data.room.models.LiftWithPR

@Composable
fun TabScreen(navController: NavController) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("PR", "Percentage", "Graph")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = {
                        tabIndex = index
                        when (title) {
                            "PR" -> navController.navigate("pr_tab")
                            "Percentage" -> navController.navigate("percentage_tab")
                            "Graph" -> navController.navigate("graph_tab")
                        }
                    }
                )
            }
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    navController: NavController,
    userId: Int
) {
    val homeViewModel: HomeViewModel = viewModel()
    val user = homeViewModel.state.collectAsState().value.users.find { it.id == userId }
    var showPopUp by remember { mutableStateOf(false) } // -> STATE
    val tabNavController = rememberNavController()

    user?.let { user ->
        val userPRsWithLifts by homeViewModel.getUserPRsWithLifts(userId).collectAsState(initial = emptyList())

        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        modifier = Modifier.height(70.dp),
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = { Text("${user.first_name} ${user.last_name} ", style = MaterialTheme.typography.titleLarge) },
                    )

                    TabScreen(navController = tabNavController)


                }

            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showPopUp = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                }
            }
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(it),
            ) {
                NavHost(
                    navController = tabNavController,
                    startDestination = "pr_tab"
                ) {
                    composable("pr_tab") {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            val sortedUserPRsWithLifts = userPRsWithLifts.sortedBy { it.lift.lift }

                            items(sortedUserPRsWithLifts) { liftWithPR ->
                                PRItem(liftWithPR, homeViewModel, userId)
                            }
                        }
                    }
                    composable("percentage_tab") {
                        PercentWorkoutScreen(userPRsWithLifts)
                    }
                    composable("graph_tab") {
                        //PercentWorkoutScreen()
                    }
                }
            }

        }

        //for adding new PR
        if (showPopUp) {
            AddNewPR(
                onDismiss = { showPopUp = false },
                onConfirm = { liftName, weight, repetitions ->
                    homeViewModel.insertPR(userId, liftName, weight, repetitions)
                },
                liftWithPR = null

            )
        }
    } ?: run {
        Text(text = "User not found", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun AddNewPR(
    onDismiss: () -> Unit,
    onConfirm: (String, Double, Int) -> Unit,
    liftWithPR: LiftWithPR?,
    update : Boolean = false,

    ) {
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
            Column {
                if (update){
                    if (liftWithPR != null) {
                        Text(liftWithPR.lift.lift)
                    }
                }else {
                    TextField(
                        value = liftName,
                        onValueChange = { liftName = it },
                        label = { Text("Lift") }
                    )
                }


                TextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("lb") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                TextField(
                    value = repetitions,
                    onValueChange = { repetitions = it },
                    label = { Text("Reps") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PRItem(
    liftWithPR: LiftWithPR,
    homeViewModel: HomeViewModel,
    userId: Int
) {
    var showPopUp by remember { mutableStateOf(false) } // -> STATE

    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                // Perform the action when the item is dismissed
                homeViewModel.deletePR(liftWithPR.pr)
                true
            } else {
                false
            }
        }
    )

    SwipeToDismiss(
        state = dismissState ,
        background = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.LightGray
                    DismissValue.DismissedToEnd, DismissValue.DismissedToStart -> Color.Red
            }
            )
        },
        dismissContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    showPopUp = true
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(text = "${liftWithPR.lift.lift}: ${liftWithPR.pr.weight} lb", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Repetitions: ${liftWithPR.pr.repetitions}", style = MaterialTheme.typography.bodyLarge)
                    //Text(text = "Date: ${liftWithPR.pr.date}", style = MaterialTheme.typography.bodyLarge)
                }
            }

        }
        )

        if (showPopUp){
            UpdatePR(
                liftWithPR,
                homeViewModel,
                onConfirm = { liftName, weight, repetitions ->
                    homeViewModel.insertPR(userId, liftName, weight, repetitions)},
                    onDismiss = { showPopUp = false },
            )
        }


//    if (showPopUp) {
//        AddNewPR(
//            onDismiss = { showPopUp = false },
//            onConfirm = { liftName, weight, repetitions ->
//                homeViewModel.insertPR(liftWithPR.pr.userId, liftName, weight, repetitions)
//            },
//            update = true,
//            liftWithPR = liftWithPR
//        )
//    }
}

@Composable
fun UpdatePR(
    liftWithPR: LiftWithPR,
    homeViewModel: HomeViewModel,
    onDismiss: () -> Unit,
    onConfirm: (String, Double, Int) -> Unit
) {
    var weight by remember { mutableStateOf("") }
    var repetitions by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    val weightValue = weight.toDoubleOrNull() ?: 0.0
                    val repetitionsValue = repetitions.toIntOrNull() ?: 0
                    onConfirm(liftWithPR.lift.lift, weightValue, repetitionsValue)
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
            Column {
                    TextField(
                        value = liftWithPR.lift.lift,
                        onValueChange = { liftWithPR.lift.lift = it },
                        label = { Text("Lift") }
                    )
                TextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("lb") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                TextField(
                    value = repetitions,
                    onValueChange = { repetitions = it },
                    label = { Text("Reps") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )


                }



            }

    )
}

