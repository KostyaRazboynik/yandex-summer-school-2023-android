package com.kostyarazboynik.todoapp.ui.view.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kostyarazboynik.todoapp.appComponent
import com.kostyarazboynik.todoapp.databinding.FragmentMainBinding

/**
 * Main Fragment
 *
 * @author Kovalev Konstantin
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var fragmentViewComponent: MainViewController? = null

    private val viewModel: MainViewModel by viewModels { requireContext().appComponent.findViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(layoutInflater)

        fragmentViewComponent = MainViewController(
            binding = binding,
            viewModel = viewModel,
            navController = findNavController(),
            context = requireContext(),
            lifecycleOwner = viewLifecycleOwner,
            layoutInflater = layoutInflater
        ).apply {
            setUpViews()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewComponent = null
        _binding = null
    }
}