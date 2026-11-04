package com.anindra.messages.data

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

/**
 * Storage manager for custom contact and user profile pictures.
 * Allows users to set, update, and persist custom avatar images for any contact or profile.
 */
object CustomAvatarStore {

    private fun getAvatarDirectory(context: Context): File {
        return File(context.filesDir, "custom_avatars").apply {
            if (!exists()) mkdirs()
        }
    }

    fun getAvatarFile(context: Context, key: String): File {
        val cleanKey = key.replace(Regex("[^a-zA-Z0-9_.-]"), "_")
        return File(getAvatarDirectory(context), "avatar_$cleanKey.jpg")
    }

    fun hasCustomAvatar(context: Context, key: String): Boolean {
        if (key.isBlank()) return false
        val file = getAvatarFile(context, key)
        return file.exists() && file.length() > 0
    }

    fun getAvatarUri(context: Context, key: String): String? {
        if (key.isBlank()) return null
        val file = getAvatarFile(context, key)
        return if (file.exists() && file.length() > 0) Uri.fromFile(file).toString() else null
    }

    /**
     * Copies selected image from content Uri into internal persistent storage.
     */
    fun saveAvatar(context: Context, key: String, sourceUri: Uri): Boolean {
        return try {
            val destFile = getAvatarFile(context, key)
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Removes custom avatar.
     */
    fun removeAvatar(context: Context, key: String): Boolean {
        val file = getAvatarFile(context, key)
        return if (file.exists()) file.delete() else true
    }
}
