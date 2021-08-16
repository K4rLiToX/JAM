package com.carlosdiestro.jobapplicationmanager.datasource.respositories

import androidx.lifecycle.MutableLiveData
import com.carlosdiestro.jobapplicationmanager.datasource.entities.JobApplication
import com.carlosdiestro.jobapplicationmanager.interfaces.IMainRepository
import com.carlosdiestro.jobapplicationmanager.utils.Constants.PENDING_STATUS

class MainRepositoryMock: IMainRepository {

    private var allJobApplicationList = mutableListOf<JobApplication>()
    private var pendingJobApplicationList = mutableListOf<JobApplication>()
    private var acceptedJobApplicationList = mutableListOf<JobApplication>()
    private var rejectedJobApplicationList = mutableListOf<JobApplication>()

    private val observableAllJobApplications =
        MutableLiveData<List<JobApplication>>(allJobApplicationList)
    private val observablePendingJobApplications =
        MutableLiveData<List<JobApplication>>(pendingJobApplicationList)
    private val observableAcceptedJobApplications =
        MutableLiveData<List<JobApplication>>(acceptedJobApplicationList)
    private val observableRejectedJobApplications =
        MutableLiveData<List<JobApplication>>(rejectedJobApplicationList)

    override suspend fun insertJobApplication(jobApplication: JobApplication) {
        allJobApplicationList.add(jobApplication)
        refreshLiveData()
    }

    override suspend fun updateJobApplication(jobApplication: JobApplication) {
        val index = allJobApplicationList.indexOf(jobApplication)
        allJobApplicationList.apply {
            removeAt(index)
            add(index, jobApplication)
        }
        refreshLiveData()
    }

    override suspend fun clearNonPendingJobApplications() {
        allJobApplicationList = (allJobApplicationList.filter { jobApplication -> jobApplication.status == PENDING_STATUS }).toMutableList()
        refreshLiveData()
    }

    override fun getAll() = observableAllJobApplications


    override fun getPendingJobApplications() = observablePendingJobApplications


    override fun getAcceptedJobApplications() = observableAcceptedJobApplications


    override fun getRejectedJobApplications() = observableRejectedJobApplications

    private fun refreshLiveData() {
        observableAllJobApplications.postValue(allJobApplicationList)
    }
}