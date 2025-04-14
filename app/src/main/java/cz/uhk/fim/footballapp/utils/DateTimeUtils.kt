package cz.uhk.fim.footballapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone



fun formatToLocalTime(isoString: String): String {
    return try {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val outputFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

        val date = inputFormatter.parse(isoString)
        outputFormatter.format(date ?: Date())
    } catch (e: Exception) {
        "Invalid date"
    }
}

fun formatToLocalDateTime(isoString: String): String {
    return try {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val outputFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

        val date = inputFormatter.parse(isoString)
        outputFormatter.format(date ?: Date())
    } catch (e: Exception) {
        "Invalid date"
    }
}

fun formatToDate(isoString: String): String {
    return try {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val outputFormatter = SimpleDateFormat("dd.MM.", Locale.getDefault())

        val date = inputFormatter.parse(isoString)
        outputFormatter.format(date ?: Date())
    } catch (e: Exception) {
        "Invalid date"
    }
}
