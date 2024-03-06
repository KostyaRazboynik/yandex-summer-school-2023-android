package com.kostyarazboynik.todoapp.ui.view.fragments.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kostyarazboynik.todoapp.data.datasource.local.SharedPreferencesAppSettings
import com.kostyarazboynik.todoapp.data.datasource.local.models.NotificationModeEntity
import com.kostyarazboynik.todoapp.data.repository.YandexPassportRepository
import com.kostyarazboynik.todoapp.domain.models.NotificationMode
import com.kostyarazboynik.todoapp.domain.models.UiState
import com.kostyarazboynik.todoapp.domain.repository.ToDoItemRepository
import com.kostyarazboynik.todoapp.utils.Mappers.toModel
import com.kostyarazboynik.todoapp.utils.Mappers.toOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View Model for Yandex Passport Repository
 *
 * @author Kovalev Konstantin
 *
 */
class YandexAuthViewModel @Inject constructor(
    private val yandexPassportRepository: YandexPassportRepository,
    private val repository: ToDoItemRepository,
    private val sharedPreferences: SharedPreferencesAppSettings,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _accountInfo = MutableStateFlow<String?>(null)
    val accountInfo: StateFlow<String?> = _accountInfo

    init {
        getInfo()
    }

    private fun getInfo() =
        viewModelScope.launch(Dispatchers.IO) {
            _accountInfo.value = yandexPassportRepository.getInfo()?.login
        }

    fun setNotificationPermission(permission: Boolean) =
        sharedPreferences.setNotificationPermission(permission = permission)

    fun getNotificationPermission(): Boolean =
        sharedPreferences.getNotificationPermission()

    fun setTheme(theme: Int) = sharedPreferences.setTheme(theme = theme)

    fun getTheme(): Int = sharedPreferences.getTheme()

    fun getOption(context: Context): String =
        sharedPreferences.getNotificationOption().toModel().toOption(context = context)

    fun putThemeMode(mode: String) =
        sharedPreferences.setNotificationOption(
            option = NotificationModeEntity.valueOf(mode.uppercase())
        )

    fun getLabel(): NotificationMode = sharedPreferences.getNotificationOption().toModel()

    fun updateNotificationIntents() =
        coroutineScope.launch(Dispatchers.IO) {
            repository.getToDoItemsFlow().collect { uiState ->
                when (uiState) {
                    is UiState.Success -> repository.updateNotifications(items = uiState.data)
                    else -> {}
                }
            }
        }
}