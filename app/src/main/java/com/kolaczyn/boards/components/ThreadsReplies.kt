package com.kolaczyn.boards.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kolaczyn.boards.data.BoardsSource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadsReplies(boardsSource: BoardsSource, boardSlug: String?, threadId: Int?) {
    var coroutineScope = rememberCoroutineScope()
    val replies by boardsSource.getThreadsReplies(boardSlug ?: "a", threadId = threadId ?: 0)
        .collectAsState(initial = null)

    var replyInput by remember { mutableStateOf("") }

    var sendReply: () -> Unit = {
        val message = replyInput
        coroutineScope.launch {
            boardsSource.postReply(boardSlug ?: "a", threadId ?: 0, message).collect {
            }
        }
        replyInput = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
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

            Button(onClick = {
                sendReply()
            }) {
                Text(text = "Reply")
            }
            TextField(
                value = replyInput,
                onValueChange = { replyInput = it },
                label = { Text("Reply") }
            )
        }
    }
}

