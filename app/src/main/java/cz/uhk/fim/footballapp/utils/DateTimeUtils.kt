package cz.uhk.fim.footballapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@RequiresApi(Build.VERSION_CODES.O)
fun formatToLocalTime(isoString: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

        val dateTime = OffsetDateTime.parse(isoString, inputFormatter)
        dateTime.format(outputFormatter)
    } catch (e: Exception) {
        "Invalid date"
    }
}