package com.kostyarazboynik.todoapp.data.datasource.local


import android.content.Context
import com.kostyarazboynik.todoapp.data.datasource.local.models.NotificationModeEntity
import com.kostyarazboynik.todoapp.utils.Constants.THEME_FOLLOW_SYSTEM
import com.kostyarazboynik.todoapp.utils.Constants.TOKEN_API
import java.util.UUID
import javax.inject.Inject

/**
 * Shared Preferences App Settings
 *
 * @author Konstantin Kovalev
 *
 */

class SharedPreferencesAppSettings @Inject constructor(
    context: Context,
) {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    init {
        if (!sharedPreferences.contains(SHARED_PREFERENCES_DEVICE_TAG))
            editor.putString(SHARED_PREFERENCES_DEVICE_TAG, UUID.randomUUID().toString())
                .apply()

        if (!sharedPreferences.contains(SHARED_PREFERENCES_THEME_TAG))
            setTheme(theme = THEME_FOLLOW_SYSTEM)

        if (!sharedPreferences.contains(SHARED_PREFERENCES_NOTIFICATION_TAG))
            editor.putString(SHARED_PREFERENCES_NOTIFICATION_TAG, "")

        if (!sharedPreferences.contains(SHARED_PREFERENCES_NOTIFICATION_OPTION))
            editor.putString(
                SHARED_PREFERENCES_NOTIFICATION_OPTION,
                NotificationModeEntity.DAY.toString().lowercase()
            )

    }

    fun setNotificationOption(option: NotificationModeEntity) =
        editor.putString(SHARED_PREFERENCES_NOTIFICATION_OPTION, option.toString().lowercase())
            .apply()

    fun getNotificationOption(): NotificationModeEntity =
        NotificationModeEntity.valueOf(
            (sharedPreferences.getString(
                SHARED_PREFERENCES_NOTIFICATION_OPTION,
                NotificationModeEntity.DAY.toString().lowercase()
            ) ?: NotificationModeEntity.DAY.toString().lowercase()).uppercase()
        )


    fun setCurrentToken(token: String) =
        editor.putString(SHARED_PREFERENCES_TOKEN, token).apply()

    fun getCurrentToken(): String =
        sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, TOKEN_API) ?: TOKEN_API

    fun getDeviceId(): String =
        sharedPreferences.getString(SHARED_PREFERENCES_DEVICE_TAG, "0d") ?: "0d"

    fun putRevisionId(revision: Int) =
        editor.putInt(SHARED_PREFERENCES_REVISION_TAG, revision).apply()

    fun getRevisionId(): Int = sharedPreferences.getInt(SHARED_PREFERENCES_REVISION_TAG, 1)

    fun getTheme(): Int =
        sharedPreferences.getInt(SHARED_PREFERENCES_THEME_TAG, THEME_FOLLOW_SYSTEM)

    fun setTheme(theme: Int) =
        sharedPreferences.edit().putInt(SHARED_PREFERENCES_THEME_TAG, theme).apply()

    fun getNotificationPermission(): Boolean =
        sharedPreferences.getBoolean(SHARED_PREFERENCES_NOTIFICATION_PERMISSION, false)

    fun setNotificationPermission(permission: Boolean) =
        editor.putBoolean(SHARED_PREFERENCES_NOTIFICATION_PERMISSION, permission).apply()

    fun getFullCurrentToken(): String = getCurrentToken().toFullToken()

    private fun String.toFullToken() =
        if (this == TOKEN_API) "Bearer $TOKEN_API"
        else "OAuth $this"

    fun addNotification(id: String) =
        editor.putString(SHARED_PREFERENCES_NOTIFICATION_TAG, getNotificationsId() + " $id").apply()

    fun getNotificationsId(): String =
        sharedPreferences.getString(SHARED_PREFERENCES_NOTIFICATION_TAG, "").toString()

    fun removeNotification(id: String) {
        val arr = ArrayList(getNotificationsId().split(" "))
        if (arr.contains(id)) arr.remove(id)
        editor.putString(
            SHARED_PREFERENCES_NOTIFICATION_TAG,
            arr.fold("") { previous, next -> "$previous $next" })
            .apply()
    }

    companion object {
        const val SHARED_PREFERENCES_NAME = "settings"
        const val SHARED_PREFERENCES_REVISION_TAG = "current_revision"
        const val SHARED_PREFERENCES_DEVICE_TAG = "current_device_id"
        const val SHARED_PREFERENCES_TOKEN = "current_token"
        const val SHARED_PREFERENCES_THEME_TAG = "theme"
        const val SHARED_PREFERENCES_NOTIFICATION_TAG = "notifications"
        const val SHARED_PREFERENCES_NOTIFICATION_OPTION = "notification_option"
        const val SHARED_PREFERENCES_NOTIFICATION_PERMISSION = "notification_permission"
    }
}