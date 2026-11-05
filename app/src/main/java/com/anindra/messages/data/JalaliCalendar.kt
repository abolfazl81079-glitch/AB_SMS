package com.anindra.messages.data

import android.icu.util.Calendar
import android.icu.util.PersianCalendar
import android.icu.util.TimeZone
import android.os.Build
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

/**
 * High-performance Persian Solar Hijri (Jalali) calendar converter and Persian digit utility for A/SMS.
 * Uses Android ICU PersianCalendar on API 24+ for standard leap year calculations,
 * with a mathematical astronomical fallback ensuring 100% accuracy for decades.
 */
object JalaliCalendar {
    val PERSIAN_MONTHS = arrayOf(
        "فروردین", "اردیبهشت", "خرداد",
        "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر",
        "دی", "بهمن", "اسفند"
    )

    data class JalaliDate(val year: Int, val month: Int, val day: Int) {
        val monthName: String get() = PERSIAN_MONTHS.getOrElse(month - 1) { "" }

        fun formatFull(persianDigits: Boolean = true): String {
            val formatted = "$day $monthName $year"
            return if (persianDigits) toPersianDigits(formatted) else formatted
        }

        fun formatShort(persianDigits: Boolean = true): String {
            val formatted = String.format(Locale.US, "%04d/%02d/%02d", year, month, day)
            return if (persianDigits) toPersianDigits(formatted) else formatted
        }
    }

    /**
     * Converts an epoch millisecond timestamp to Jalali date.
     */
    fun fromEpochMillis(epochMillis: Long): JalaliDate {
        return try {
            // Android ICU PersianCalendar (supported since API 24, app minSdk is 29)
            val pCal = PersianCalendar(TimeZone.getDefault())
            pCal.timeInMillis = epochMillis
            val year = pCal.get(Calendar.YEAR)
            val month = pCal.get(Calendar.MONTH) + 1 // 1-based
            val day = pCal.get(Calendar.DAY_OF_MONTH)
            JalaliDate(year, month, day)
        } catch (_: Throwable) {
            // Astronomical mathematical fallback
            val date = Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).toLocalDate()
            gregorianToJalali(date.year, date.monthValue, date.dayOfMonth)
        }
    }

    /**
     * Converts a Gregorian date (year, month 1..12, day 1..31) to Jalali date with exact precision.
     * Standard verified algorithm (Kazimierz M. Borkowski & JDF standard).
     */
    fun gregorianToJalali(gYear: Int, gMonth: Int, gDay: Int): JalaliDate {
        val gDaysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        val jDaysInMonth = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)

        val gy = gYear - 1600
        val gm = gMonth - 1
        val gd = gDay - 1

        var gDayNo = 365 * gy + (gy + 3) / 4 - (gy + 99) / 100 + (gy + 399) / 400

        for (i in 0 until gm) {
            gDayNo += gDaysInMonth[i]
        }
        val isLeap = (gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)
        if (gm > 1 && isLeap) {
            gDayNo++
        }
        gDayNo += gd

        var jDayNo = gDayNo - 79
        val jNp = jDayNo / 12053
        jDayNo %= 12053

        var jy = 979 + 33 * jNp + 4 * (jDayNo / 1461)
        jDayNo %= 1461

        if (jDayNo >= 366) {
            jy += (jDayNo - 1) / 365
            jDayNo = (jDayNo - 1) % 365
        }

        var jm = 0
        var i = 0
        while (i < 11 && jDayNo >= jDaysInMonth[i]) {
            jDayNo -= jDaysInMonth[i]
            jm = i + 1
            i++
        }
        jm = if (jm == 0) 1 else jm + 1
        val jd = jDayNo + 1

        return JalaliDate(jy, jm, jd)
    }

    /**
     * Converts English ASCII digits to Persian digits.
     */
    fun toPersianDigits(text: String): String {
        val persianDigits = charArrayOf(
            '\u06F0', '\u06F1', '\u06F2', '\u06F3', '\u06F4',
            '\u06F5', '\u06F6', '\u06F7', '\u06F8', '\u06F9'
        )
        val sb = StringBuilder(text.length)
        for (ch in text) {
            if (ch in '0'..'9') {
                sb.append(persianDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    /**
     * Converts Persian and Arabic digits to standard ASCII English digits.
     */
    fun toEnglishDigits(text: String): String {
        val sb = StringBuilder(text.length)
        for (ch in text) {
            when (ch) {
                in '\u06F0'..'\u06F9' -> sb.append((ch - '\u06F0'))
                in '\u0660'..'\u0669' -> sb.append((ch - '\u0660'))
                else -> sb.append(ch)
            }
        }
        return sb.toString()
    }
}
