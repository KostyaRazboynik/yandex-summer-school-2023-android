package com.kostyarazboynik.todoapp.ui.view.fragments.main

import androidx.lifecycle.ViewModel
import com.kostyarazboynik.todoapp.data.internet_checker.ConnectivityObserver
import com.kostyarazboynik.todoapp.data.internet_checker.ConnectivityObserver.Status.Unavailable
import com.kostyarazboynik.todoapp.domain.models.ToDoItem
import com.kostyarazboynik.todoapp.domain.models.UiState
import com.kostyarazboynik.todoapp.domain.repository.ToDoItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val repository: ToDoItemRepository,
    private val connection: ConnectivityObserver,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _status = MutableStateFlow(Unavailable)
    val status = _status.asStateFlow()
    private var internetState = status.value

    private val _visibility: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val visibility: StateFlow<Boolean> = _visibility

    private val _toDoItems = MutableStateFlow<UiState<List<ToDoItem>>>(
        UiState.Start
    )
    val toDoItems: StateFlow<UiState<List<ToDoItem>>> = _toDoItems.asStateFlow()


    val countComplete = _toDoItems.map { state ->
        when (state) {
            is UiState.Success -> state.data.count { it.done }
            else -> 0
        }
    }

    init {
        observeNetwork()
        loadData()
    }

    private fun observeNetwork() =
        coroutineScope.launch(Dispatchers.IO) {
            connection.observe().collectLatest {
                _status.emit(it)
            }
        }

    fun changeMode() {
        _visibility.value = _visibility.value.not()
    }


    fun loadData() =
        coroutineScope.launch(Dispatchers.IO) {
            _toDoItems.emitAll(repository.getToDoItemsFlow())
        }

    fun loadNetworkList() {
        coroutineScope.launch(Dispatchers.IO) {
            _toDoItems.emitAll(repository.getRemoteToDoItemsFlow())
        }
    }

    fun createItem(toDoItem: ToDoItem) =
        coroutineScope.launch(Dispatchers.IO) {
            repository.createToDoItem(toDoItem = toDoItem.copy())
        }

    fun deleteItem(toDoItem: ToDoItem) =
        coroutineScope.launch(Dispatchers.IO) {
            repository.deleteToDoItem(toDoItem = toDoItem)
        }

    fun updateItem(toDoItem: ToDoItem) {
        toDoItem.changedAt?.time = System.currentTimeMillis()
        coroutineScope.launch(Dispatchers.IO) {
            repository.updateToDoItem(toDoItem = toDoItem)
        }
    }

    fun updateInternetState() {
        internetState = status.value
    }

    fun internetStateAvailable(): Boolean = internetState == ConnectivityObserver.Status.Available

    fun checkInternetStateAvailable(status: ConnectivityObserver.Status): Boolean {
        var flag = true
        when (status) {
            ConnectivityObserver.Status.Available -> if (internetState != status) {
                //viewModel.loadNetworkList()
            }

            else -> if (internetState != status) flag = false
        }

        internetState = status
        return flag
    }
}
