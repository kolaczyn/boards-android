package com.kolaczyn.boards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.kolaczyn.boards.data.BoardsSource
import com.kolaczyn.boards.navigation.Navigation
import com.kolaczyn.boards.ui.theme.BoardsTheme

class MainActivity : ComponentActivity() {
    private val boardsSource = BoardsSource()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoardsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(boardsSource = boardsSource)
                }
            }
        }
    }
}

