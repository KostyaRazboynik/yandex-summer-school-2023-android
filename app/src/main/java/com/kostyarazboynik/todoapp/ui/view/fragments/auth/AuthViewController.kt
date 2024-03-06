package com.kostyarazboynik.todoapp.ui.view.fragments.auth

import android.content.Context
import android.os.Build
import android.view.ContextThemeWrapper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kostyarazboynik.todoapp.R
import com.kostyarazboynik.todoapp.databinding.FragmentAuthBinding
import com.kostyarazboynik.todoapp.utils.Constants.TOKEN_API
import com.yandex.authsdk.YandexAuthSdk

/**
 * Auth Fragment View Controller
 *
 * @author Kovalev Konstantin
 *
 */
class AuthViewController(
    private val binding: FragmentAuthBinding,
    private val navController: NavController,
    private val yandexAuthSdk: YandexAuthSdk,
    private val viewModel: AuthViewModel,
    private val fragment: Fragment,
    private val context: Context,
) {
    fun setUpViews() {
        setUpPermission()
        setUpToken()
        setUpYandexAuthButton()
        setUpLogInButton()
    }

    private fun setUpPermission() {
        if (!viewModel.getNotificationPermission()) {
            if (Build.VERSION.SDK_INT >= 33) {
                fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    viewModel.setNotificationPermission(permission = isGranted)
                }.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            } else showSettingDialog()
        }
    }

    private fun setUpLogInButton() =
        binding.logInGuestButton.setOnClickListener {
            viewModel.checkToken(curToken = TOKEN_API)
            navController.navigate(R.id.action_fragmentAuth_to_listFragment)
        }

    private fun setUpYandexAuthButton() =
        binding.yandexAuthButton.setOnClickListener {
            viewModel.launchRegister(yandexAuthSdk = yandexAuthSdk)
        }


    private fun setUpToken() {
        if (viewModel.setUpRegister(fragment = fragment, yandexAuthSdk = yandexAuthSdk))
            navController.navigate(R.id.action_fragmentAuth_to_listFragment)
    }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(
            ContextThemeWrapper(context, R.style.AlertDialogCustom)
        )
            .setTitle(context.getString(R.string.permission_title))
            .setMessage(context.getString(R.string.permission_message))
            .setPositiveButton(context.getString(R.string.permission_yes)) { _, _ ->
                viewModel.setNotificationPermission(permission = true)
            }
            .setNegativeButton(context.getString(R.string.permission_no)) { _, _ ->
                viewModel.setNotificationPermission(permission = false)
            }
            .show()
    }
}