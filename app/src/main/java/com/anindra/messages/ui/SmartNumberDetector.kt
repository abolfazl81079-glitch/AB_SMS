package com.anindra.messages.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anindra.messages.data.JalaliCalendar

/**
 * Intelligent number sequence detector, normalizer, and smart actions for AB/SMS.
 */
object SmartNumberDetector {

    enum class NumberType(val label: String) {
        BANK_CARD("Bank Card"),
        IBAN("Sheba / IBAN"),
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
     * Scans message body and detects all numeric sequences and entities.
     */
    fun detectNumbers(text: String): List<DetectedNumber> {
        if (text.isBlank()) return emptyList()

        val results = mutableListOf<DetectedNumber>()
        // Normalize text digits for regex scanning
        val normalizedBody = JalaliCalendar.toEnglishDigits(text)

        // 1. Bank Card: 16 digits, with optional spaces or dashes
        val cardRegex = Regex("""(?<!\d)(\d{4}[ -]?\d{4}[ -]?\d{4}[ -]?\d{4})(?!\d)""")
        for (m in cardRegex.findAll(normalizedBody)) {
            val original = text.substring(m.range)
            val digitsOnly = m.groupValues[1].filter { it.isDigit() }
            if (digitsOnly.length == 16) {
                val luhn = isValidLuhn(digitsOnly)
                results.add(
                    DetectedNumber(
                        originalText = original,
                        normalizedText = digitsOnly,
                        type = NumberType.BANK_CARD,
                        isValidLuhn = luhn,
                        range = m.range
                    )
                )
            }
        }

        // 2. Sheba / IBAN (IR followed by 24 digits)
        val ibanRegex = Regex("""(?i)\b(IR[ -]?(?:\d[ -]?){24})(?!\d)""")
        for (m in ibanRegex.findAll(normalizedBody)) {
            val original = text.substring(m.range)
            val cleanIban = "IR" + m.groupValues[1].filter { it.isDigit() }
            results.add(
                DetectedNumber(
                    originalText = original,
                    normalizedText = cleanIban,
                    type = NumberType.IBAN,
                    range = m.range
                )
            )
        }

        // 3. Mobile phone numbers:
        // Iranian: 09..., +989..., or international: +\d{9,14}
        val phoneRegex = Regex("""(?<!\d)(\+?\d{1,4}[ -]?)?(0?9\d{2}[ -]?\d{3}[ -]?\d{4})(?!\d)""")
        for (m in phoneRegex.findAll(normalizedBody)) {
            val range = m.range
            if (results.none { it.range.first <= range.first && it.range.last >= range.last }) {
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

        // 4. Landlines (e.g. 021..., 031..., etc.)
        val landlineRegex = Regex("""(?<!\d)(0\d{2,3}[ -]?\d{7,8})(?!\d)""")
        for (m in landlineRegex.findAll(normalizedBody)) {
            val range = m.range
            if (results.none { it.range.first <= range.first && it.range.last >= range.last }) {
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

        // 5. Verification Codes / OTP (4 to 8 digits near code keywords)
        val otpRegex = Regex("""(?i)(?:code|otp|رمز|کد|تایید|verification|pin)[:\s-]*(\d{4,8})(?!\d)""")
        for (m in otpRegex.findAll(normalizedBody)) {
            val group = m.groups[1] ?: continue
            val range = group.range
            if (results.none { it.range.first <= range.first && it.range.last >= range.last }) {
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

        // 6. Postal code / National ID / Tracking codes (10 to 24 digits not captured above)
        val trackingRegex = Regex("""(?<!\d)(\d{10,24})(?!\d)""")
        for (m in trackingRegex.findAll(normalizedBody)) {
            val range = m.range
            if (results.none { !(range.last < it.range.first || range.first > it.range.last) }) {
                val original = text.substring(range)
                results.add(
                    DetectedNumber(
                        originalText = original,
                        normalizedText = normalize(original),
                        type = NumberType.TRACKING,
                        range = range
                    )
                )
            }
        }

        // 7. General Numeric Sequences: Detect ANY 4 or more digits (4-digit, 10-digit, any length)
        val generalNumRegex = Regex("""(?<!\d)(\d(?:[ -]?\d){3,29})(?!\d)""")
        for (m in generalNumRegex.findAll(normalizedBody)) {
            val range = m.range
            val overlaps = results.any { existing ->
                !(range.last < existing.range.first || range.first > existing.range.last)
            }
            if (!overlaps) {
                val original = text.substring(range)
                val clean = normalize(original)
                if (clean.length >= 4) {
                    val inferredType = when {
                        clean.length in 4..8 && (text.contains("کد", ignoreCase = true) || text.contains("رمز", ignoreCase = true) || text.contains("otp", ignoreCase = true)) ->
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

        return results.sortedBy { it.range.first }
    }
}

/**
 * Cyberpunk styled Smart Action Pills row for detected numbers under a message bubble.
 */
@Composable
fun SmartNumberActionRow(
    detectedNumbers: List<SmartNumberDetector.DetectedNumber>,
    modifier: Modifier = Modifier
) {
    if (detectedNumbers.isEmpty()) return
    val context = LocalContext.current
    val uniqueNumbers = remember(detectedNumbers) { detectedNumbers.distinctBy { it.normalizedText } }

    Row(
        modifier = modifier
            .padding(top = 4.dp, bottom = 2.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (item in uniqueNumbers) {
            val (icon, badgeColor) = when (item.type) {
                SmartNumberDetector.NumberType.BANK_CARD ->
                    Icons.Rounded.CreditCard to (if (item.isValidLuhn) Color(0xFFFFCC00) else Color(0xFFFFB4AB))
                SmartNumberDetector.NumberType.IBAN ->
                    Icons.Rounded.Numbers to Color(0xFF00E5FF)
                SmartNumberDetector.NumberType.MOBILE, SmartNumberDetector.NumberType.LANDLINE ->
                    Icons.Rounded.Call to Color(0xFF00E5FF)
                SmartNumberDetector.NumberType.OTP ->
                    Icons.Rounded.Security to Color(0xFFFFCC00)
                SmartNumberDetector.NumberType.TRACKING, SmartNumberDetector.NumberType.GENERAL ->
                    Icons.Rounded.Numbers to Color(0xFFFFCC00)
            }

            // Glass action chip with cyber border
            Surface(
                color = Color(0x33101827),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.7f)),
                modifier = Modifier.clickable {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText(item.type.label, item.normalizedText))
                    Toast.makeText(context, "کپی شد: ${item.normalizedText}", Toast.LENGTH_SHORT).show()
                }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = badgeColor,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = item.normalizedText,
                        color = Color(0xFFF0F6FC),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (item.type == SmartNumberDetector.NumberType.BANK_CARD) {
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = if (item.isValidLuhn) "✓" else "✕",
                            color = badgeColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (item.type == SmartNumberDetector.NumberType.MOBILE || item.type == SmartNumberDetector.NumberType.LANDLINE) {
                        Spacer(Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clickable {
                                    try {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.normalizedText}"))
                                        context.startActivity(intent)
                                    } catch (_: Exception) {}
                                }
                        ) {
                            Text(
                                text = "CALL",
                                color = Color(0xFF00E5FF),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
