package com.kolaczyn.boards.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kolaczyn.boards.data.BoardsSource
import com.kolaczyn.boards.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Boards(boardsSource: BoardsSource, navController: NavController) {
    val boards by boardsSource.getBoards().collectAsState(initial = null)

    val navigate = { boardSlug: String ->
        navController.navigate(
            Screen.ThreadsScreen.withArgs(boardSlug)
        )
    }

    Scaffold(
        topBar = {
            AppBar(title = "Boards")
        }, content = { padding ->
            Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        for (thread in boards ?: emptyList()) {
                            Button(onClick = { navigate(thread.slug) }) {
                                Text(text = "${thread.name} - /${thread.slug}/")
                            }
                        }
                    }
                }
            }
        })
}
