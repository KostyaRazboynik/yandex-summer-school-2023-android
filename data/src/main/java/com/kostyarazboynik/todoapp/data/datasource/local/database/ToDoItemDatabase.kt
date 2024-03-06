package com.kostyarazboynik.todoapp.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kostyarazboynik.todoapp.data.datasource.local.database.dao.ToDoItemDao
import com.kostyarazboynik.todoapp.data.datasource.local.database.models.ToDoItemEntity

/**
 * Database for ToDoItems
 *
 * @author Konstantin Kovalev
 *
 */
@Database(entities = [ToDoItemEntity::class], version = 1)
abstract class ToDoItemDatabase : RoomDatabase() {
    abstract fun toDoItemDao(): ToDoItemDao
}