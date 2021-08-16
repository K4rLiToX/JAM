package com.carlosdiestro.jobapplicationmanager.ui.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.jobapplicationmanager.datasource.entities.JobApplication
import com.carlosdiestro.jobapplicationmanager.datasource.repositories.MainRepository
import com.carlosdiestro.jobapplicationmanager.utils.FilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val allJobApplications = mainRepository.getAll()
    private val pendingJobApplications = mainRepository.getPendingJobApplications()
    private val acceptedJobApplications = mainRepository.getAcceptedJobApplications()
    private val rejectedJobApplications = mainRepository.getRejectedJobApplications()

    val jobApplications = MediatorLiveData<List<JobApplication>>()

    var filterType = FilterType.ALL

    init {
        jobApplications.addSource(allJobApplications) { result ->
            if (filterType == FilterType.ALL) {
                result?.let { jobApplications.value = it }
            }
        }
        jobApplications.addSource(pendingJobApplications) { result ->
            if (filterType == FilterType.PENDING) {
                result?.let { jobApplications.value = it }
            }
        }
        jobApplications.addSource(acceptedJobApplications) { result ->
            if (filterType == FilterType.ACCEPTED) {
                result?.let { jobApplications.value = it }
            }
        }
        jobApplications.addSource(rejectedJobApplications) { result ->
            if (filterType == FilterType.REJECTED) {
                result?.let { jobApplications.value = it }
            }
        }
    }

    fun filterJobApplications(filterType: FilterType) = when (filterType) {
        FilterType.ALL -> allJobApplications.value?.let { jobApplications.value = it }
        FilterType.PENDING -> pendingJobApplications.value?.let { jobApplications.value = it }
        FilterType.ACCEPTED -> acceptedJobApplications.value?.let { jobApplications.value = it }
        FilterType.REJECTED -> rejectedJobApplications.value?.let { jobApplications.value = it }
    }.also {
        this.filterType = filterType
    }

    fun insertJobApplication(jobApplication: JobApplication) = viewModelScope.launch {
        mainRepository.insertJobApplication(jobApplication)
    }

    fun updateJobApplication(jobApplication: JobApplication) = viewModelScope.launch {
        mainRepository.updateJobApplication(jobApplication)
    }

    fun cleanNonPendingJobApplications() = viewModelScope.launch {
        mainRepository.clearNonPendingJobApplications()
    }
}