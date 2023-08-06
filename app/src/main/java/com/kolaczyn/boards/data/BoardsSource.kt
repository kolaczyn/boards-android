package com.kolaczyn.boards.data

import android.util.Log
import com.kolaczyn.boards.data.models.BoardDto
import com.kolaczyn.boards.data.models.BoardsThreadsDto
import com.kolaczyn.boards.data.models.ThreadsRepliesDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BoardsSource {
    private val apiService = RetrofitBuilder.boardsService

    fun getBoards(): Flow<List<BoardDto>> = flow {
        try {
            emit(apiService.getBoards())
        } catch (e: Exception) {
            Log.e("BoardsSource", e.toString())
            emit(emptyList())
        }
    }

    fun getBoardsThreads(slug: String): Flow<BoardsThreadsDto?> = flow {
        try {
            emit(apiService.getBoardsReplies(slug))
        } catch (e: Exception) {
            Log.e("BoardSource", e.toString())
            emit(null)
        }
    }

    fun getThreadsReplies(slug: String, threadId: Int): Flow<ThreadsRepliesDto?> = flow {
        try {
            emit(apiService.getThreadsReplies(slug, threadId))
        } catch (e: Exception) {
            Log.e("BoardsSource", e.toString())
            emit(null)
        }
    }

}
