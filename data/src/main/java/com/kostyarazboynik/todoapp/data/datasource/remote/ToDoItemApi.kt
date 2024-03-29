package com.kostyarazboynik.todoapp.data.datasource.remote

import com.kostyarazboynik.todoapp.data.datasource.remote.dto.ToDoItemRequest
import com.kostyarazboynik.todoapp.data.datasource.remote.dto.ToDoItemListRequest
import com.kostyarazboynik.todoapp.data.datasource.remote.dto.ToDoItemListResponse
import com.kostyarazboynik.todoapp.data.datasource.remote.dto.ToDoItemResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Api for ToDoItems
 *
 * @author Konstantin Kovalev
 *
 */
interface ToDoItemApi {

    @GET("list")
    suspend fun getList(@Header("Authorization") token: String): Response<ToDoItemListResponse>

    @PATCH("list")
    suspend fun updateList(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body body: ToDoItemListRequest,
    ): Response<ToDoItemListResponse>

    @POST("list")
    suspend fun addToDoItem(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body newItem: ToDoItemRequest,
    ): Response<ToDoItemResponse>

    @PUT("list/{id}")
    suspend fun updateToDoItem(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Path("id") itemId: String,
        @Body body: ToDoItemRequest,
    ): Response<ToDoItemResponse>

    @DELETE("list/{id}")
    suspend fun deleteToDoItem(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Path("id") itemId: String,
    ): Response<ToDoItemResponse>
}