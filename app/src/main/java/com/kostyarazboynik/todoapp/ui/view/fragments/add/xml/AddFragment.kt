package com.kostyarazboynik.todoapp.ui.view.fragments.add.xml


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kostyarazboynik.todoapp.appComponent
import com.kostyarazboynik.todoapp.databinding.FragmentAddBinding
import com.kostyarazboynik.todoapp.ui.utils.SharedViewHelper
import com.kostyarazboynik.todoapp.ui.view.fragments.ToDoItemViewModel


/**
 * Add Fragment
 *
 * @author Kovalev Konstantin
 *
 */
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private var fragmentViewComponent: AddViewController? = null

    private val sharedViewHelper = SharedViewHelper
    private val viewModel: ToDoItemViewModel by viewModels { requireContext().appComponent.findViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentAddBinding.inflate(layoutInflater)

        viewModel.setToDoItem()

        fragmentViewComponent = AddViewController(
            binding = binding,
            sharedViewHelper = sharedViewHelper,
            viewModel = viewModel,
            navController = findNavController(),
            context = requireContext(),
            lifecycleOwner = viewLifecycleOwner
        ).apply {
            setUpViewModel()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewComponent = null
        _binding = null
    }
}