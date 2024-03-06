package com.kostyarazboynik.todoapp.utils

import android.content.Context
import com.kostyarazboynik.todoapp.R
import com.kostyarazboynik.todoapp.data.datasource.local.models.NotificationModeEntity
import com.kostyarazboynik.todoapp.domain.models.Importance
import com.kostyarazboynik.todoapp.domain.models.NotificationMode

object Mappers {

    // Importance -> String
    fun Importance.getString(context: Context): String =
        when (this) {
            Importance.HIGH -> context.getString(R.string.high_priority)
            Importance.LOW -> context.getString(R.string.low_priority)
            Importance.NO -> context.getString(R.string.no_priority)
        }

    // String -> Importance
    fun String.toImportance(context: Context): Importance =
        when (this) {
            context.getString(R.string.high_priority) -> Importance.HIGH
            context.getString(R.string.low_priority) -> Importance.LOW
            else -> Importance.NO
        }

    /**
     * options - string array of ["Day before deadline", "Hour before deadline", "Time of the deadline"]
     */
    fun NotificationMode.toOption(context: Context): String {
        val options = context.resources.getStringArray(R.array.notification_labels)
        return when (this) {
            NotificationMode.DAY -> options[0]
            NotificationMode.HOUR -> options[1]
            NotificationMode.DEADLINE -> options[2]
        }
    }

    fun NotificationModeEntity.toModel(): NotificationMode = when (this) {
        NotificationModeEntity.DAY -> NotificationMode.DAY
        NotificationModeEntity.DEADLINE -> NotificationMode.DEADLINE
        NotificationModeEntity.HOUR -> NotificationMode.HOUR
    }
}