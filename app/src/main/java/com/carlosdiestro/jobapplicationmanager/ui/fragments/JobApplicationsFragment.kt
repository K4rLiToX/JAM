package com.carlosdiestro.jobapplicationmanager.ui.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.carlosdiestro.jobapplicationmanager.R
import com.carlosdiestro.jobapplicationmanager.databinding.FragmentJobApplicationsBinding
import com.carlosdiestro.jobapplicationmanager.datasource.entities.JobApplication
import com.carlosdiestro.jobapplicationmanager.ui.adapters.JobApplicationAdapter
import com.carlosdiestro.jobapplicationmanager.ui.viewmodels.MainViewModel
import com.carlosdiestro.jobapplicationmanager.utils.Constants.ACCEPTED_STATUS
import com.carlosdiestro.jobapplicationmanager.utils.Constants.PENDING_STATUS
import com.carlosdiestro.jobapplicationmanager.utils.Constants.REJECTED_STATUS
import com.carlosdiestro.jobapplicationmanager.utils.FilterType
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


@AndroidEntryPoint
class JobApplicationsFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentJobApplicationsBinding
    private lateinit var recyclerAdapter: JobApplicationAdapter
    private lateinit var jobApplicationList: List<JobApplication>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobApplicationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUpRecyclerView()
        setFilterText(viewModel.filterType)
        observeAllJobApplications()
    }

    private fun setUpClickListeners() {
        binding.apply {
            btnNewApplication.setOnClickListener { navigateToNewJobApplication() }
            btnFilter.setOnClickListener { v -> openStatusFilterMenu(v, R.menu.status_filter_menu) }
            btnClean.setOnClickListener { openCleanConfirmationDialog() }
        }
    }

    private fun navigateToNewJobApplication() {
        findNavController().navigate(R.id.jobApplicationsToNewJobApplication)
    }

    private fun openStatusFilterMenu(v: View, @MenuRes statusFilterMenu: Int) {
        val popUp = PopupMenu(requireContext(), v)

        popUp.apply {
            menuInflater.inflate(statusFilterMenu, popUp.menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.status_filter_by_all -> {
                        viewModel.filterJobApplications(FilterType.ALL)
//                        setFilterText(viewModel.filterType)
                        true
                    }
                    R.id.status_filter_by_pending -> {
                        viewModel.filterJobApplications(FilterType.PENDING)
//                        setFilterText(viewModel.filterType)
                        true
                    }
                    R.id.status_filter_by_accepted -> {
                        viewModel.filterJobApplications(FilterType.ACCEPTED)
//                        setFilterText(viewModel.filterType)
                        true
                    }
                    R.id.status_filter_by_rejected -> {
                        viewModel.filterJobApplications(FilterType.REJECTED)
//                        setFilterText(viewModel.filterType)
                        true
                    }
                    else -> false
                }.also { setFilterText(viewModel.filterType) }
            }
            show()
        }
    }

    private fun openCleanConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.clean_alert_dialog_title))
            .setMessage(getString(R.string.clean_alert_dialog_message))
            .setPositiveButton(getString(R.string.clean_alert_dialog_positive_button_text)) { dialog, _ ->
                viewModel.cleanNonPendingJobApplications()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.clean_alert_dialog_negative_button_text)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setUpRecyclerView() = binding.rvJobApplications.apply {
        recyclerAdapter = JobApplicationAdapter(requireContext())
        adapter = recyclerAdapter
        ItemTouchHelper(swipeGesture).attachToRecyclerView(this)
    }

    private fun setFilterText(filterType: FilterType) = binding.apply {
        val id = when (filterType) {
            FilterType.ALL -> R.string.status_filter_menu_all
            FilterType.PENDING -> R.string.status_filter_menu_pending
            FilterType.ACCEPTED -> R.string.status_filter_menu_accepted
            FilterType.REJECTED -> R.string.status_filter_menu_rejected
        }
        txtShowFilter.text = getString(id)
    }

    private fun observeAllJobApplications() = viewModel.apply {
        jobApplications.observe(viewLifecycleOwner, { jobApplications ->
            jobApplicationList = jobApplications
            recyclerAdapter.submitList(jobApplications)
        })
    }

    private val swipeGesture =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                jobApplicationList[viewHolder.absoluteAdapterPosition].apply {
                    if (status == PENDING_STATUS) {
                        when (direction) {
                            ItemTouchHelper.LEFT -> status = REJECTED_STATUS
                            ItemTouchHelper.RIGHT -> status = ACCEPTED_STATUS
                        }
                        viewModel.updateJobApplication(this)
                    }
                }
                binding.rvJobApplications.adapter = recyclerAdapter
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(
                        resources.getColor(
                            R.color.light_red,
                            requireActivity().theme
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.ic_reject)
                    .addSwipeRightBackgroundColor(
                        resources.getColor(
                            R.color.light_green,
                            requireActivity().theme
                        )
                    )
                    .addSwipeRightActionIcon(R.drawable.ic_check)
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
}