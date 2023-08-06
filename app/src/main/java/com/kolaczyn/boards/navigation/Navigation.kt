package com.kolaczyn.boards.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kolaczyn.boards.components.BoardsThreads
import com.kolaczyn.boards.components.ThreadsReplies
import com.kolaczyn.boards.data.BoardsSource

@Composable
fun Navigation(boardsSource: BoardsSource) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            BoardsThreads(boardsSource, navController)
        }

        composable(
            route = Screen.DetailScreen.route + "/{threadId}",
            listOf(navArgument("threadId") {
                type = NavType.IntType
            })
        ) { entry ->
            ThreadsReplies(boardsSource, entry.arguments?.getInt("threadId"))
        }
    }
}

