package com.carlosdiestro.jobapplicationmanager.datasource.entities

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carlosdiestro.jobapplicationmanager.R

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

    fun mapStatusToString(ctx: Context, status: Int) = when(status) {
        0 -> ctx.getString(R.string.pending_status)
        1 -> ctx.getString(R.string.accepted_status)
        else -> ctx.getString(R.string.rejected_status)
    }
}
