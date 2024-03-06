package com.kostyarazboynik.todoapp.data.datasource.local.database.converter

import androidx.room.TypeConverter
import com.kostyarazboynik.todoapp.data.datasource.local.database.models.ImportanceEntity

class ImportanceEntityConverter {

    @TypeConverter
    fun fromImportance(importance: ImportanceEntity) = importance.name

    @TypeConverter
    fun toImportance(value: String) = enumValueOf<ImportanceEntity>(value)
}