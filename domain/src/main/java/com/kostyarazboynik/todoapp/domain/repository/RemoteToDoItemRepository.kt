package com.kostyarazboynik.todoapp.domain.repository

import com.kostyarazboynik.todoapp.domain.models.DataState
import com.kostyarazboynik.todoapp.domain.models.ToDoItem
import kotlinx.coroutines.flow.Flow

interface RemoteToDoItemRepository {

    suspend fun getMergedToDoItems(currentList: List<ToDoItem>): Flow<DataState<List<ToDoItem>>>

    suspend fun createRemoteToDoItem(newToDoItem: ToDoItem)

    suspend fun updateRemoteToDoItem(toDoItem: ToDoItem)

    suspend fun deleteRemoteToDoItem(id: String)

}