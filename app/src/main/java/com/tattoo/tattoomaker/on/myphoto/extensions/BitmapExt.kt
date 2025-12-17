package com.tattoo.tattoomaker.on.myphoto.extensions

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.graphics.scale
import kotlin.math.sqrt

@Suppress("DEPRECATION")
fun getBitmap(contentResolver: ContentResolver, fileUri: Uri?, maxBytes: Long = 50_000_000): Bitmap? {
    return try {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, fileUri!!)
            ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            }
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
        }

        val byteCount = bitmap.byteCount
        if (byteCount > maxBytes) {
            // Calculate scale factor to reduce size
            val scale = sqrt(maxBytes.toDouble() / byteCount).toFloat()
            val newWidth = (bitmap.width * scale).toInt()
            val newHeight = (bitmap.height * scale).toInt()

            // Create scaled bitmap
            val scaledBitmap = bitmap.scale(newWidth, newHeight)

            // Recycle original bitmap to free memory
            if (bitmap != scaledBitmap) {
                bitmap.recycle()
            }
            scaledBitmap
        } else {
            bitmap
        }
    } catch (e: Exception) {
        null
    }
}