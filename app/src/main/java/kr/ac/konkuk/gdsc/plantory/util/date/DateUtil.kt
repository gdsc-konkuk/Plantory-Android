package kr.ac.konkuk.gdsc.plantory.util.date

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateUtil {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun parseDate(dateString: String): Date? {
        return try {
            dateFormat.parse(dateString)
        } catch (e: ParseException) {
            null
        }
    }

    fun calculateDaysFromDate(date: Date): Long {
        val now = Calendar.getInstance().time
        return TimeUnit.MILLISECONDS.toDays(now.time - date.time)
    }
}
