package com.example.prtracker.ui.home

import HomeViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prtracker.data.room.models.User

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserScreen(
    onNavigate: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)
    val homeState by homeViewModel.state.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {onNavigate.invoke(-1)}) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    )
            }
        }
    ) {
        LazyColumn (modifier = Modifier.padding(it)){
            items(homeState.users) { user ->
                UserCard(user = user, onUserClick = { onNavigate(user.id) })
            }
        }
    }

}


@Composable
fun UserCard(user: User, onUserClick: (User) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onUserClick(user) }
    ) {
        Text(
            text = "${user.first_name} ${user.last_name}",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}