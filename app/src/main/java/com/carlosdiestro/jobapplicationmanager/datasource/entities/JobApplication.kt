package com.carlosdiestro.jobapplicationmanager.datasource.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "application_table")
data class JobApplication(
    var jobPosition: String,
    var company: String,
    var location: String,
    var status: Int = 0,
    var applicationDate: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
