package com.carlosdiestro.jobapplicationmanager.datasource.repositories

import com.carlosdiestro.jobapplicationmanager.datasource.dao.JobApplicationDAO
import com.carlosdiestro.jobapplicationmanager.datasource.entities.JobApplication
import com.carlosdiestro.jobapplicationmanager.interfaces.IMainRepository
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val jobApplicationDAO: JobApplicationDAO
) : IMainRepository {

    override suspend fun insertJobApplication(jobApplication: JobApplication) =
        jobApplicationDAO.insertJobApplication(jobApplication)

    override suspend fun updateJobApplication(jobApplication: JobApplication) =
        jobApplicationDAO.updateJobApplication(jobApplication)

    override suspend fun clearNonPendingJobApplications() = with(jobApplicationDAO) {
        clearAcceptedJobApplications()
        clearRejectedJobApplications()
    }

    override fun getAll() =
        jobApplicationDAO.getAll()

    override fun getPendingJobApplications() =
        jobApplicationDAO.getPendingJobApplications()

    override fun getAcceptedJobApplications() =
        jobApplicationDAO.getAcceptedJobApplications()

    override fun getRejectedJobApplications() =
        jobApplicationDAO.getRejectedJobApplications()
}