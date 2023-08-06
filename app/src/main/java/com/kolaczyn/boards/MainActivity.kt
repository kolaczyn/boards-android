package com.kolaczyn.boards

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kolaczyn.boards.models.BoardsThreadsDto
import com.kolaczyn.boards.models.ReplyDto
import com.kolaczyn.boards.models.ThreadsRepliesDto
import com.kolaczyn.boards.ui.theme.BoardsTheme
import com.kolaczyn.boards.utils.formatDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

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

interface BoardsApiClient {
    @GET("/boards/{slug}/threads/{threadId}")
    suspend fun getThreadsReplies(
        @Path("slug") slug: String,
        @Path("threadId") threadId: Int
    ): ThreadsRepliesDto

    @GET("/boards/{slug}")
    suspend fun getBoardsReplies(@Path("slug") slug: String): BoardsThreadsDto
}

object RetrofitBuilder {
    private const val BASE_URL = "https://api.kolaczyn.com"

    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val boardsService: BoardsApiClient = getRetrofit().create(BoardsApiClient::class.java)
}

class BoardsSource {
    private val apiService = RetrofitBuilder.boardsService

    fun getThreadsReplies(slug: String, threadId: Int): Flow<ThreadsRepliesDto?> = flow {
        try {
            emit(apiService.getThreadsReplies(slug, threadId))
        } catch (e: Exception) {
            Log.e("BoardsSource", e.toString())
            emit(null)
        }
    }

    fun getBoardsThreads(slug: String): Flow<BoardsThreadsDto?> = flow {
        try {
            emit(apiService.getBoardsReplies(slug))
        } catch (e: Exception) {
            Log.e("BoardsSource", e.toString())
            emit(null)
        }
    }
}


@Composable
fun ReplyItem(reply: ReplyDto) {
    Column(modifier = Modifier.padding(bottom = 4.dp)) {
        Text(
            "Anonymous (no. ${reply.id}, ${formatDate(reply.createdAt ?: "")}",
            fontWeight = FontWeight.Light,
            fontSize = 14.sp
        )
        Text(text = reply.message, fontWeight = FontWeight.Normal)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BoardsTheme {
        ReplyItem(reply = ReplyDto(1, "Hello, how are you?", "2023-07-31T19:13:51.647387Z"))
    }
}


sealed class Screen(val route: String) {
    object MainScreen : Screen(route = "main_screen")
    object DetailScreen : Screen(route = "detail_screen")

    fun withArgs(vararg args: String): String = buildString {
        append(route)
        args.forEach { arg ->
            append("/$arg")
        }
    }
}

@Composable
fun ThreadsReplies(boardsSource: BoardsSource, navController: NavController, threadId: Int?) {
    val replies by boardsSource.getThreadsReplies("a", threadId = threadId ?: 0)
        .collectAsState(initial = null)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 4.dp)
        ) {
            Text(
                text = if (replies == null) "" else "Thread #${replies?.id}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                for (reply in replies?.replies ?: emptyList()) {
                    ReplyItem(reply = reply)
                }
            }
        }
    }
}

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
            ThreadsReplies(boardsSource, navController, entry.arguments?.getInt("threadId"))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardsThreads(boardsSource: BoardsSource, navController: NavController) {
    val threads by boardsSource.getBoardsThreads("a").collectAsState(initial = null)

    Scaffold { padding ->
        Column(modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 4.dp)
                ) {
                    Text(
                        text = if (threads != null) "Threads on /${threads?.slug}/" else "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        for (thread in threads?.threads ?: emptyList()) {
                            Button(onClick = {
                                navController.navigate(
                                    Screen.DetailScreen.withArgs(
                                        "${thread.id}"
                                    )
                                )
                            }) {
                                Text(text = thread.message)
                            }
                        }
                    }
                }
            }
        }
    }
}