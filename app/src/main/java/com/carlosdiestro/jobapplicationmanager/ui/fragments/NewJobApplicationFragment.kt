package com.carlosdiestro.jobapplicationmanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.carlosdiestro.jobapplicationmanager.R
import com.carlosdiestro.jobapplicationmanager.databinding.FragmentNewJobApplicationBinding
import com.carlosdiestro.jobapplicationmanager.datasource.entities.JobApplication
import com.carlosdiestro.jobapplicationmanager.ui.viewmodels.MainViewModel
import com.carlosdiestro.jobapplicationmanager.utils.Constants.GSON
import com.carlosdiestro.jobapplicationmanager.utils.Constants.PENDING_STATUS
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewJobApplicationFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentNewJobApplicationBinding
    private var dateTimeStamp: Long = 0
    private lateinit var jobApplication: JobApplication
    private var isEditionMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initialSetUp()
        binding = FragmentNewJobApplicationBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initialSetUp() {
        arguments?.let {
            val jobApplicationSerialized =
                NewJobApplicationFragmentArgs.fromBundle(it).jobApplication
            if (jobApplicationSerialized != "") {
                jobApplication = GSON.fromJson(jobApplicationSerialized, JobApplication::class.java)
                isEditionMode = true
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewStyle()
        setUpViewAnimations()
        setUpClickListeners()
        if (isEditionMode) setUpEditionMode()
    }

    private fun setUpViewStyle() {
        val shapeAppearanceModel =
            ShapeAppearanceModel().toBuilder().setTopRightCorner(CornerFamily.CUT, 300F).build()
        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)

        shapeDrawable.fillColor = ContextCompat.getColorStateList(requireContext(), R.color.white)
        ViewCompat.setBackground(binding.newJobLayout, shapeDrawable)
    }

    private fun setUpViewAnimations() {
        binding.apply {
            btnBackToJobApplications.animation = setAnimation(R.anim.view_slide_down)
            newJobTitle.animation = setAnimation(R.anim.view_slide_down)
            newJobLayout.animation = setAnimation(R.anim.view_slide_up)
            btnAddNewApplication.animation = setAnimation(R.anim.view_shows)
        }
    }

    private fun setAnimation(animation: Int) =
        AnimationUtils.loadAnimation(requireContext(), animation)

    private fun setUpClickListeners() {
        binding.apply {
            btnBackToJobApplications.setOnClickListener { navigateBack() }
            btnAddNewApplication.setOnClickListener { addOrEditJobApplication() }
            etApplicationDate.setOnClickListener { openDatePicker() }
            btnResetStatus.setOnClickListener { resetStatus() }
        }
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }

    private fun addOrEditJobApplication() {
        if (!areFieldsEmpty()) {
            if (!isEditionMode) insertJobApplication()
            else updateJobApplication()
            handleTransitionAnimation()
        } else {
            showWarning()
        }
    }

    private fun insertJobApplication() {
        binding.apply {
            viewModel.insertJobApplication(
                JobApplication(
                    etJobPosition.text.toString().trim(),
                    etCompany.text.toString().trim(),
                    etLocation.text.toString().trim(),
                    PENDING_STATUS,
                    dateTimeStamp
                )
            )
        }
    }

    private fun updateJobApplication() {
        binding.apply {
            jobApplication.apply {
                jobPosition = etJobPosition.text.toString().trim()
                company = etCompany.text.toString().trim()
                location = etLocation.text.toString().trim()
                if (!btnResetStatus.isEnabled) status = PENDING_STATUS
                applicationDate = dateTimeStamp
            }
        }
        viewModel.updateJobApplication(jobApplication)
    }

    private fun handleTransitionAnimation() {
        binding.newJobMotionLayout.transitionToEnd {
            navigateBack()
        }
    }

    private fun showWarning() {
        Snackbar.make(
            requireContext(),
            binding.btnAddNewApplication,
            getString(R.string.add_new_job_application_error),
            Snackbar.LENGTH_SHORT
        ).show()
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
                dateTimeStamp = selection!!
                binding.etApplicationDate.setText(
                    SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Date(dateTimeStamp))
                )
                dismiss()
            }
            addOnNegativeButtonClickListener {
                dismiss()
            }
        }
    }

    private fun resetStatus() {
        jobApplication.status = PENDING_STATUS
        binding.apply {
            btnResetStatus.text = getString(R.string.btn_reset_status_done)
            btnResetStatus.icon = null
            btnResetStatus.isEnabled = false
        }

    }

    private fun setUpEditionMode() {
        binding.apply {
            newJobTitle.text = getString(R.string.edit_job_title)
            jobApplication.apply {
                etJobPosition.setText(jobPosition)
                etCompany.setText(company)
                etLocation.setText(location)
                etApplicationDate.setText(timeStampToDate(applicationDate))
                dateTimeStamp = applicationDate
            }
            btnResetStatus.visibility = View.VISIBLE
        }
    }
}