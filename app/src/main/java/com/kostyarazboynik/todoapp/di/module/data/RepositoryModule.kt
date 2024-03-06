package com.kostyarazboynik.todoapp.di.module.data

import com.kostyarazboynik.todoapp.data.repository.RemoteToDoItemRepositoryImpl
import com.kostyarazboynik.todoapp.data.repository.ToDoItemRepositoryImpl
import com.kostyarazboynik.todoapp.domain.notifications.NotificationsScheduler
import com.kostyarazboynik.todoapp.domain.repository.RemoteToDoItemRepository
import com.kostyarazboynik.todoapp.domain.repository.ToDoItemRepository
import com.kostyarazboynik.todoapp.utils.notifications.NotificationsSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface RepositoryModule {

    @Reusable
    @Binds
    fun bindToDoRepository(toDoRepository: ToDoItemRepositoryImpl): ToDoItemRepository

    @Reusable
    @Binds
    fun bindRemoteToDoRepository(remoteToDoRepository: RemoteToDoItemRepositoryImpl): RemoteToDoItemRepository

    @Reusable
    @Binds
    fun bindSchedulerRepository(notificationsScheduler: NotificationsSchedulerImpl): NotificationsScheduler
}
