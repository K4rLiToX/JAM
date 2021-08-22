package com.carlosdiestro.jobapplicationmanager.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlosdiestro.jobapplicationmanager.R
import com.carlosdiestro.jobapplicationmanager.databinding.JobApplicationItemLayoutBinding
import com.carlosdiestro.jobapplicationmanager.datasource.entities.JobApplication
import com.carlosdiestro.jobapplicationmanager.utils.Constants
import com.carlosdiestro.jobapplicationmanager.utils.Constants.ACCEPTED_STATUS
import com.carlosdiestro.jobapplicationmanager.utils.Constants.PENDING_STATUS

class JobApplicationAdapter(
    private val ctx: Context,
    private val iJobApplicationListener: IJobApplicationListener
) :
    ListAdapter<JobApplication, JobApplicationAdapter.JobApplicationViewHolder>(
        JobApplicationDiffUtilCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobApplicationViewHolder {
        return JobApplicationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.job_application_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: JobApplicationViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { iJobApplicationListener.onItemClicked(getItem(position)) }
    }

    inner class JobApplicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = JobApplicationItemLayoutBinding.bind(itemView)

        fun bind(jobApplication: JobApplication) = binding.apply {
            jobApplication.apply {
                txtCompany.text = company
                txtApplicationDate.text = Constants.timeStampToDate(applicationDate)
                txtJobPosition.text = jobPosition
                txtLocation.text = location
                txtStatus.text = mapStatusToString(ctx, status)
                txtStatus.setBackgroundColor(getStatusBackgroundColor(status))
            }
        }

        private fun getStatusBackgroundColor(status: Int) = when(status) {
            PENDING_STATUS -> ctx.getColor(R.color.light_blue)
            ACCEPTED_STATUS -> ctx.getColor(R.color.light_green)
            else -> ctx.getColor(R.color.light_red)
        }
    }
}

interface IJobApplicationListener {
    fun onItemClicked(jobApplication: JobApplication)
}

private object JobApplicationDiffUtilCallback : DiffUtil.ItemCallback<JobApplication>() {
    override fun areItemsTheSame(oldItem: JobApplication, newItem: JobApplication) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: JobApplication, newItem: JobApplication) =
        oldItem == newItem
}