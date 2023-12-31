package com.kolaczyn.boards.data

import com.kolaczyn.boards.data.models.BoardDto
import com.kolaczyn.boards.data.models.BoardsThreadsDto
import com.kolaczyn.boards.data.models.CreateReplyDto
import com.kolaczyn.boards.data.models.ThreadsRepliesDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BoardsApiClient {
    @GET("/boards")
    suspend fun getBoards(): List<BoardDto>

    @GET("/boards/{slug}")
    suspend fun getBoardsReplies(@Path("slug") slug: String): BoardsThreadsDto

    @GET("/boards/{slug}/threads/{threadId}")
    suspend fun getThreadsReplies(
        @Path("slug") slug: String,
        @Path("threadId") threadId: Int
    ): ThreadsRepliesDto

    @POST("/boards/{slug}/threads/{threadId}/replies")
    suspend fun postReply(
        @Path("slug") slug: String,
        @Path("threadId") threadId: Int,
        @Body reply: CreateReplyDto
    )
}

object RetrofitBuilder {
    private const val BASE_URL = "https://api.kolaczyn.com"

    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val boardsService: BoardsApiClient = getRetrofit().create(BoardsApiClient::class.java)
}
