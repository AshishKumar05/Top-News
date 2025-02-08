package com.thetopheadlines.topnews.data.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    const val BASE_URL = "https://newsapi.org/v2/"
    const val API_KEY = "cdd9560dd60b49f09817ff23a6b56db1"

    fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM. d, yyyy", Locale.getDefault())

        return try {
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            "Unknown Date"
        }
    }
}