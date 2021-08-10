package com.carlosdiestro.jobapplicationmanager.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.carlosdiestro.jobapplicationmanager.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
}