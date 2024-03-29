package com.kostyarazboynik.todoapp.ui.view.fragments.update

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.kostyarazboynik.todoapp.R
import com.kostyarazboynik.todoapp.databinding.FragmentUpdateBinding
import com.kostyarazboynik.todoapp.domain.models.ToDoItem
import com.kostyarazboynik.todoapp.domain.models.UiState
import com.kostyarazboynik.todoapp.ui.utils.SharedViewHelper
import com.kostyarazboynik.todoapp.ui.view.activity.MainActivity
import com.kostyarazboynik.todoapp.ui.view.fragments.ToDoItemViewModel
import com.kostyarazboynik.todoapp.utils.Mappers.toImportance
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Update Fragment View Controller
 *
 * @author Kovalev Konstantin
 */
class UpdateViewController(
    private val binding: FragmentUpdateBinding,
    private val viewModel: ToDoItemViewModel,
    private val navController: NavController,
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {

    private val sharedViewHelper = SharedViewHelper

    private val datePicker =
        MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme).build()

    private var deadlineDate: Date? = null
    private var deadlineChanged = false

    private val args = binding.args!!

    fun setUpViewModel() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.toDoItem.collect { state ->
                    when (state) {
                        is UiState.Success -> {
                            setUpDeadlineText()
                            setUpPrioritySpinner()
                            setUpUpdateButton(toDoItem = state.data)
                            setUpDeleteButton(toDoItem = state.data)
                            setUpToolBar()
                            setUpDatePicker()
                            setUpDeadlineSwitch()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun setUpDeadlineText() {
        binding.currentDeadlineDate.text =
            if (args.currentItem.deadline != null) SimpleDateFormat(
                "dd-MM-yyyy HH:mm",
                Locale.getDefault()
            ).format(args.currentItem.deadline!!) else ""
    }

    private fun setUpPrioritySpinner() {
        binding.currentPrioritySp.onItemSelectedListener = sharedViewHelper.spinnerListener
    }

    private fun setUpUpdateButton(toDoItem: ToDoItem) =
        binding.saveBtn.setOnClickListener {
            updateToDoItem(toDoItem = toDoItem)
        }

    private fun setUpToolBar() =
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

    private fun setUpDatePicker() =
        datePicker.apply {
            addOnPositiveButtonClickListener {
                createDate(time = it)
            }
            addOnNegativeButtonClickListener {
                if (deadlineDate == null) deleteDate()
            }
            addOnCancelListener {
                if (deadlineDate == null) deleteDate()
            }
        }

    private fun setUpDeadlineSwitch() =
        binding.currentDeadlineSw.setOnClickListener {
            if (binding.currentDeadlineSw.isChecked) {
                binding.currentDeadlineSw.visibility = View.VISIBLE
                showDateTimePicker()
            } else deleteDate()
        }

    private fun setUpDeleteButton(toDoItem: ToDoItem) =
        binding.buttonDeleteTask.setOnClickListener {
            confirmItemRemoval(toDoItem = toDoItem)
        }

    private fun createDate(time: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val date = calendar.time

        binding.currentDeadlineDate.visibility = View.VISIBLE

        showTimePicker(date = date)
    }

    private fun showTimePicker(date: Date) = TimePickerDialog(
        context,
        R.style.MaterialCalendarTheme,
        { _, hourOfDay, minute ->

            deadlineDate = Date(date.time + hourOfDay * 3600000 + minute * 60000)
            binding.currentDeadlineDate.text =
                SimpleDateFormat("d MMMM y HH:mm", Locale.getDefault()).format(deadlineDate!!)
            deadlineChanged = true
        }, 0, 0, true
    ).apply {
        setOnCancelListener {
            deleteDate()
        }
        show()
    }

    private fun deleteDate() {
        binding.currentDeadlineSw.isChecked = false
        binding.currentDeadlineDate.text = ""
        deadlineDate = null
        deadlineChanged = true
    }

    private fun showDateTimePicker() =
        datePicker.show((context as MainActivity).supportFragmentManager, "materialDatePicker")

    private fun updateToDoItem(toDoItem: ToDoItem) {
        val title = binding.currentTitleEt.text.toString()

        setToDoItem(toDoItem = toDoItem, title = title, time = Calendar.getInstance().time)

        if (sharedViewHelper.verifyDataFromUser(title = title))
            updateVerifiedToDoItem(toDoItem = toDoItem)
        else makeToast(text = context.getString(R.string.fill_title))
    }

    private fun setToDoItem(toDoItem: ToDoItem, title: String, time: Date) {
        toDoItem.title = title
        toDoItem.importance =
            binding.currentPrioritySp.selectedItem.toString().toImportance(context)
        toDoItem.deadline = if (deadlineChanged) deadlineDate else args.currentItem.deadline
        toDoItem.done = args.currentItem.done
        toDoItem.changedAt = time
    }

    private fun updateVerifiedToDoItem(toDoItem: ToDoItem) {
        viewModel.updateToDoItem(toDoItem = toDoItem)

        makeToast(text = context.getString(R.string.successfully_updated))

        navController.popBackStack()
    }

    private fun confirmItemRemoval(toDoItem: ToDoItem) =
        AlertDialog.Builder(
            ContextThemeWrapper(context, R.style.AlertDialogCustom)
        ).apply {
            setPositiveButton(context.getString(R.string.yes_pos_btn)) { _, _ ->
                viewModel.deleteToDoItem(toDoItem = toDoItem)

                makeToast(text = "${context.getString(R.string.successfully_removed)}: '${toDoItem.title}'")

                navController.popBackStack()
            }
            setNegativeButton(context.getString(R.string.no_neg_btn)) { _, _ -> }
            setTitle("${context.getString(R.string.delete_warning_title)} '${toDoItem.title}'")
            setMessage("${context.getString(R.string.delete_warning)} '${toDoItem.title}'?")
            create()
            show()
        }

    private fun makeToast(text: String) =
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}
