package com.carlosdiestro.jobapplicationmanager.datasource.entities

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carlosdiestro.jobapplicationmanager.R
import com.carlosdiestro.jobapplicationmanager.utils.Constants.ACCEPTED_STATUS
import com.carlosdiestro.jobapplicationmanager.utils.Constants.PENDING_STATUS
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

@Entity(tableName = "application_table")
data class JobApplication(
    var jobPosition: String,
    var company: String,
    var location: String,
    var status: Int = PENDING_STATUS,
    var applicationDate: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    fun mapStatusToString(ctx: Context, status: Int) = when(status) {
        PENDING_STATUS -> ctx.getString(R.string.pending_status)
        ACCEPTED_STATUS -> ctx.getString(R.string.accepted_status)
        else -> ctx.getString(R.string.rejected_status)
    }

    fun timeStampToDate(timeStamp: Long) = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(timeStamp)
}
