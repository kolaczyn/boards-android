package com.kolaczyn.boards.data

import android.util.Log
import com.kolaczyn.boards.data.models.BoardsThreadsDto
import com.kolaczyn.boards.data.models.ThreadsRepliesDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
