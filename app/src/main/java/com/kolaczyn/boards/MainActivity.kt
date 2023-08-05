package com.kolaczyn.boards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kolaczyn.boards.models.ReplyDto
import com.kolaczyn.boards.ui.theme.BoardsTheme
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoardsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RepliesList()
                }
            }
        }
    }
}


fun formatDate(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")
    val parsedDate = ZonedDateTime.parse(dateString, formatter)

    val displayFormatter = DateTimeFormatter.ofPattern("MM-dd 'at' HH:mm")
    return parsedDate.withZoneSameInstant(ZoneId.systemDefault()).format(displayFormatter)
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
fun RepliesList() {
    val mockReplies = createMockReplies()

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
            for (reply in mockReplies) {
                ReplyItem(reply = reply)
            }
        }

    }
}