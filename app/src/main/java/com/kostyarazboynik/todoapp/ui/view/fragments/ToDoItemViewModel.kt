package com.kostyarazboynik.todoapp.ui.view.fragments

import androidx.lifecycle.ViewModel
import com.kostyarazboynik.todoapp.domain.models.ToDoItem
import com.kostyarazboynik.todoapp.domain.models.UiState
import com.kostyarazboynik.todoapp.domain.repository.ToDoItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ToDoItemViewModel @Inject constructor(
    private val repository: ToDoItemRepository,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _toDoItem: MutableStateFlow<UiState<ToDoItem>> = MutableStateFlow(UiState.Start)
    val toDoItem = _toDoItem.asStateFlow()

    fun getToDoItem(id: String) {
        if (toDoItem.value == UiState.Start)
            coroutineScope.launch(Dispatchers.IO) {
                _toDoItem.emit(UiState.Success(data = repository.getToDoItemById(id = id)))
            }
    }

    fun setToDoItem() {
        if (toDoItem.value == UiState.Start)
            _toDoItem.value = UiState.Success(data = ToDoItem())
    }

    fun addToDoItem(toDoItem: ToDoItem) =
        coroutineScope.launch(Dispatchers.IO) {
            repository.createToDoItem(toDoItem = toDoItem.copy())
        }

    fun deleteToDoItem(toDoItem: ToDoItem) =
        coroutineScope.launch(Dispatchers.IO) {
            repository.deleteToDoItem(toDoItem = toDoItem)
        }

    fun updateToDoItem(toDoItem: ToDoItem) {
        toDoItem.changedAt?.time = System.currentTimeMillis()
        coroutineScope.launch(Dispatchers.IO) {
            repository.updateToDoItem(toDoItem = toDoItem)
        }
    }
}
