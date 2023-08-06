package com.kolaczyn.boards.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kolaczyn.boards.data.BoardsSource

@Composable
fun ThreadsReplies(boardsSource: BoardsSource, threadId: Int?) {
    val replies by boardsSource.getThreadsReplies("a", threadId = threadId ?: 0)
        .collectAsState(initial = null)

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
        }
    }
}
