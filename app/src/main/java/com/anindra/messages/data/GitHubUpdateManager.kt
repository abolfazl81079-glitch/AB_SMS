package com.anindra.messages.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

/**
 * GitHub Releases update manager for AB/SMS.
 * Handles update checks, changelog retrieval, APK download, and SHA-256 verification.
 */
object GitHubUpdateManager {

    private const val DEFAULT_REPO = "absms-app/AB_SMS"

    data class ReleaseInfo(
        val tagName: String,
        val versionName: String,
        val releaseNotes: String,
        val apkDownloadUrl: String?,
        val apkName: String?,
        val hasNewerVersion: Boolean
    )

    /**
     * Checks GitHub for the latest release.
     */
    suspend fun checkUpdate(context: Context, repo: String = DEFAULT_REPO): Result<ReleaseInfo> = withContext(Dispatchers.IO) {
        try {
            val endpoint = "https://api.github.com/repos/$repo/releases/latest"
            val url = URL(endpoint)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json")
            conn.setRequestProperty("User-Agent", "AB_SMS_Android")
            conn.connectTimeout = 10000
            conn.readTimeout = 15000

            val code = conn.responseCode
            if (code != 200) {
                return@withContext Result.failure(Exception("GitHub API returned HTTP $code"))
            }

            val body = conn.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(body)

            val tagName = json.optString("tag_name", "")
            val notes = json.optString("body", "No release notes provided.")
            val assets = json.optJSONArray("assets")

            var apkUrl: String? = null
            var apkName: String? = null

            if (assets != null) {
                for (i in 0 until assets.length()) {
                    val asset = assets.getJSONObject(i)
                    val name = asset.optString("name", "")
                    if (name.endsWith(".apk", ignoreCase = true)) {
                        apkUrl = asset.optString("browser_download_url")
                        apkName = name
                        break
                    }
                }
            }

            val currentVer = try {
                val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                pInfo.versionName ?: "1.0.0"
            } catch (_: Exception) {
                "1.0.0"
            }

            val cleanTag = tagName.removePrefix("v").removePrefix("V")
            val isNewer = isVersionNewer(cleanTag, currentVer)

            Result.success(
                ReleaseInfo(
                    tagName = tagName,
                    versionName = cleanTag,
                    releaseNotes = notes,
                    apkDownloadUrl = apkUrl,
                    apkName = apkName,
                    hasNewerVersion = isNewer
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Downloads APK from URL and returns the local File.
     */
    suspend fun downloadApk(
        context: Context,
        downloadUrl: String,
        fileName: String,
        onProgress: (Int) -> Unit = {}
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val url = URL(downloadUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 15000
            conn.readTimeout = 30000
            conn.setRequestProperty("User-Agent", "AB_SMS_Android")

            val totalLength = conn.contentLength
            val destDir = File(context.cacheDir, "updates").apply { mkdirs() }
            val targetFile = File(destDir, fileName)

            conn.inputStream.use { input ->
                FileOutputStream(targetFile).use { output ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Int
                    var totalRead = 0L

                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        totalRead += bytesRead
                        if (totalLength > 0) {
                            val progress = ((totalRead * 100) / totalLength).toInt()
                            onProgress(progress)
                        }
                    }
                }
            }

            Result.success(targetFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Verifies SHA-256 hash of downloaded APK.
     */
    fun verifySha256(file: File, expectedHash: String): Boolean {
        if (!file.exists()) return false
        val md = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { input ->
            val buffer = ByteArray(8192)
            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } != -1) {
                md.update(buffer, 0, bytesRead)
            }
        }
        val digest = md.digest().joinToString("") { "%02x".format(it) }
        return digest.equals(expectedHash.trim(), ignoreCase = true)
    }

    /**
     * Launches system package installer for the downloaded APK.
     */
    fun installApk(context: Context, apkFile: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            apkFile
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    private fun isVersionNewer(latest: String, current: String): Boolean {
        val lParts = latest.split('.').mapNotNull { it.toIntOrNull() }
        val cParts = current.split('.').mapNotNull { it.toIntOrNull() }
        val max = maxOf(lParts.size, cParts.size)
        for (i in 0 until max) {
            val l = lParts.getOrElse(i) { 0 }
            val c = cParts.getOrElse(i) { 0 }
            if (l > c) return true
            if (l < c) return false
        }
        return false
    }
}
