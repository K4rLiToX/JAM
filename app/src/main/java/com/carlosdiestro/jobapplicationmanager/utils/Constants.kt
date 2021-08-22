package com.carlosdiestro.jobapplicationmanager.utils

import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

object Constants {

    const val LOCALDATABASE_NAME = "localdatase"
    const val PENDING_STATUS = 0
    const val ACCEPTED_STATUS = 1
    const val REJECTED_STATUS = 2
    val GSON = Gson()

    fun timeStampToDate(timeStamp: Long): String =
        SimpleDateFormat("dd/MM/yyyy", Locale.US).format(timeStamp)
}