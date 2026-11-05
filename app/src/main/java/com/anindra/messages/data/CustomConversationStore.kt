package com.anindra.messages.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages per-conversation customization settings:
 * custom cell border color, preview lines, border opacity, and border width.
 * Overrides global settings when set; falls back to global settings when default (-1).
 */
object CustomConversationStore {
    private const val PREFS_NAME = "custom_conversation_prefs"
    private const val KEY_COLOR_PREFIX = "cell_color_"
    private const val KEY_LINES_PREFIX = "preview_lines_"
    private const val KEY_OPACITY_PREFIX = "border_opacity_"
    private const val KEY_WIDTH_PREFIX = "border_width_"

    private val _revision = MutableStateFlow(0)
    val revision: StateFlow<Int> = _revision.asStateFlow()

    fun getCellColor(context: Context, key: String): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_COLOR_PREFIX + key, null)
    }

    fun setCellColor(context: Context, key: String, color: String?) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (color == null || color == "default") {
            prefs.edit().remove(KEY_COLOR_PREFIX + key).apply()
        } else {
            prefs.edit().putString(KEY_COLOR_PREFIX + key, color).apply()
        }
        _revision.value++
    }

    fun getPreviewLines(context: Context, key: String): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_LINES_PREFIX + key, 0)
    }

    fun setPreviewLines(context: Context, key: String, lines: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (lines <= 0) {
            prefs.edit().remove(KEY_LINES_PREFIX + key).apply()
        } else {
            prefs.edit().putInt(KEY_LINES_PREFIX + key, lines).apply()
        }
        _revision.value++
    }

    fun getBorderOpacity(context: Context, key: String): Float {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getFloat(KEY_OPACITY_PREFIX + key, -1f)
    }

    fun setBorderOpacity(context: Context, key: String, opacity: Float) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (opacity < 0f) {
            prefs.edit().remove(KEY_OPACITY_PREFIX + key).apply()
        } else {
            prefs.edit().putFloat(KEY_OPACITY_PREFIX + key, opacity).apply()
        }
        _revision.value++
    }

    fun getBorderWidth(context: Context, key: String): Float {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getFloat(KEY_WIDTH_PREFIX + key, -1f)
    }

    fun setBorderWidth(context: Context, key: String, width: Float) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (width < 0f) {
            prefs.edit().remove(KEY_WIDTH_PREFIX + key).apply()
        } else {
            prefs.edit().putFloat(KEY_WIDTH_PREFIX + key, width).apply()
        }
        _revision.value++
    }
}
