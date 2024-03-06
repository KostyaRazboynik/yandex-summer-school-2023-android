package com.kostyarazboynik.todoapp.data.datasource.local.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kostyarazboynik.todoapp.data.datasource.local.database.converter.ImportanceEntityConverter


/**
 * Entity for ToDoItem
 *
 * @author Konstantin Kovalev
 *
 */

@Entity(tableName = "todo_items")
data class ToDoItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "description")
    var title: String,
    @TypeConverters(ImportanceEntityConverter::class)
    @ColumnInfo(name = "importance")
    var importance: ImportanceEntity,
    @ColumnInfo(name = "deadline")
    var deadline: Long?,
    @ColumnInfo(name = "done")
    var done: Boolean,
    @ColumnInfo(name = "createdAt")
    val createdAt: Long,
    @ColumnInfo(name = "changedAt")
    var changedAt: Long?
)