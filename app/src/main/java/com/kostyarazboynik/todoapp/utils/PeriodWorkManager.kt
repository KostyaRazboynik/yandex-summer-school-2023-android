package com.kostyarazboynik.todoapp.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.kostyarazboynik.todoapp.App
import com.kostyarazboynik.todoapp.domain.repository.ToDoItemRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Period Work Manager
 *
 * @author Kovalev Konstantin
 *
 */
class PeriodWorkManager(
    private val context: Context,
    workerParameters: WorkerParameters,
) : Worker(context, workerParameters) {

    @Inject
    lateinit var repository: ToDoItemRepository

    override fun doWork(): Result {
        (context.applicationContext as App).appComponent.inject(this)
        mergeData()
        return Result.success()
    }

    private fun mergeData() = runBlocking {
        return@runBlocking repository.getRemoteToDoItemsFlow()
    }
}