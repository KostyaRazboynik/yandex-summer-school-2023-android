package com.kostyarazboynik.todoapp.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kostyarazboynik.todoapp.data.datasource.local.SharedPreferencesAppSettings
import com.kostyarazboynik.todoapp.data.internet_checker.ConnectivityObserver
import com.kostyarazboynik.todoapp.data.repository.YandexPassportRepository
import com.kostyarazboynik.todoapp.domain.notifications.NotificationsScheduler
import com.kostyarazboynik.todoapp.domain.repository.ToDoItemRepository
import com.kostyarazboynik.todoapp.ui.view.fragments.ToDoItemViewModel
import com.kostyarazboynik.todoapp.ui.view.fragments.add.compose.ToDoItemViewModelCompose
import com.kostyarazboynik.todoapp.ui.view.fragments.auth.AuthViewModel
import com.kostyarazboynik.todoapp.ui.view.fragments.main.MainViewModel
import com.kostyarazboynik.todoapp.ui.view.fragments.settings.YandexAuthViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * View Model Factory
 *
 * @author Kovalev Konstantin
 *
 */
class ViewModelFactory @Inject constructor(
    private val repositoryImpl: ToDoItemRepository,
    private val connectivityObserver: ConnectivityObserver,
    private val coroutineScope: CoroutineScope,
    private val yandexPassportRepository: YandexPassportRepository,
    private val sharedPreferences: SharedPreferencesAppSettings,
    private val schedulerImpl: NotificationsScheduler
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            MainViewModel::class.java ->
                MainViewModel(
                    repository = repositoryImpl,
                    connection = connectivityObserver,
                    coroutineScope = coroutineScope
                )

            AuthViewModel::class.java -> AuthViewModel(
                repository = repositoryImpl,
                coroutineScope = coroutineScope,
                sharedPreferences = sharedPreferences,
                schedulerImpl = schedulerImpl
            )

            ToDoItemViewModel::class.java -> ToDoItemViewModel(
                repository = repositoryImpl,
                coroutineScope = coroutineScope
            )

            YandexAuthViewModel::class.java -> YandexAuthViewModel(
                yandexPassportRepository = yandexPassportRepository,
                repository = repositoryImpl,
                sharedPreferences = sharedPreferences,
                coroutineScope = coroutineScope
            )

            ToDoItemViewModelCompose::class.java -> ToDoItemViewModelCompose(
                repository = repositoryImpl,
                coroutineScope = coroutineScope
            )

            else -> error("Unknown view model class")
        } as T
}