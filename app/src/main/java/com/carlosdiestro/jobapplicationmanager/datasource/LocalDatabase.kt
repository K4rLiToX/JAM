package com.carlosdiestro.jobapplicationmanager.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carlosdiestro.jobapplicationmanager.datasource.dao.JobApplicationDAO
import com.carlosdiestro.jobapplicationmanager.datasource.entities.JobApplication

@Database(
    entities = [JobApplication::class],
    version = 2,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun getJobApplicationDAO(): JobApplicationDAO
}