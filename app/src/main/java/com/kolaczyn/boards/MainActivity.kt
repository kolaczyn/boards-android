package com.kolaczyn.boards

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    RepliesList(boardsSource = boardsSource)
                }
            }
        }
    }
}

interface BoardsApiClient {
    @GET("/boards/a/threads/47")
    suspend fun getThreadsReplies(): ThreadsRepliesDto

    @GET("/boards/a")
    suspend fun getBoardsReplies(): BoardsThreadsDto
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

    fun getThreadsReplies(): Flow<ThreadsRepliesDto?> = flow {
        try {
            emit(apiService.getThreadsReplies())
        } catch (e: Exception) {
            Log.e("BoardsSource", e.toString())
            emit(null)
        }
    }

    fun getBoardsThreads(): Flow<BoardsThreadsDto?> = flow {
        try {
            emit(apiService.getBoardsReplies())
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


fun createMockReplies(): List<ReplyDto> {
    val reply1 = ReplyDto(1, "Hello, how are you?", "2023-07-31T19:13:51.647387Z")
    val reply2 = ReplyDto(2, "I'm doing great, thanks!", "2023-07-31T19:13:51.647387Z")
    val reply3 = ReplyDto(3, "What about you?", "2023-07-31T19:13:51.647387Z")
    val reply4 = ReplyDto(4, "I'm doing fine too.", "2023-07-31T19:13:51.647387Z")
    val reply5 = ReplyDto(5, "Let's meet up later.", "2023-07-31T19:13:51.647387Z")

    return listOf(reply1, reply2, reply3, reply4, reply5)
}

@Composable
fun RepliesList(boardsSource: BoardsSource) {
    val replies by boardsSource.getThreadsReplies().collectAsState(initial = null)
    val threads by boardsSource.getBoardsThreads().collectAsState(initial = null)


    if (replies == null || threads == null) {
        Text(text = "Loading...")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
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
                        text = "Threads on /a/",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        for (thread in threads?.threads ?: emptyList()) {
                            Text(text = thread.message)
                        }
                    }
                }

            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(2f),
            contentAlignment = Alignment.BottomCenter
        ) {

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
                        text = "Thread #438",
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
    }


}