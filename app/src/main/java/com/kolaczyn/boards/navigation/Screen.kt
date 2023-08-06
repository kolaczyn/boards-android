package com.kolaczyn.boards.navigation

sealed class Screen(val route: String) {
    object BoardsScreen : Screen(route = "boards_screen")
    object ThreadsScreen : Screen(route = "threads_screen")
    object RepliesScreen : Screen(route = "replies_screen")

    fun withArgs(vararg args: String): String = buildString {
        append(route)
        args.forEach { arg ->
            append("/$arg")
        }
    }
}
