package com.carlosdiestro.jobapplicationmanager.datasource.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.carlosdiestro.jobapplicationmanager.datasource.entities.JobApplication

@Dao
interface JobApplicationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobApplication(jobApplication: JobApplication)

    @Update
    suspend fun updateJobApplication(jobApplication: JobApplication)

    @Query("DELETE FROM application_table WHERE status = 1 AND status = 2")
    suspend fun clearNonPendingJobApplications()

    @Query("SELECT * FROM application_table ORDER BY id DESC")
    fun getAll() : LiveData<List<JobApplication>>

    @Query("SELECT * FROM application_table WHERE status = 0 ORDER BY id DESC")
    fun getPendingJobApplications() : LiveData<List<JobApplication>>

    @Query("SELECT * FROM application_table WHERE status = 1 ORDER BY id DESC")
    fun getAcceptedJobApplications() : LiveData<List<JobApplication>>

    @Query("SELECT * FROM application_table WHERE status = 2 ORDER BY id DESC")
    fun getRejectedJobApplications() : LiveData<List<JobApplication>>
}