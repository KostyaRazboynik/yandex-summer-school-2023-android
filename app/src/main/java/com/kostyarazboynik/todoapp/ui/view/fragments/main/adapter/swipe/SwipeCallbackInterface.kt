package com.kostyarazboynik.todoapp.ui.view.fragments.main.adapter.swipe

import com.kostyarazboynik.todoapp.domain.models.ToDoItem

/**
 * Interface for Swipe Callback
 *
 * @author Kovalev Konstantin
 *
 */
interface SwipeCallbackInterface {
    fun onDelete(toDoItem: ToDoItem)
    fun onChangeDone(toDoItem: ToDoItem)
}