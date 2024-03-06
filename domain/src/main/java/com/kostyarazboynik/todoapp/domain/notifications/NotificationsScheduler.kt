package com.kostyarazboynik.todoapp.domain.notifications

import com.kostyarazboynik.todoapp.domain.models.ToDoItem

interface NotificationsScheduler {
    fun schedule(item: ToDoItem)
    fun cancel(id: String)
    fun cancelAll()
}