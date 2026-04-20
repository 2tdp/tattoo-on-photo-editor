package com.tattoo.tattoomaker.on.myphoto.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.tattoo.tattoomaker.on.myphoto.BuildConfig
import com.tattoo.tattoomaker.on.myphoto.model.LanguageModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import kotlin.apply
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.io.use
import kotlin.let

fun Any?.toJson(): String = Gson().toJson(this)

fun AppCompatActivity.changeLanguage(context: Context, language: LanguageModel?): Context {
    language?.let {
        try {
            Locale.setDefault(language.locale)
            return context.createConfigurationContext(Configuration(context.resources.configuration).apply {
                this.setLocale(language.locale)
            })
        } catch (e: Exception) {
            e.printStackTrace()
            return context.createConfigurationContext(Configuration(context.resources.configuration).apply {
                this.setLocale(Locale.ENGLISH)
            })
        }
    }
    return context.createConfigurationContext(Configuration(context.resources.configuration).apply {
        this.setLocale(Locale.ENGLISH)
    })
}

fun AppCompatActivity.showToast(msg: String, gravity: Int) {
    val toast: Toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
    toast.setGravity(gravity, 0, 0)
    toast.show()
}

fun AppCompatActivity.openSettingPermission(action: String) {
    val intent = Intent(action)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

fun AppCompatActivity.startShareFileIntent(path: String, mimeType: String = "image/*") {
    try {
        Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            val uri = FileProvider.getUriForFile(
                this@startShareFileIntent,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                File(path)
            )
            putExtra(Intent.EXTRA_STREAM, uri)

            val chooser = Intent.createChooser(this@apply, "Share file")
            val resInfoList: List<ResolveInfo> = packageManager
                .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                grantUriPermission(
                    packageName,
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            startActivity(chooser)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Activity.bitmapToUri(bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): Uri? {
    return try {
        val file = File.createTempFile("temp_image_", ".png", cacheDir).apply {
            deleteOnExit()
        }
        FileOutputStream(file).use { out ->
            bitmap.compress(format, 100, out)
        }
        FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            file
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun AppCompatActivity.getBitmapFromUri(uri: Uri): Bitmap? {
    val contentResolver = contentResolver
    return withContext(Dispatchers.IO) {
        getBitmap(contentResolver, uri)
    }
}

fun AppCompatActivity.launchOnMain(
    block: suspend CoroutineScope.() -> Unit
): Job {
    return lifecycleScope.launch(
        CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() },
        block = block
    )
}

fun AppCompatActivity.shareMultipleFile(
    fileNames: List<String>,
    mimeType: String = "image/*"
): Boolean {
    try {
        val uris = mutableListOf<Uri>()
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
        val chooser = Intent.createChooser(intent, "Share files")
        fileNames.forEach { fileName ->
            val file = File(fileName)
            if (file.exists()) {
                val fileUri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    file
                )
                uris.add(fileUri)

                val resInfoList: List<ResolveInfo> = packageManager.queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

                for (resolveInfo in resInfoList) {
                    val packageName = resolveInfo.activityInfo.packageName
                    grantUriPermission(
                        packageName,
                        fileUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
            }
        }

        if (uris.isNotEmpty()) {
            intent.type = mimeType
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,
                kotlin.collections.ArrayList(uris)
            )
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(chooser)
            return true
        }
        return false
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}
