package com.kolaczyn.boards.data.models

data class ReplyDto(
    val id: Int,
    val message: String,
    val createdAt: String?,
    val imageUrl: String? = null,
)