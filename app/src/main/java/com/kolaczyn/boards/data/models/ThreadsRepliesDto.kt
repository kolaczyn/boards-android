package com.kolaczyn.boards.data.models

data class ThreadsRepliesDto(
    val id: Int,
    val title: String?,
    val replies: List<ReplyDto>
)