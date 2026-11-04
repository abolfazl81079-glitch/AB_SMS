package com.anindra.messages.data

import java.time.Instant
import java.time.ZoneId

/**
 * High-performance Persian Solar Hijri (Jalali) calendar converter and Persian digit utility for AB/SMS.
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
            val formatted = String.format("%04d/%02d/%02d", year, month, day)
            return if (persianDigits) toPersianDigits(formatted) else formatted
        }
    }

    /**
     * Converts a Gregorian date to Jalali date with precision.
     */
    fun gregorianToJalali(gy: Int, gm: Int, gd: Int): JalaliDate {
        val gDaysInMonth = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        val gy2 = if (gm > 2) gy + 1 else gy
        var gDayNo = 365 * gy + (gy2 + 3) / 4 - (gy2 + 99) / 100 + (gy2 + 399) / 400 - 80 + gd + gDaysInMonth[gm - 1]
        val gyLeap = (gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)
        if (gm > 2 && gyLeap) gDayNo++

        var jDayNo = gDayNo - 79
        val jNp = jDayNo / 12053
        jDayNo %= 12053

        var jy = 979 + 33 * jNp + 4 * (jDayNo / 1461)
        jDayNo %= 1461

        if (jDayNo >= 366) {
            jy += (jDayNo - 1) / 365
            jDayNo = (jDayNo - 1) % 365
        }

        val jm: Int
        val jd: Int
        if (jDayNo < 186) {
            jm = 1 + (jDayNo / 31)
            jd = 1 + (jDayNo % 31)
        } else {
            jm = 7 + ((jDayNo - 186) / 30)
            jd = 1 + ((jDayNo - 186) % 30)
        }

        return JalaliDate(jy, jm, jd)
    }

    fun fromEpochMillis(epochMillis: Long): JalaliDate {
        val date = Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).toLocalDate()
        return gregorianToJalali(date.year, date.monthValue, date.dayOfMonth)
    }

    /**
     * Converts English ASCII digits to Persian digits.
     */
    fun toPersianDigits(text: String): String {
        val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
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
                in '۰'..'۹' -> sb.append((ch - '۰'))
                in '٠'..'٩' -> sb.append((ch - '٠'))
                else -> sb.append(ch)
            }
        }
        return sb.toString()
    }
}
