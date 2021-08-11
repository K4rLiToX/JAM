package com.carlosdiestro.jobapplicationmanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.carlosdiestro.jobapplicationmanager.R
import com.carlosdiestro.jobapplicationmanager.databinding.FragmentNewJobApplicationBinding
import com.carlosdiestro.jobapplicationmanager.datasource.entities.JobApplication
import com.carlosdiestro.jobapplicationmanager.ui.viewmodels.MainViewModel
import com.carlosdiestro.jobapplicationmanager.utils.Constants.PENDING_STATUS
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

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
        with(binding) {
            btnBackToJobApplications.setOnClickListener { navigateBack() }
            btnAddNewApplication.setOnClickListener { addNewJobApplication() }
            etApplicationDate.setOnClickListener { openDatePicker() }
        }
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }

    private fun addNewJobApplication() {
        if (!areFieldsEmpty()) {
            insertJobApplication()
            navigateBack()
        } else {
            showWarning()
        }
    }

    private fun insertJobApplication() {
        with(binding) {
            viewModel.insertJobApplication(
                JobApplication(
                    etJobPosition.text.toString().trim(),
                    etCompany.text.toString().trim(),
                    etLocation.text.toString().trim(),
                    PENDING_STATUS,
                    etApplicationDate.text.toString()
                )
            )
        }
    }

    private fun showWarning() {
        Snackbar.make(
            requireContext(),
            binding.btnAddNewApplication,
            getString(R.string.add_new_job_application_error),
            Snackbar.LENGTH_SHORT
        ).setAnchorView(binding.btnAddNewApplication).show()
    }

    private fun areFieldsEmpty() = with(binding) {
        etJobPosition.text.toString().isEmpty() ||
                etCompany.text.toString().isEmpty() ||
                etLocation.text.toString().isEmpty() ||
                etApplicationDate.text.toString().isEmpty()
    }

    private fun openDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.date_picker_title))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker.show(parentFragmentManager, "datePicker")
        manageDatePickerClickListeners(datePicker)
    }

    private fun manageDatePickerClickListeners(datePicker: MaterialDatePicker<Long>) {
        with(datePicker) {
            addOnPositiveButtonClickListener {
                binding.etApplicationDate.setText(
                    SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Date(selection!!))
                )
                dismiss()
            }
            addOnNegativeButtonClickListener {
                dismiss()
            }
        }
    }
}