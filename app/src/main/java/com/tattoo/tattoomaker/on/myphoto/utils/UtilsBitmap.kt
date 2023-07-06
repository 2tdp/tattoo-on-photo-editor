package com.tattoo.tattoomaker.on.myphoto.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import java.io.*
import java.lang.Exception
import java.util.*

object UtilsBitmap {

    fun drawIconWithPath(canvas: Canvas, path: Path, paint: Paint?, size: Float, x: Int, y: Int) {
        val rectF = RectF()
        path.computeBounds(rectF, true)
        val scale = if (rectF.width() >= rectF.height()) size / rectF.width()
        else size / rectF.height()
        canvas.translate(x.toFloat(), y.toFloat())
        canvas.scale(scale, scale)
        canvas.drawPath(path, paint!!)
    }

    fun toARGBString(alpha: Int, color: Int): String {
        // format: #AARRGGBB
        var hex = Integer.toHexString(alpha).uppercase()
        if (hex.length == 1) hex = "0$hex"
        var red = Integer.toHexString(Color.red(color))
        var green = Integer.toHexString(Color.green(color))
        var blue = Integer.toHexString(Color.blue(color))
        if (red.length == 1) red = "0$red"
        if (green.length == 1) green = "0$green"
        if (blue.length == 1) blue = "0$blue"
        Log.d(Constant.TAG, "toARGBString: #$hex$red$green$blue")
        return "#$hex$red$green$blue"
    }

    fun toRGBString(color: Int): String {
        // format: #RRGGBB
        var red = Integer.toHexString(Color.red(color))
        var green = Integer.toHexString(Color.green(color))
        var blue = Integer.toHexString(Color.blue(color))
        if (red.length == 1) red = "0$red"
        if (green.length == 1) green = "0$green"
        if (blue.length == 1) blue = "0$blue"
        return "#$red$green$blue"
    }

    fun overlay(bmp1: Bitmap, bmp2: Bitmap, isScale: Boolean): Bitmap {
        var w = 0
        var h = 0
        if (isScale) {
            w = 1080
            h = 1080 * bmp1.height / bmp1.width
        } else {
            w = bmp1.width
            h = bmp1.height
        }
        val bmOverlay = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmOverlay)
        val rectF = RectF(0F, 0F, w.toFloat(), h.toFloat())
        canvas.drawBitmap(bmp1, null, rectF, Paint())
        canvas.drawBitmap(bmp2, null, rectF, Paint())
        return bmOverlay
    }

    fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        val img: Bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
        bitmap.recycle()
        return img
    }

    fun getRoundedCornerBitmap(bitmap: Bitmap, cornerSizePx: Int): Bitmap {
        val output: Bitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        paint.isAntiAlias = true
        paint.color = -0x1
        paint.style = Paint.Style.FILL
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawRoundRect(rectF, cornerSizePx.toFloat(), cornerSizePx.toFloat(), paint)

        // draw bitmap
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    fun getBitmapFromVectorDrawable(context: Context?, drawableId: Int): Bitmap? {
        val drawable: Drawable? = context?.let { ContextCompat.getDrawable(it, drawableId) }
        var bitmap: Bitmap? = null
        bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        if (bitmap != null) {
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
        return null
    }

    fun loadBitmapFromView(context: Context?, view: View): Bitmap {
        val b: Bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        view.layout(view.left, view.top, view.right, view.bottom)
        val c = Canvas(b)
        c.rotate(view.rotation, view.width.toFloat() / 2, view.height.toFloat() / 2)
        view.draw(c)
        return b
    }

    fun getImageSize(context: Context, uri: Uri?): FloatArray {
        try {
            val options: BitmapFactory.Options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            val input = context.contentResolver.openInputStream(uri!!)
            BitmapFactory.decodeStream(input, null, options)
            input!!.close()
            return floatArrayOf(options.outWidth.toFloat(), options.outHeight.toFloat())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
        return floatArrayOf(0f, 0f)
    }

    fun createByteImage(context: Context, uriPic: String?, callBack: ICallBackItem) {
        val bmp: Bitmap
        try {
            bmp = getBitmapFromUri(context, Uri.parse(uriPic))?.let {
                modifyOrientation(
                    context,
                    it,
                    Uri.parse(uriPic)
                )
            }!!
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 84, stream)
            callBack.callBack(stream.toByteArray(), -1)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun createByteAudio(uriAudio: String?, callBack: ICallBackItem) {
        try {
            val inputStream = FileInputStream(uriAudio)
            val os = ByteArrayOutputStream()
            val buffer = ByteArray(0xFFFF)
            var len = inputStream.read(buffer)
            while (len != -1) {
                os.write(buffer, 0, len)
                len = inputStream.read(buffer)
            }
            callBack.callBack(os.toByteArray(), -1)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun saveBitmapToApp(
        context: Context?,
        bitmap: Bitmap,
        nameFolder: String,
        nameFile: String
    ): String {
        val directory = Utils.getStore(context!!) + "/" + nameFolder + "/"
        val myPath = File(directory, "$nameFile-${Calendar.getInstance(Locale.getDefault()).time}.png")
        try {
            val fos = FileOutputStream(myPath)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
            return myPath.path
        } catch (e: Exception) {
            Log.e("SAVE_IMAGE", e.message, e)
        }
        return ""
    }

    fun getBitmapFromUri(context: Context, selectedFileUri: Uri?): Bitmap? {
        var image: Bitmap? = null
        try {
            val parcelFileDescriptor: ParcelFileDescriptor? =
                context.contentResolver.openFileDescriptor(selectedFileUri!!, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }

    fun getBitmapFromAsset(context: Context, nameFolder: String, name: String): Bitmap? {
        val assetManager: AssetManager = context.assets
        val istr: InputStream
        var bitmap: Bitmap? = null
        try {
            istr = assetManager.open("$nameFolder/$name")
            bitmap = BitmapFactory.decodeStream(istr)
        } catch (e: IOException) {
            // handle exception
            e.printStackTrace()
        }
        return bitmap
    }

    @SuppressLint("Recycle")
    @Throws(IOException::class)
    fun modifyOrientation(context: Context, bitmap: Bitmap, uri: Uri?): Bitmap {
        val `is` = context.contentResolver.openInputStream(uri!!)
        val ei = ExifInterface(`is`!!)
        val orientation =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            else -> bitmap
        }
    }

    @Throws(IOException::class)
    fun modifyOrientation(bitmap: Bitmap, fileDescriptor: FileDescriptor?): Bitmap {
        val ei = ExifInterface(fileDescriptor!!)
        val orientation =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            else -> bitmap
        }
    }

    fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
}