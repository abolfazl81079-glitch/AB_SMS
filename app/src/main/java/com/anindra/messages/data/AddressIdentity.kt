package com.anindra.messages.data

/** Address identity shared by threads, contact lookup and reply gates.
 *
 *  Phone numbers are normalized to E.164. Alphanumeric sender IDs (e.g.
 *  "A1 SRB", "VM-HDFCBK") are kept verbatim: reducing them to their digits
 *  collapsed "A1 SRB" to "1", which then displayed as the sender and fused
 *  unrelated senders together (issue #207). */
object AddressIdentity {

    /** E.164 canonical spelling when [address] is a phone number, else the
     *  trimmed original so alphanumeric sender IDs survive intact. */
    fun canonical(address: String, region: String): String =
        PhoneNumberUtils.toE164(address, region) ?: address.trim()

    /** True when two spellings identify the same sender. Addresses containing
     *  a letter are alphanumeric sender IDs and must match exactly
     *  (case-insensitive); digit/punctuation-only addresses keep the issue
     *  #183 digit-run comparison. */
    fun samePerson(a: String, b: String): Boolean {
        if (a == b) return true
        if (a.any { it.isLetter() } || b.any { it.isLetter() }) {
            return a.trim().equals(b.trim(), ignoreCase = true)
        }
        val da = a.filter { it.isDigit() }
        val db = b.filter { it.isDigit() }
        if (da.isEmpty() || db.isEmpty()) return false
        if (da == db) return true
        val diff = da.length - db.length
        if (diff in 1..3 && da.drop(diff) == db) return true
        val rev = db.length - da.length
        return rev in 1..3 && db.drop(rev) == da
    }

    /** True for dialable phone/short-code addresses; false for alphanumeric
     *  sender IDs, which cannot receive replies. */
    fun isReplyable(address: String): Boolean = PhoneNumberUtils.isDialableAddress(address)

    /** True when [query] can only be a phone number: at least one digit, and
     *  nothing but digits and the punctuation people type numbers with. Supports Persian & Arabic digits. */
    fun isNumberQuery(query: String): Boolean {
        val eng = JalaliCalendar.toEnglishDigits(query)
        return eng.any { it in '0'..'9' } && eng.all { it in '0'..'9' || it in "+-(). " }
    }

    /** True when [query] is a number too short to match anything yet, so the
     *  list should stay unfiltered while the user is still typing it. */
    fun tooShortToBeNumber(query: String): Boolean {
        val eng = JalaliCalendar.toEnglishDigits(query)
        return isNumberQuery(query) && eng.count { it in '0'..'9' } < MIN_SEARCH_DIGITS
    }

    /** True when [query] is a phone-number fragment that identifies [address],
     *  however each side was formatted (spaces, dashes, `+`, country code).
     *  Normalizes Persian and Arabic numerals to ensure accurate matching. */
    fun matchesNumber(address: String, query: String): Boolean {
        if (address.any { it.isLetter() }) return false
        val q = JalaliCalendar.toEnglishDigits(query).filter { it in '0'..'9' }
        if (q.length < MIN_SEARCH_DIGITS) return false
        val a = JalaliCalendar.toEnglishDigits(address).filter { it in '0'..'9' }
        if (a.isEmpty()) return false
        if (a.contains(q)) return true
        val qNoZero = q.trimStart('0')
        if (qNoZero.length >= MIN_SEARCH_DIGITS && a.contains(qNoZero)) return true
        nationalSpellings(a).forEach { 
            if (it.contains(q) || (qNoZero.length >= MIN_SEARCH_DIGITS && it.contains(qNoZero))) return true 
        }
        // Reverse direction, only once the address carries enough digits to be a
        // real number rather than something reduced below significance.
        if (a.length >= ContactLookup.MIN_MATCH_DIGITS && (q.endsWith(a) || (qNoZero.length >= ContactLookup.MIN_MATCH_DIGITS && qNoZero.endsWith(a)))) return true
        return ContactLookup.compareDigits(a, q)
    }

    /** [e164Digits] as national digits, plus that run with the trunk zero
     *  stripped: "+447700900123" → ["07700900123", "7700900123"]. Covers both
     *  the leading-zero national form (Italy, Germany) and the plain one. The
     *  region is irrelevant — the input is already E.164 — so "ZZ" is passed to
     *  keep the parse off the device region. */
    private fun nationalSpellings(e164Digits: String): List<String> {
        val national = PhoneNumberUtils.nationalDigits("+$e164Digits", "ZZ")
            ?: return emptyList()
        val withoutZero = national.trimStart('0')
        return if (withoutZero.isEmpty() || withoutZero == national) listOf(national)
        else listOf(national, withoutZero)
    }

    /** A query with fewer digits than this is treated as text, not a number, so
     *  a stray "1" does not match every conversation. */
    private const val MIN_SEARCH_DIGITS = 3
}
