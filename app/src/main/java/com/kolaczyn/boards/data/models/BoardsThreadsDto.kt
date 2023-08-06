package com.kolaczyn.boards.data.models

data class ThreadPreviewDto(
    val id: Int,
    val message: String,
    val repliesCount: Int,
    val imageUrl: String?
)

data class BoardsThreadsDto(
    val slug: String,
    val name: String,
    val threads: List<ThreadPreviewDto>
)