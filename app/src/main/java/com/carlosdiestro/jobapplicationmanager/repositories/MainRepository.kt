package com.carlosdiestro.jobapplicationmanager.repositories

import com.carlosdiestro.jobapplicationmanager.datasource.dao.JobApplicationDAO
import com.carlosdiestro.jobapplicationmanager.datasource.entities.JobApplication
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val jobApplicationDAO: JobApplicationDAO
) {

    suspend fun insertJobApplication(jobApplication: JobApplication) =
        jobApplicationDAO.insertJobApplication(jobApplication)

    suspend fun updateJobApplication(jobApplication: JobApplication) =
        jobApplicationDAO.updateJobApplication(jobApplication)

    suspend fun clearNonPendingJobApplications() = jobApplicationDAO.apply {
        clearAcceptedJobApplications()
        clearRejectedJobApplications()
    }

    fun getAll() =
        jobApplicationDAO.getAll()

    fun getPendingJobApplications() =
        jobApplicationDAO.getPendingJobApplications()

    fun getAcceptedJobApplications() =
        jobApplicationDAO.getAcceptedJobApplications()

    fun getRejectedJobApplications() =
        jobApplicationDAO.getRejectedJobApplications()
}