package com.anindra.messages.data

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log

/**
 * Manages runtime dynamic switching of Android app launcher icons.
 * Controls enable/disable states of manifest activity-aliases.
 */
object LauncherIconManager {
    private const val TAG = "LauncherIconManager"

    val ALIAS_MAP = mapOf(
        SettingsStore.ICON_BLACK_WHITE to "com.anindra.messages.LauncherBlackWhite",
        SettingsStore.ICON_STARK_GOLD to "com.anindra.messages.LauncherStarkGold",
        SettingsStore.ICON_MATRIX_GREEN to "com.anindra.messages.LauncherMatrixGreen",
        SettingsStore.ICON_CYBER_CYAN to "com.anindra.messages.LauncherCyberCyan"
    )

    fun applyIcon(context: Context, iconCode: String) {
        val targetAlias = ALIAS_MAP[iconCode] ?: ALIAS_MAP[SettingsStore.ICON_BLACK_WHITE]!!
        val pm = context.packageManager
        val pkg = context.packageName

        for ((_, aliasName) in ALIAS_MAP) {
            val component = ComponentName(pkg, aliasName)
            val shouldEnable = (aliasName == targetAlias)
            val newState = if (shouldEnable) {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            }

            try {
                val currentState = pm.getComponentEnabledSetting(component)
                if (currentState != newState) {
                    pm.setComponentEnabledSetting(
                        component,
                        newState,
                        PackageManager.DONT_KILL_APP
                    )
                    Log.d(TAG, "Switched alias $aliasName to state $newState")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error configuring alias $aliasName", e)
            }
        }
    }
}
