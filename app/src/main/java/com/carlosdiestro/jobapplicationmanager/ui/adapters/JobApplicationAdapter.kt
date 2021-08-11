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
import com.carlosdiestro.jobapplicationmanager.utils.Constants.ACCEPTED_STATUS
import com.carlosdiestro.jobapplicationmanager.utils.Constants.PENDING_STATUS
import com.carlosdiestro.jobapplicationmanager.utils.Constants.REJECTED_STATUS

class JobApplicationAdapter(
    private val ctx: Context
) :
    ListAdapter<JobApplication, JobApplicationAdapter.JobApplicationViewHolder>(
        JobApplicationDiffUtilCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobApplicationViewHolder {
        return JobApplicationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.job_application_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: JobApplicationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class JobApplicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = JobApplicationItemLayoutBinding.bind(itemView)

        fun bind(jobApplication: JobApplication) = with(binding) {
            txtCompany.text = jobApplication.company
            txtApplicationDate.text = jobApplication.timeStampToDate(jobApplication.applicationDate)
            txtJobPosition.text = jobApplication.jobPosition
            txtLocation.text = jobApplication.location
            txtStatus.text = jobApplication.mapStatusToString(ctx, jobApplication.status)
            txtStatus.setBackgroundColor(getStatusBackgroundColor(jobApplication.status))
        }

        private fun getStatusBackgroundColor(status: Int) = when(status) {
            PENDING_STATUS -> ctx.getColor(R.color.light_blue)
            ACCEPTED_STATUS -> ctx.getColor(R.color.light_green)
            else -> ctx.getColor(R.color.light_red)
        }
    }
}

private object JobApplicationDiffUtilCallback : DiffUtil.ItemCallback<JobApplication>() {
    override fun areItemsTheSame(oldItem: JobApplication, newItem: JobApplication) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: JobApplication, newItem: JobApplication) =
        oldItem == newItem
}