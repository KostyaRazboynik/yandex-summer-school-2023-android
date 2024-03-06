package com.kostyarazboynik.todoapp.ui.view.fragments.auth

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.kostyarazboynik.todoapp.data.datasource.local.SharedPreferencesAppSettings
import com.kostyarazboynik.todoapp.domain.notifications.NotificationsScheduler
import com.kostyarazboynik.todoapp.domain.repository.ToDoItemRepository
import com.kostyarazboynik.todoapp.utils.Constants
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthSdk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val repository: ToDoItemRepository,
    private val coroutineScope: CoroutineScope,
    private val sharedPreferences: SharedPreferencesAppSettings,
    private val schedulerImpl: NotificationsScheduler
) : ViewModel() {

    private lateinit var register: ActivityResultLauncher<Intent>

    private fun deleteCurrentItems() {
        schedulerImpl.cancelAll()
        coroutineScope.launch(Dispatchers.IO) {
            repository.deleteCurrentToDoItems()
        }
    }

    fun setNotificationPermission(permission: Boolean) =
        sharedPreferences.setNotificationPermission(permission = permission)

    fun getNotificationPermission(): Boolean =
        sharedPreferences.getNotificationPermission()

    private fun setUpToken(result: ActivityResult, data: Intent?, sdk: YandexAuthSdk): Boolean {
        val yandexAuthToken = sdk.extractToken(result.resultCode, data)
        if (yandexAuthToken != null) {
            checkToken(curToken = yandexAuthToken.value)
            return true
        }
        return false
    }

    fun checkToken(curToken: String) {
        if (curToken != sharedPreferences.getCurrentToken()) {
            sharedPreferences.setCurrentToken(token = curToken)
            sharedPreferences.putRevisionId(revision = 0)
            deleteCurrentItems()
        }
    }

    fun setUpRegister(fragment: Fragment, yandexAuthSdk: YandexAuthSdk): Boolean {
        var navigate = false
        register =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Constants.REQUEST_LOGIN_SDK_CODE) {
                    val data: Intent? = result.data
                    if (data != null) {
                        try {
                            navigate = setUpToken(result = result, data = data, sdk = yandexAuthSdk)
                        } catch (_: YandexAuthException) {
                        }
                    }
                }
            }
        return navigate
    }

    fun launchRegister(yandexAuthSdk: YandexAuthSdk) =
        register.launch(yandexAuthSdk.createLoginIntent(YandexAuthLoginOptions.Builder().build()))

}
