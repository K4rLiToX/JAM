package com.carlosdiestro.jobapplicationmanager.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.carlosdiestro.jobapplicationmanager.utils.MainCoroutineRule
import com.carlosdiestro.jobapplicationmanager.datasource.entities.JobApplication
import com.carlosdiestro.jobapplicationmanager.datasource.respositories.MainRepositoryMock
import com.carlosdiestro.jobapplicationmanager.utils.getOrAwaitValueTest
import com.carlosdiestro.jobapplicationmanager.utils.Constants.ACCEPTED_STATUS
import com.carlosdiestro.jobapplicationmanager.utils.Constants.REJECTED_STATUS
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel(MainRepositoryMock())
    }

    @Test
    fun insertJobApplication() {
        val jobApplication = JobApplication(
            "Android Developer",
            "Google",
            "Dublin, Ireland",
            applicationDate = 1629109889
        )

        viewModel.insertJobApplication(jobApplication)

        val allJobApplications = viewModel.jobApplications.getOrAwaitValueTest()

        assertThat(allJobApplications.contains(jobApplication)).isTrue()
    }

    @Test
    fun updateJobApplication() {
        val jobApplication = JobApplication(
            "Android Developer",
            "Google",
            "Dublin, Ireland",
            applicationDate = 1629109889
        )

        viewModel.insertJobApplication(jobApplication)

        val jobApplicationAdded = viewModel.jobApplications.getOrAwaitValueTest()[0]

        val newJobPosition = "QA Leader"
        jobApplicationAdded.jobPosition = newJobPosition

        viewModel.updateJobApplication(jobApplicationAdded)

        val allJobApplications = viewModel.jobApplications.getOrAwaitValueTest()

        assertThat(allJobApplications.contains(jobApplicationAdded)).isTrue()
        assertThat(allJobApplications.size).isEqualTo(1)
        assertThat(allJobApplications[0].jobPosition).isEqualTo(newJobPosition)
        assertThat(allJobApplications[0].id).isEqualTo(jobApplicationAdded.id)
    }

    @Test
    fun cleanNonPendingJobApplications() {
        val jobApplication1 = JobApplication(
            "Android Developer",
            "Google",
            "Dublin, Ireland",
            applicationDate = 1629109889
        )
        val jobApplication2 = JobApplication(
            "QA Leader",
            "Accenture",
            "Barcelona, Spain",
            ACCEPTED_STATUS,
            1629109889
        )
        val jobApplication3 = JobApplication(
            "Junior Software Engineer",
            "Spotify",
            "Stockholm, Sweden",
            REJECTED_STATUS,
            1629109889
        )

        viewModel.apply {
            insertJobApplication(jobApplication1)
            insertJobApplication(jobApplication2)
            insertJobApplication(jobApplication3)
            cleanNonPendingJobApplications()
        }

        val allJobApplications = viewModel.jobApplications.getOrAwaitValueTest()

        assertThat(allJobApplications.size).isEqualTo(1)
        assertThat(allJobApplications.contains(jobApplication1)).isTrue()
        assertThat(allJobApplications.contains(jobApplication2)).isFalse()
        assertThat(allJobApplications.contains(jobApplication3)).isFalse()
    }
}