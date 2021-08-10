package com.carlosdiestro.jobapplicationmanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.carlosdiestro.jobapplicationmanager.databinding.FragmentNewJobApplicationBinding
import com.carlosdiestro.jobapplicationmanager.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewJobApplicationFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentNewJobApplicationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewJobApplicationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.btnBackToJobApplications.setOnClickListener { navigateBack() }
        binding.btnAddNewApplication.setOnClickListener { addNewJobApplication() }
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }

    private fun addNewJobApplication() {
        navigateBack()
    }
}