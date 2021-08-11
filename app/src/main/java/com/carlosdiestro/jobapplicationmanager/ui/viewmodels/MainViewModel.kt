package com.carlosdiestro.jobapplicationmanager.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosdiestro.jobapplicationmanager.datasource.entities.JobApplication
import com.carlosdiestro.jobapplicationmanager.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val jobApplications = mainRepository.getAll()

    fun insertJobApplication(jobApplication: JobApplication) = viewModelScope.launch {
        mainRepository.insertJobApplication(jobApplication)
    }
}