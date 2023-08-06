package com.kolaczyn.boards.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kolaczyn.boards.components.Boards
import com.kolaczyn.boards.components.BoardsThreads
import com.kolaczyn.boards.components.ThreadsReplies
import com.kolaczyn.boards.data.BoardsSource

@Composable
fun Navigation(boardsSource: BoardsSource) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.BoardsScreen.route) {
        composable(route = Screen.BoardsScreen.route) {
            Boards(boardsSource, navController)
        }

        composable(
            route = "${Screen.ThreadsScreen.route}/{boardSlug}",
            listOf(navArgument("boardSlug") {
                type = NavType.StringType
            })
        ) { entry ->
            BoardsThreads(
                boardsSource,
                entry.arguments?.getString("boardSlug"),
                navController
            )
        }

        composable(
            route = "${Screen.RepliesScreen.route}/{boardSlug}/{threadId}",
            listOf(navArgument("boardSlug") {
                type = NavType.StringType
            }, navArgument("threadId") {
                type = NavType.IntType
            })
        ) { entry ->
            ThreadsReplies(
                boardsSource,
                entry.arguments?.getString("boardSlug"),
                entry.arguments?.getInt("threadId")
            )
        }
    }
}

