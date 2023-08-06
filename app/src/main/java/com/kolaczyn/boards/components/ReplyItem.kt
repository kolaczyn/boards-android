package com.kolaczyn.boards.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kolaczyn.boards.data.models.ReplyDto
import com.kolaczyn.boards.ui.theme.BoardsTheme
import com.kolaczyn.boards.utils.formatDate

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
fun ReplyItemPreview() {
    BoardsTheme {
        ReplyItem(reply = ReplyDto(1, "Hello, how are you?", "2023-07-31T19:13:51.647387Z"))
    }
}


