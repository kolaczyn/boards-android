package com.kolaczyn.boards.models

data class ThreadsRepliesDto(
    val id: Int,
    val title: String?,
    val replies: List<ReplyDto>
)