package com.carlosdiestro.jobapplicationmanager.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.carlosdiestro.jobapplicationmanager.datasource.dao.JobApplicationDAO
import com.carlosdiestro.jobapplicationmanager.datasource.entities.JobApplication
import com.carlosdiestro.jobapplicationmanager.getOrAwaitValue
import com.carlosdiestro.jobapplicationmanager.utils.Constants.ACCEPTED_STATUS
import com.carlosdiestro.jobapplicationmanager.utils.Constants.REJECTED_STATUS
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @SmallTest is for Unit Tests
 * @MediumTes is for Integration Tests
 * @LargeTest is for end-to-end (UI) Tests
 * */

@RunWith(AndroidJUnit4::class)
@SmallTest
class JobApplicationTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: LocalDatabase
    private lateinit var dao: JobApplicationDAO

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocalDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getJobApplicationDAO()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertJobApplication() = runBlockingTest {
        val jobApplication = JobApplication(
            "Android Developer",
            "Google",
            "Dublin, Ireland",
            applicationDate = 1629109889
        )
        dao.insertJobApplication(jobApplication)

        val allJobApplications = dao.getAll().getOrAwaitValue()

        assertThat(allJobApplications.contains(jobApplication)).isTrue()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun updateJobApplication() = runBlockingTest {
        val jobApplication = JobApplication(
            "Android Developer",
            "Google",
            "Dublin, Ireland",
            applicationDate = 1629109889
        )
        dao.insertJobApplication(jobApplication)

        val jobApplicationAdded = dao.getAll().getOrAwaitValue()[0]
        val newCompany = "Accenture"

        jobApplicationAdded.company = newCompany

        dao.updateJobApplication(jobApplicationAdded)

        val allJobApplications = dao.getAll().getOrAwaitValue()

        assertThat(allJobApplications.contains(jobApplicationAdded)).isTrue()
        assertThat(allJobApplications.size).isEqualTo(1)
        assertThat(allJobApplications[0].company).isEqualTo(newCompany)
        assertThat(allJobApplications[0].id).isEqualTo(jobApplicationAdded.id)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clearAcceptedJobApplications() = runBlockingTest {
        val jobApplicationAccepted = JobApplication(
            "Android Developer",
            "Google",
            "Dublin, Ireland",
            ACCEPTED_STATUS,
            1629109889
        )
        val jobApplication = JobApplication(
            "Android Developer",
            "Google",
            "Dublin, Ireland",
            applicationDate = 1629109889
        )

        dao.apply {
            insertJobApplication(jobApplicationAccepted)
            insertJobApplication(jobApplication)
            clearAcceptedJobApplications()
        }

        val allJobApplications = dao.getAll().getOrAwaitValue()

        assertThat(allJobApplications.size).isEqualTo(1)
        assertThat(allJobApplications.contains(jobApplication)).isTrue()
        assertThat(allJobApplications.contains(jobApplicationAccepted)).isFalse()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clearRejectedJobApplications() = runBlockingTest {
        val jobApplicationRejected = JobApplication(
            "Android Developer",
            "Google",
            "Dublin, Ireland",
            REJECTED_STATUS,
            1629109889
        )
        val jobApplication = JobApplication(
            "Android Developer",
            "Google",
            "Dublin, Ireland",
            applicationDate = 1629109889
        )

        dao.apply {
            insertJobApplication(jobApplicationRejected)
            insertJobApplication(jobApplication)
            clearRejectedJobApplications()
        }

        val allJobApplications = dao.getAll().getOrAwaitValue()

        assertThat(allJobApplications.size).isEqualTo(1)
        assertThat(allJobApplications.contains(jobApplication)).isTrue()
        assertThat(allJobApplications.contains(jobApplicationRejected)).isFalse()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getAllJobApplications() = runBlockingTest {
        val jobApplication1 = JobApplication(
            "Android Developer",
            "Google",
            "Dublin, Ireland",
            applicationDate = 1629109889
        )
        val jobApplication2 = JobApplication(
            "Junior Software Engineer",
            "Facebook",
            "Belfast, Ireland",
            applicationDate = 1629109889
        )

        dao.apply {
            insertJobApplication(jobApplication1)
            insertJobApplication(jobApplication2)
        }

        val allJobApplications = dao.getAll().getOrAwaitValue()

        assertThat(allJobApplications.size).isEqualTo(2)
        assertThat(allJobApplications.contains(jobApplication1)).isTrue()
        assertThat(allJobApplications.contains(jobApplication2)).isTrue()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getPendingJobApplications() = runBlockingTest {
        val jobApplication1 = JobApplication(
            "Android Developer",
            "Google",
            "Dublin, Ireland",
            applicationDate = 1629109889
        )
        val jobApplication2 = JobApplication(
            "Junior Software Engineer",
            "Facebook",
            "Belfast, Ireland",
            applicationDate = 1629109889
        )

        dao.apply {
            insertJobApplication(jobApplication1)
            insertJobApplication(jobApplication2)
        }

        val pendingJobApplications = dao.getPendingJobApplications().getOrAwaitValue()

        assertThat(pendingJobApplications.size).isEqualTo(2)
        assertThat(pendingJobApplications.contains(jobApplication1)).isTrue()
        assertThat(pendingJobApplications.contains(jobApplication2)).isTrue()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getAcceptedJobApplications() = runBlockingTest {
        val jobApplication1 = JobApplication(
            "Android Developer",
            "Google",
            "Dublin, Ireland",
            ACCEPTED_STATUS,
            1629109889
        )
        val jobApplication2 = JobApplication(
            "Junior Software Engineer",
            "Facebook",
            "Belfast, Ireland",
            applicationDate = 1629109889
        )

        dao.apply {
            insertJobApplication(jobApplication1)
            insertJobApplication(jobApplication2)
        }

        val acceptedJobApplications = dao.getAcceptedJobApplications().getOrAwaitValue()

        assertThat(acceptedJobApplications.size).isEqualTo(1)
        assertThat(acceptedJobApplications.contains(jobApplication1)).isTrue()
        assertThat(acceptedJobApplications.contains(jobApplication2)).isFalse()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getRejectedJobApplications() = runBlockingTest {
        val jobApplication1 = JobApplication(
            "Android Developer",
            "Google",
            "Dublin, Ireland",
            REJECTED_STATUS,
            1629109889
        )
        val jobApplication2 = JobApplication(
            "Junior Software Engineer",
            "Facebook",
            "Belfast, Ireland",
            applicationDate = 1629109889
        )

        dao.apply {
            insertJobApplication(jobApplication1)
            insertJobApplication(jobApplication2)
        }

        val rejectedJobApplications = dao.getRejectedJobApplications().getOrAwaitValue()

        assertThat(rejectedJobApplications.size).isEqualTo(1)
        assertThat(rejectedJobApplications.contains(jobApplication1)).isTrue()
        assertThat(rejectedJobApplications.contains(jobApplication2)).isFalse()
    }

}