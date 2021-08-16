package com.carlosdiestro.jobapplicationmanager.interfaces

import androidx.lifecycle.LiveData
import com.carlosdiestro.jobapplicationmanager.datasource.entities.JobApplication

interface IMainRepository {

    suspend fun insertJobApplication(jobApplication: JobApplication)

    suspend fun updateJobApplication(jobApplication: JobApplication)

    suspend fun clearNonPendingJobApplications()

    fun getAll(): LiveData<List<JobApplication>>

    fun getPendingJobApplications(): LiveData<List<JobApplication>>

    fun getAcceptedJobApplications(): LiveData<List<JobApplication>>

    fun getRejectedJobApplications(): LiveData<List<JobApplication>>
}