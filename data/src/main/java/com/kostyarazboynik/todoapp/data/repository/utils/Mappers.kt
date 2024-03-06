package com.kostyarazboynik.todoapp.data.repository.utils

import com.kostyarazboynik.todoapp.data.datasource.local.database.models.ImportanceEntity
import com.kostyarazboynik.todoapp.data.datasource.local.database.models.ToDoItemEntity
import com.kostyarazboynik.todoapp.data.datasource.remote.dto.ToDoItemNetworkEntity
import com.kostyarazboynik.todoapp.domain.models.Importance
import com.kostyarazboynik.todoapp.domain.models.ToDoItem
import java.util.Date

object Mappers {

    // ToDoItem -> ToDoItemNetworkEntity
    fun ToDoItem.modelToNetworkEntity(deviseId: String): ToDoItemNetworkEntity =
        ToDoItemNetworkEntity(
            id = this.id,
            title = this.title,
            importance = when (this.importance) {
                Importance.LOW -> "low"
                Importance.HIGH -> "high"
                Importance.NO -> "no"
            },
            deadline = this.deadline?.time,
            done = this.done,
            createdAt = this.createdAt.time,
            changedAt = this.changedAt?.time ?: 0,
            lastUpdatedBy = deviseId,
            color = null
        )

    // ToDoItemNetworkEntity -> ToDoItem
    fun ToDoItemNetworkEntity.networkEntityToModel(): ToDoItem =
        ToDoItem(
            id = this.id,
            title = this.title,
            importance = when (this.importance) {
                "low" -> Importance.LOW
                "high" -> Importance.HIGH
                else -> Importance.NO
            },
            deadline = this.deadline?.let { Date(it) },
            done = this.done,
            createdAt = Date(this.createdAt),
            changedAt = Date(this.changedAt)
        )

    // ToDoItem -> ToDoItemEntity
    fun ToDoItem.modelToEntity(): ToDoItemEntity =
        ToDoItemEntity(
            id = this.id,
            title = this.title,
            importance = this.importance.toEntity(),
            deadline = this.deadline?.time,
            done = this.done,
            createdAt = this.createdAt.time,
            changedAt = this.changedAt?.time
        )

    // ToDoItemEntity -> ToDoItem
    fun ToDoItemEntity.entityToModel(): ToDoItem =
        ToDoItem(
            id = this.id,
            title = this.title,
            importance = this.importance.toModel(),
            deadline = this.deadline?.let { Date(it) },
            done = this.done,
            createdAt = Date(this.createdAt),
            changedAt = this.changedAt?.let { Date(it) }
        )

    private fun Importance.toEntity(): ImportanceEntity = when (this) {
        Importance.LOW -> ImportanceEntity.LOW
        Importance.HIGH -> ImportanceEntity.HIGH
        Importance.NO -> ImportanceEntity.NO
    }

    private fun ImportanceEntity.toModel(): Importance = when (this) {
        ImportanceEntity.LOW -> Importance.LOW
        ImportanceEntity.HIGH -> Importance.HIGH
        ImportanceEntity.NO -> Importance.NO
    }
}