package com.kostyarazboynik.todoapp.ui.view.fragments.settings

import android.content.Context
import android.os.Build
import android.view.ContextThemeWrapper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kostyarazboynik.todoapp.R
import com.kostyarazboynik.todoapp.databinding.FragmentSettingsBinding
import com.kostyarazboynik.todoapp.utils.Constants.THEME_FOLLOW_SYSTEM
import com.kostyarazboynik.todoapp.utils.Constants.THEME_NIGHT_NO
import com.kostyarazboynik.todoapp.utils.Constants.THEME_NIGHT_YES
import kotlinx.coroutines.launch

/**
 * Settings Fragment View Controller
 *
 * @author Kovalev Konstantin
 *
 */
class SettingsViewController(
    private val binding: FragmentSettingsBinding,
    private val navController: NavController,
    private val viewModel: YandexAuthViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    fragment: Fragment,
) {

    fun setUpViews() {
        setUpSelector()
        setUpSelectorListener()
        setUpCloseButton()
        collectAccountInfo()
        setUpPermissionBtn()
        setUpNotificationOption()
    }

    private fun setUpNotificationOption() {
        binding.notificationOption.setOnClickListener {

            val notificationOptions = context.resources.getStringArray(R.array.notification_options)
            val notificationLabels = context.resources.getStringArray(R.array.notification_labels)

            val selectedIndex = notificationOptions.indexOf(viewModel.getLabel().toString().lowercase())

            MaterialAlertDialogBuilder(
                ContextThemeWrapper(context, R.style.AlertDialogCustom)
            ).apply {
                setSingleChoiceItems(notificationLabels, selectedIndex) { dialog, which ->
                    viewModel.putThemeMode(mode = notificationOptions[which])
                    binding.notificationOption.text = context.getString(
                        R.string.notification_option,
                        viewModel.getOption(context)
                    )
                    binding.notificationOption.visibility = View.VISIBLE
                    viewModel.updateNotificationIntents()
                    dialog.dismiss()
                }
                show()
                create()
            }
        }
    }

    private fun setUpPermissionBtn() {
        binding.autoDownloadSwitch.setOnCheckedChangeListener { _, checked ->
            binding.autoDownloadSwitch.isClickable = checked
            if (!checked) {
                viewModel.setNotificationPermission(permission = false)
                binding.notificationOption.visibility = View.GONE
            } else {
                binding.notificationOption.text =
                    context.getString(R.string.notification_option, viewModel.getOption(context))
                binding.notificationOption.visibility = View.VISIBLE
            }
        }

        if (viewModel.getNotificationPermission()) {
            binding.autoDownloadSwitch.isChecked = true
            binding.autoDownloadSwitch.isClickable = true
            binding.notificationOption.text =
                context.getString(R.string.notification_option, viewModel.getOption(context))
            binding.notificationOption.visibility = View.VISIBLE
        }

        binding.autoDownload.setOnClickListener {
            if (!binding.autoDownloadSwitch.isChecked) {
                if (Build.VERSION.SDK_INT >= 33)
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                else showSettingDialog()
            }
        }
    }

    private fun collectAccountInfo() =
        lifecycleOwner.lifecycleScope.launch {
            viewModel.accountInfo.collect {
                if (it != null) binding.loggedName.text = it
                else binding.loggedName.text = context.getString(R.string.guest_mode)
            }
        }

    private fun setUpCloseButton() =
        binding.toolbar.setOnClickListener {
            navController.popBackStack()
        }

    private fun setUpSelectorListener() =
        binding.themeSelector.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.light_theme_button -> {
                    viewModel.setTheme(THEME_NIGHT_NO)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                R.id.dark_theme_button -> {
                    viewModel.setTheme(THEME_NIGHT_YES)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                R.id.system_theme_button -> {
                    viewModel.setTheme(THEME_FOLLOW_SYSTEM)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }

    private fun setUpSelector() =
        when (viewModel.getTheme()) {
            THEME_NIGHT_NO -> binding.themeSelector.check(R.id.light_theme_button)
            THEME_NIGHT_YES -> binding.themeSelector.check(R.id.dark_theme_button)
            else -> binding.themeSelector.check(R.id.system_theme_button)
        }

    private val notificationPermissionLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            viewModel.setNotificationPermission(permission = isGranted)
            binding.autoDownloadSwitch.isChecked = isGranted
        }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(
            ContextThemeWrapper(context, R.style.AlertDialogCustom)
        )
            .setTitle(context.getString(R.string.permission_title))
            .setMessage(context.getString(R.string.permission_message))
            .setPositiveButton(context.getString(R.string.permission_yes)) { _, _ ->
                viewModel.setNotificationPermission(true)
                binding.autoDownloadSwitch.isChecked = true
                binding.notificationOption.text =
                    context.getString(R.string.notification_option, viewModel.getOption(context))
                binding.notificationOption.visibility = View.VISIBLE
            }
            .setNegativeButton(context.getString(R.string.permission_no)) { _, _ ->
                viewModel.setNotificationPermission(false)
                binding.notificationOption.visibility = View.GONE
            }
            .show()
    }
}