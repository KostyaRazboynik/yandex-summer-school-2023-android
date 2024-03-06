package com.kostyarazboynik.todoapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * ToDoItem data model
 *
 * @author Konstantin Kovalev
 *
 */
@Parcelize
data class ToDoItem(
    var id: String,
    var title: String,
    var importance: Importance,
    var deadline: Date? = null,
    var done: Boolean,
    var createdAt: Date,
    var changedAt: Date? = null
) : Parcelable {

    constructor() : this(
        id = "",
        title = "",
        importance = Importance.NO,
        deadline= null,
        done = false,
        createdAt = Date(0),
        changedAt = null
    )
}