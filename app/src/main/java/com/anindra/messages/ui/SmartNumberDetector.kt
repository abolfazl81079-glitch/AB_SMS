package com.anindra.messages.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    ) {
        /**
         * Checks if the entity contains separators (hyphens, dots, commas, slashes, underscores, colons, or spaces).
         * USSD codes and URLs are exempt as their special characters are structural.
         */
        fun hasSeparators(): Boolean {
            if (type == NumberType.USSD || type == NumberType.URL) return false
            val separators = setOf('-', '–', '—', '.', ',', '،', '٫', '/', '\\', '_', ':', ' ', '(', ')', '\u066B', '\u066C', '\u060C')
            return originalText.any { it in separators }
        }

        /**
         * Strips all separators (hyphens, dots, commas, slashes, underscores, colons, spaces)
         * while keeping pure digits/alphanumeric characters, and normalizes Persian numerals to standard digits.
         */
        fun cleanWithoutSeparators(): String {
            if (type == NumberType.USSD || type == NumberType.URL) return originalText
            val separators = setOf('-', '–', '—', '.', ',', '،', '٫', '/', '\\', '_', ':', ' ', '(', ')', '\u066B', '\u066C', '\u060C')
            val filtered = originalText.filter { it !in separators }
            return JalaliCalendar.toEnglishDigits(filtered).trim()
        }
    }

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
     * Guaranteed multi-pass detection with hyphen, dot, comma, and separator preservation.
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
        val amountRegex = Regex("""(?<!\d)(\d{1,3}(?:[,،٫.]\d{3})+(?:\.\d+)?)(?!\d)""")
        for (m in amountRegex.findAll(normalizedBody)) {
            val range = m.range
            if (!overlaps(range)) {
                val original = text.substring(range)
                val digitsOnly = original.filter { it.isDigit() || it in '\u06F0'..'\u06F9' || it in '\u0660'..'\u0669' }
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

        // 3. Dates with dots, slashes, or dashes (e.g. 1405.08.12, 1405.08.13, 1403/11/04, 2026-11-04)
        val dateRegex = Regex("""(?<!\d)(\d{2,4}[./\-\u066B\u060D]\d{1,2}[./\-\u066B\u060D]\d{1,2})(?!\d)""")
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

        // 6. Alphanumeric Promo / Discount / Voucher / Tracking Codes
        // A. Keyword-preceded codes (e.g. "کد: LDMM7X", "کد: X4W2VT", "کد تخفیف: OKALA50", "کد درگاه دیجی‌پی: DEZTAV", "کد درگاه دیجی‌پی: DEBMET")
        val promoKeywordRegex = Regex("""(?i)(?:کد|رمز|code|coupon|voucher|pin|تخفیف|شناسه|پیگیری|کدتخفیف|درگاه|هدیه|معرف|شارژ)[^:\n\r]{0,35}[:\s-]+\s*([A-Za-z0-9_-]{3,24})(?![A-Za-z0-9_-])""")
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

        // B. Standalone mixed alphanumeric codes containing letters AND digits (e.g. LDMM7X, X4W2VT, SNAP-50)
        val standaloneAlphaNumRegex = Regex("""(?<![A-Za-z0-9_-])([A-Za-z0-9]+(?:[-_][A-Za-z0-9]+)*)(?![A-Za-z0-9_-])""")
        for (m in standaloneAlphaNumRegex.findAll(text)) {
            val token = m.groupValues[1]
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

        // C. Standalone uppercase promo words (4 to 16 chars: e.g. DEZTAV, DEBMET, YALDA, NOROOZ)
        val uppercasePromoRegex = Regex("""(?<![A-Za-z0-9])([A-Z]{4,16})(?![A-Za-z0-9])""")
        val excludedEnglishWords = setOf(
            "MESSAGE", "MESSAGES", "DELIVERED", "RECEIVED", "FAILED", "ONLINE", "STARK",
            "CYBER", "SYSTEM", "ERROR", "HTTPS", "HTTP", "FALSE", "TRUE", "ANDROID",
            "NOTIFICATION", "INFO", "ALERT", "CALL", "CHAT", "DETAILS", "SIM", "CANCEL"
        )
        for (m in uppercasePromoRegex.findAll(text)) {
            val token = m.groupValues[1]
            if (token !in excludedEnglishWords && !overlaps(m.range)) {
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

        // 8. Mobile & Landline Phone Numbers (with hyphens or spaces, e.g. 0912-345-6789, 0999 998 7641, 050 0030 1536)
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

        // 9. All Multi-Segment Numbers with Separators (Hyphens, Dots, Slashes, Underscores, Colons, Spaces)
        // Completely highlights full expressions like 1405.08.13, 123-456-789, 12-34-56, 18:10, etc.
        val separatedNumRegex = Regex("""(?<!\d)(\d+(?:[-–—./\\_،٫: ]\d+)+)(?!\d)""")
        for (m in separatedNumRegex.findAll(normalizedBody)) {
            val range = m.range
            if (!overlaps(range)) {
                val original = text.substring(range)
                val clean = normalize(original)
                results.add(
                    DetectedNumber(
                        originalText = original,
                        normalizedText = clean,
                        type = NumberType.GENERAL,
                        range = range
                    )
                )
            }
        }

        // 10. All Other Numeric Runs (2 or more digits: e.g. 738, 1405, 50000, 80, 400, 0116466042000, etc.)
        val pureNumRegex = Regex("""(?<!\d)(\d{2,30})(?!\d)""")
        for (m in pureNumRegex.findAll(normalizedBody)) {
            val range = m.range
            if (!overlaps(range)) {
                val original = text.substring(range)
                val clean = normalize(original)
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

        // Sort all detected items by their exact start offset in the message text
        return results.sortedBy { it.range.first }
    }
}

/**
 * Cyber HUD Glass Dialog asking whether to strip hyphens, dots, spaces, and symbols
 * or copy the exact original text to clipboard.
 * Fully synchronized with dynamic theme colors.
 */
@Composable
fun CopySeparatorDialog(
    item: SmartNumberDetector.DetectedNumber,
    onDismiss: () -> Unit,
    onCopy: (textToCopy: String, isCleaned: Boolean) -> Unit
) {
    val cleanText = item.cleanWithoutSeparators()
    val originalText = item.originalText
    val cs = MaterialTheme.colorScheme

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = cs.surfaceContainerHigh.copy(alpha = 0.95f),
        tonalElevation = 6.dp,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.padding(16.dp),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ContentCopy,
                    contentDescription = null,
                    tint = cs.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "کپی به کلیپ‌بورد",
                    color = cs.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "آیا مایلید علامت‌ها، فاصله‌ها و خطوط تیره از متن حذف شوند؟",
                    color = cs.onSurface,
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp
                )

                // Option 1 Preview Card: Cleaned without separators (Directly clickable)
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = cs.primary.copy(alpha = 0.12f)),
                    border = BorderStroke(0.8.dp, cs.primary.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCopy(cleanText, true) }
                ) {
                    Column(Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "حالت خالص (بدون علامت و خط فاصله):",
                                color = cs.primary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "لمس برای کپی",
                                color = cs.primary.copy(alpha = 0.8f),
                                fontSize = 10.sp
                            )
                        }
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = cleanText,
                            color = cs.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Option 2 Preview Card: Original with separators (Directly clickable)
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant.copy(alpha = 0.5f)),
                    border = BorderStroke(0.8.dp, cs.secondary.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCopy(originalText, false) }
                ) {
                    Column(Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "حالت اصلی (با علامت و خط فاصله):",
                                color = cs.secondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "لمس برای کپی",
                                color = cs.secondary.copy(alpha = 0.8f),
                                fontSize = 10.sp
                            )
                        }
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = originalText,
                            color = cs.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onCopy(cleanText, true) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.primary,
                            contentColor = cs.onPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("کپی بدون علامت", fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
                    }

                    OutlinedButton(
                        onClick = { onCopy(originalText, false) },
                        border = BorderStroke(0.8.dp, cs.secondary),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = cs.secondary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("کپی با علامت", fontWeight = FontWeight.Medium, fontSize = 12.sp, maxLines = 1)
                    }
                }

                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(contentColor = cs.onSurfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("انصراف", fontSize = 13.sp)
                }
            }
        },
        dismissButton = null
    )
}
