package com.carlosdiestro.jobapplicationmanager.ui.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.carlosdiestro.jobapplicationmanager.utils.Constants.REJECTED_STATUS
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
        observeJobApplications()
    }

    private fun setUpClickListeners() {
        binding.btnNewApplication.setOnClickListener { navigateToNewJobApplication() }
    }

    private fun navigateToNewJobApplication() {
        findNavController().navigate(com.carlosdiestro.jobapplicationmanager.R.id.jobApplicationsToNewJobApplication)
    }

    private fun setUpRecyclerView() = binding.rvJobApplications.apply {
        recyclerAdapter = JobApplicationAdapter(requireContext())
        adapter = recyclerAdapter
        ItemTouchHelper(swipeGesture).attachToRecyclerView(this)
    }

    private fun observeJobApplications() {
        viewModel.jobApplications.observe(viewLifecycleOwner, { jobApplications ->
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
                val jobApplication = jobApplicationList[viewHolder.absoluteAdapterPosition]
                when (direction) {
                    ItemTouchHelper.LEFT -> jobApplication.status = REJECTED_STATUS
                    ItemTouchHelper.RIGHT -> jobApplication.status = ACCEPTED_STATUS
                }
                viewModel.updateJobApplication(jobApplication)
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