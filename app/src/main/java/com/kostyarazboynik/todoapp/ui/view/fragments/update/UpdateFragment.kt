package com.kostyarazboynik.todoapp.ui.view.fragments.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kostyarazboynik.todoapp.appComponent
import com.kostyarazboynik.todoapp.databinding.FragmentUpdateBinding
import com.kostyarazboynik.todoapp.ui.view.fragments.ToDoItemViewModel


/**
 * Update Fragment
 *
 * @author Kovalev Konstantin
 *
 */
class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private var fragmentViewComponent: UpdateViewController? = null

    private val viewModel: ToDoItemViewModel by viewModels { requireContext().appComponent.findViewModelFactory() }

    private val args: UpdateFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentUpdateBinding.inflate(layoutInflater)
        binding.args = args

        viewModel.getToDoItem(id = args.currentItem.id)

        fragmentViewComponent = UpdateViewController(
            binding = binding,
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