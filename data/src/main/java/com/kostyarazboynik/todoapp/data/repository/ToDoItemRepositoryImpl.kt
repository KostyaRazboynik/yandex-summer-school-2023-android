package com.kostyarazboynik.todoapp.data.repository

import android.util.Log
import com.kostyarazboynik.todoapp.data.repository.utils.Mappers.entityToModel
import com.kostyarazboynik.todoapp.data.repository.utils.Mappers.modelToEntity
import com.kostyarazboynik.todoapp.data.datasource.local.database.dao.ToDoItemDao
import com.kostyarazboynik.todoapp.domain.models.DataState
import com.kostyarazboynik.todoapp.domain.models.ToDoItem
import com.kostyarazboynik.todoapp.domain.models.UiState
import com.kostyarazboynik.todoapp.domain.notifications.NotificationsScheduler
import com.kostyarazboynik.todoapp.domain.repository.RemoteToDoItemRepository
import com.kostyarazboynik.todoapp.domain.repository.ToDoItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


/**
 * Repository for ToDoItems
 *
 * @author Konstantin Kovalev
 *
 */
class ToDoItemRepositoryImpl @Inject constructor(
    private val toDoItemDao: ToDoItemDao,
    private val networkSource: RemoteToDoItemRepository,
    private val notificationsScheduler: NotificationsScheduler,
) : ToDoItemRepository {

    override fun getToDoItemsFlow(): Flow<UiState<List<ToDoItem>>> = flow {
        emit(UiState.Start)
        toDoItemDao.getToDoItemsFlow().collect { list ->
            emit(UiState.Success(list.map { it.entityToModel() }))
        }
    }

    override suspend fun createToDoItem(toDoItem: ToDoItem) {
        toDoItemDao.createToDoItem(toDoItem.modelToEntity())
        notificationsScheduler.schedule(toDoItem)
        networkSource.createRemoteToDoItem(toDoItem)
    }

    override suspend fun deleteToDoItem(toDoItem: ToDoItem) {
        toDoItemDao.deleteToDoItem(toDoItem.modelToEntity())
        notificationsScheduler.cancel(toDoItem.id)
        networkSource.deleteRemoteToDoItem(toDoItem.id)
    }

    override suspend fun updateToDoItem(toDoItem: ToDoItem) {
        toDoItemDao.updateToDoItem(toDoItem.modelToEntity())
        notificationsScheduler.schedule(toDoItem)
        networkSource.updateRemoteToDoItem(toDoItem)
    }

    override fun getRemoteToDoItemsFlow(): Flow<UiState<List<ToDoItem>>> = flow {
        emit(UiState.Start)
        Log.d("56", "getRemoteToDoItemsFlow start")
        val list =
            networkSource.getMergedToDoItems(toDoItemDao.getToDoItems().map { it.entityToModel() })
        Log.d("58", list.toString())

        list.collect { state ->
            when (state) {
                DataState.Initial -> emit(UiState.Start)
                is DataState.Exception -> emit(UiState.Error(state.cause.message.toString()))
                is DataState.Result -> {
                    updateNotifications(state.data)
                    toDoItemDao.mergeToDoItems(state.data.map { it.modelToEntity() })
                    emit(UiState.Success(state.data))
                }
            }
        }
    }

    override fun getToDoItemById(id: String): ToDoItem =
        toDoItemDao.getToDoItemById(id).entityToModel()

    override suspend fun deleteCurrentToDoItems() = toDoItemDao.deleteAllToDoItems()

    override fun updateNotifications(items: List<ToDoItem>) {
        notificationsScheduler.cancelAll()
        for (item in items) notificationsScheduler.schedule(item)
    }
}