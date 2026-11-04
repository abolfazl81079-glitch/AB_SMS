package com.anindra.messages.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import com.anindra.messages.data.JalaliCalendar

/**
 * Intelligent Code, Number, USSD, and Entity Detector for AB/SMS.
 * Detects USSD codes, discount/voucher codes, amounts with commas, dates,
 * bank cards, IBANs, phone numbers, and arbitrary numeric runs.
 */
object SmartNumberDetector {

    enum class NumberType(val label: String) {
        USSD("USSD Code"),
        URL("Link"),
        BANK_CARD("Bank Card"),
        IBAN("Sheba / IBAN"),
        AMOUNT("Amount"),
        DATE("Date"),
        PROMO_CODE("Code"),
        MOBILE("Mobile Phone"),
        LANDLINE("Phone"),
        OTP("Verification Code"),
        TRACKING("Tracking / ID"),
        GENERAL("Number")
    }

    data class DetectedNumber(
        val originalText: String,
        val normalizedText: String,
        val type: NumberType,
        val isValidLuhn: Boolean = false,
        val range: IntRange
    )

    /**
     * Local Luhn algorithm for bank card validation.
     */
    fun isValidLuhn(numberStr: String): Boolean {
        val digits = numberStr.filter { it.isDigit() }
        if (digits.length != 16) return false
        var sum = 0
        var alternate = false
        for (i in digits.length - 1 downTo 0) {
            var n = digits[i] - '0'
            if (alternate) {
                n *= 2
                if (n > 9) n = (n % 10) + 1
            }
            sum += n
            alternate = !alternate
        }
        return (sum % 10 == 0)
    }

    /**
     * Normalizes digits: converts Persian/Arabic numerals to ASCII 0-9,
     * strips unnecessary hyphens and spaces while keeping + or IR prefix.
     */
    fun normalize(text: String, isPhone: Boolean = false, isIban: Boolean = false): String {
        val ascii = JalaliCalendar.toEnglishDigits(text)
        val clean = StringBuilder()
        for (i in ascii.indices) {
            val c = ascii[i]
            if (c.isDigit()) {
                clean.append(c)
            } else if (isPhone && c == '+' && clean.isEmpty()) {
                clean.append('+')
            } else if (isIban && (c == 'I' || c == 'R' || c == 'i' || c == 'r') && clean.length < 2) {
                clean.append(c.uppercaseChar())
            }
        }
        return clean.toString()
    }

    /**
     * Copies detected entity to clipboard and displays appropriate Toast.
     */
    fun copyToClipboard(context: Context, item: DetectedNumber) {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(item.type.label, item.normalizedText))
            val msg = when (item.type) {
                NumberType.USSD -> "کد دستوری کپی شد: ${item.normalizedText}"
                NumberType.PROMO_CODE -> "کد تخفیف کپی شد: ${item.normalizedText}"
                NumberType.BANK_CARD -> "شماره کارت کپی شد: ${item.normalizedText}"
                NumberType.IBAN -> "شماره شبا کپی شد: ${item.normalizedText}"
                NumberType.AMOUNT -> "مبلغ کپی شد: ${item.normalizedText}"
                NumberType.DATE -> "تاریخ کپی شد: ${item.normalizedText}"
                NumberType.MOBILE, NumberType.LANDLINE -> "شماره کپی شد: ${item.normalizedText}"
                else -> "کپی شد: ${item.normalizedText}"
            }
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {}
    }

    /**
     * Scans message body and detects all numeric sequences, codes, USSD, amounts, and entities.
     */
    fun detectNumbers(text: String): List<DetectedNumber> {
        if (text.isBlank()) return emptyList()

        val results = mutableListOf<DetectedNumber>()
        // Normalize text digits for accurate regex scanning while keeping character offsets identical
        val normalizedBody = JalaliCalendar.toEnglishDigits(text)

        fun overlaps(range: IntRange): Boolean {
            return results.any { existing ->
                !(range.last < existing.range.first || range.first > existing.range.last)
            }
        }

        // 1. USSD Codes (e.g. *4444*2*3#, *100*1#, *555#)
        val ussdRegex = Regex("""(?<![0-9*#])(\*[\d*#]{2,25}#)(?![0-9*#])""")
        for (m in ussdRegex.findAll(normalizedBody)) {
            val range = m.range
            if (!overlaps(range)) {
                val original = text.substring(range)
                val clean = JalaliCalendar.toEnglishDigits(original).trim()
                results.add(
                    DetectedNumber(
                        originalText = original,
                        normalizedText = clean,
                        type = NumberType.USSD,
                        range = range
                    )
                )
            }
        }

        // 2. Formatted Currency / Amounts with commas or dots (e.g. 3,750,000, 5,200,675, 2,050,000)
        val amountRegex = Regex("""(?<!\d)(\d{1,3}(?:[,،]\d{3})+(?:\.\d+)?)(?!\d)""")
        for (m in amountRegex.findAll(normalizedBody)) {
            val range = m.range
            if (!overlaps(range)) {
                val original = text.substring(range)
                val digitsOnly = original.filter { it.isDigit() || it in '۰'..'۹' || it in '٠'..'٩' }
                val clean = JalaliCalendar.toEnglishDigits(digitsOnly)
                results.add(
                    DetectedNumber(
                        originalText = original,
                        normalizedText = clean,
                        type = NumberType.AMOUNT,
                        range = range
                    )
                )
            }
        }

        // 3. Dates with dots, slashes, or dashes (e.g. 1405.08.12, 1403/11/04, 2026-11-04)
        val dateRegex = Regex("""(?<!\d)(\d{2,4}[./-]\d{1,2}[./-]\d{1,2})(?!\d)""")
        for (m in dateRegex.findAll(normalizedBody)) {
            val range = m.range
            if (!overlaps(range)) {
                val original = text.substring(range)
                val clean = JalaliCalendar.toEnglishDigits(original)
                results.add(
                    DetectedNumber(
                        originalText = original,
                        normalizedText = clean,
                        type = NumberType.DATE,
                        range = range
                    )
                )
            }
        }

        // 4. Bank Card: 16 digits, with optional spaces or dashes
        val cardRegex = Regex("""(?<!\d)(\d{4}[ -]?\d{4}[ -]?\d{4}[ -]?\d{4})(?!\d)""")
        for (m in cardRegex.findAll(normalizedBody)) {
            val range = m.range
            if (!overlaps(range)) {
                val original = text.substring(range)
                val digitsOnly = m.groupValues[1].filter { it.isDigit() }
                if (digitsOnly.length == 16) {
                    val luhn = isValidLuhn(digitsOnly)
                    results.add(
                        DetectedNumber(
                            originalText = original,
                            normalizedText = digitsOnly,
                            type = NumberType.BANK_CARD,
                            isValidLuhn = luhn,
                            range = range
                        )
                    )
                }
            }
        }

        // 5. Sheba / IBAN (IR followed by 24 digits)
        val ibanRegex = Regex("""(?i)\b(IR[ -]?(?:\d[ -]?){24})(?!\d)""")
        for (m in ibanRegex.findAll(normalizedBody)) {
            val range = m.range
            if (!overlaps(range)) {
                val original = text.substring(range)
                val cleanIban = "IR" + m.groupValues[1].filter { it.isDigit() }
                results.add(
                    DetectedNumber(
                        originalText = original,
                        normalizedText = cleanIban,
                        type = NumberType.IBAN,
                        range = range
                    )
                )
            }
        }

        // 6. Alphanumeric Promo / Discount / Voucher / OTP Codes
        // A. Keyword-preceded codes (e.g. "کد: LDMM7X", "کد: X4W2VT", "کد تخفیف: OKALA50")
        val promoKeywordRegex = Regex("""(?i)(?:کد|رمز|code|coupon|voucher|pin|تخفیف|شناسه)[:\s-]*([A-Za-z0-9]{3,16})(?![A-Za-z0-9])""")
        for (m in promoKeywordRegex.findAll(text)) {
            val group = m.groups[1] ?: continue
            val range = group.range
            if (!overlaps(range)) {
                val code = text.substring(range)
                results.add(
                    DetectedNumber(
                        originalText = code,
                        normalizedText = code.trim(),
                        type = NumberType.PROMO_CODE,
                        range = range
                    )
                )
            }
        }

        // B. Standalone mixed alphanumeric codes containing letters AND digits (e.g. LDMM7X, X4W2VT)
        val standaloneAlphaNumRegex = Regex("""(?<![A-Za-z0-9])([A-Za-z0-9]{4,14})(?![A-Za-z0-9])""")
        for (m in standaloneAlphaNumRegex.findAll(text)) {
            val token = m.groupValues[1]
            // Must contain at least one letter and at least one digit
            val hasLetter = token.any { it.isLetter() }
            val hasDigit = token.any { it.isDigit() }
            if (hasLetter && hasDigit && !overlaps(m.range)) {
                results.add(
                    DetectedNumber(
                        originalText = token,
                        normalizedText = token.trim(),
                        type = NumberType.PROMO_CODE,
                        range = m.range
                    )
                )
            }
        }

        // 7. Unsubscribe Codes (e.g. "لغو11", "لغو 11")
        val unsubRegex = Regex("""(?i)(?:لغو|laghv|off)[:\s-]*([0-9\u06F0-\u06F9]{1,6})""")
        for (m in unsubRegex.findAll(text)) {
            val group = m.groups[1] ?: continue
            val range = group.range
            if (!overlaps(range)) {
                val original = text.substring(range)
                results.add(
                    DetectedNumber(
                        originalText = original,
                        normalizedText = normalize(original),
                        type = NumberType.OTP,
                        range = range
                    )
                )
            }
        }

        // 8. Mobile & Landline Phone Numbers
        val phoneRegex = Regex("""(?<!\d)(\+?\d{1,4}[ -]?)?(0?9\d{2}[ -]?\d{3}[ -]?\d{4})(?!\d)""")
        for (m in phoneRegex.findAll(normalizedBody)) {
            val range = m.range
            if (!overlaps(range)) {
                val original = text.substring(range)
                val normalized = normalize(original, isPhone = true)
                if (normalized.length in 10..15) {
                    results.add(
                        DetectedNumber(
                            originalText = original,
                            normalizedText = normalized,
                            type = NumberType.MOBILE,
                            range = range
                        )
                    )
                }
            }
        }

        val landlineRegex = Regex("""(?<!\d)(0\d{2,3}[ -]?\d{7,9})(?!\d)""")
        for (m in landlineRegex.findAll(normalizedBody)) {
            val range = m.range
            if (!overlaps(range)) {
                val original = text.substring(range)
                val normalized = normalize(original, isPhone = true)
                results.add(
                    DetectedNumber(
                        originalText = original,
                        normalizedText = normalized,
                        type = NumberType.LANDLINE,
                        range = range
                    )
                )
            }
        }

        // 9. All Other Numeric Runs (2 or more digits: e.g. ۷۳۸, 1405, 50000, 80, 400, etc.)
        val generalNumRegex = Regex("""(?<!\d)(\d(?:[ -]?\d){1,29})(?!\d)""")
        for (m in generalNumRegex.findAll(normalizedBody)) {
            val range = m.range
            if (!overlaps(range)) {
                val original = text.substring(range)
                val clean = normalize(original)
                if (clean.length >= 2) {
                    val inferredType = when {
                        clean.length in 4..6 && (text.contains("کد", ignoreCase = true) || text.contains("رمز", ignoreCase = true) || text.contains("otp", ignoreCase = true)) ->
                            NumberType.OTP
                        clean.length in 10..12 && clean.startsWith("09") -> NumberType.MOBILE
                        clean.length in 10..12 && clean.startsWith("0") -> NumberType.LANDLINE
                        clean.length == 10 -> NumberType.TRACKING
                        clean.length == 16 -> NumberType.BANK_CARD
                        else -> NumberType.GENERAL
                    }
                    val luhn = if (clean.length == 16) isValidLuhn(clean) else false
                    results.add(
                        DetectedNumber(
                            originalText = original,
                            normalizedText = clean,
                            type = inferredType,
                            isValidLuhn = luhn,
                            range = range
                        )
                    )
                }
            }
        }

        // Sort all detected items by their exact start offset in the message text
        return results.sortedBy { it.range.first }
    }
}
