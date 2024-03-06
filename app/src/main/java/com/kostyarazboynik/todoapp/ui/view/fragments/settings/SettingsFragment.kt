package com.kostyarazboynik.todoapp.ui.view.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kostyarazboynik.todoapp.appComponent
import com.kostyarazboynik.todoapp.databinding.FragmentSettingsBinding

/**
 * Settings Fragment
 *
 * @author Kovalev Konstantin
 *
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private var fragmentViewComponent: SettingsViewController? = null

    private val viewModel: YandexAuthViewModel by viewModels { requireContext().appComponent.findViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)

        fragmentViewComponent = SettingsViewController(
            binding = binding,
            navController = findNavController(),
            viewModel = viewModel,
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            fragment = this
        ).apply {
            setUpViews()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        fragmentViewComponent = null
    }
}