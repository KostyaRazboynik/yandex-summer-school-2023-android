package com.kostyarazboynik.todoapp.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * ToDoItemResponse
 *
 * @author Konstantin Kovalev
 *
 */
data class ToDoItemResponse(
    @SerializedName("revision")
    val revision: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("element")
    val element: ToDoItemNetworkEntity
)