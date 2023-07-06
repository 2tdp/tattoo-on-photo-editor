package com.tattoo.tattoomaker.on.myphoto.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityOptions
import android.content.*
import android.content.pm.ApplicationInfo
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.*
import android.os.Build
import android.os.Environment
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.MediaStore
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.IntRange
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tattoo.tattoomaker.on.myphoto.R
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*

object Utils {
    const val res = android.R.id.content
    private const val enter = R.anim.slide_in_right
    private const val exit = R.anim.slide_out_left
    private const val popEnter = R.anim.slide_in_left_small
    private const val popExit = R.anim.slide_out_right

    fun setStatusBarTransparent(activity: Activity) {
        val decorView: View = activity.window.decorView
        activity.window.statusBarColor = Color.TRANSPARENT
        activity.window.navigationBarColor = Color.TRANSPARENT
        var flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        decorView.systemUiVisibility = flags
    }

    fun hideKeyboard(context: Context, view: View?) {
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }

    fun showSoftKeyboard(context: Context, view: View) {
        if (view.requestFocus()) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun replaceFragment(
        manager: FragmentManager,
        fragment: Fragment,
        isAdd: Boolean,
        addBackStack: Boolean,
        isAnimation: Boolean
    ) {
        try {
            val fragmentTransaction = manager.beginTransaction()
            if (isAnimation) fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit)
            if (addBackStack) fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
            if (isAdd) fragmentTransaction.add(res, fragment, fragment.javaClass.simpleName)
            else fragmentTransaction.replace(res, fragment, fragment.javaClass.simpleName)

            fragmentTransaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun createBackground(
        colors: IntArray,
        border: Int,
        stroke: Int,
        colorStroke: Int
    ): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadius = border.toFloat()
        if (stroke != -1) drawable.setStroke(stroke, colorStroke)
        if (colors.size >= 2) {
            drawable.colors = colors
            drawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        } else drawable.setColor(colors[0])
        return drawable
    }

    fun createColorState(colorOn: Int, colorOff: Int): ColorStateList {
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )
        val colors = intArrayOf(colorOn, colorOff)
        return ColorStateList(states, colors)
    }

    @IntRange(from = 0, to = 3)
    fun getConnectionType(context: Context): Int {
        var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi
        val cm: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities: NetworkCapabilities? = cm.getNetworkCapabilities(cm.activeNetwork)
            if (capabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) result = 2
            else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) result = 1
            else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) result = 3
        }
        return result
    }

    fun effectVibrate(context: Context) {
        val v: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) v.vibrate(
            VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
        ) else v.vibrate(500)
    }

    fun effectPressRectangle(context: Context): TypedValue {
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        return outValue
    }

    fun effectPressOval(context: Context): TypedValue {
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true)
        return outValue
    }

    fun setIntent(context: Context, nameActivity: String?) {
        val intent = Intent()
        intent.component = nameActivity?.let { ComponentName(context, it) }
        context.startActivity(intent, ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_right, R.anim.slide_out_left).toBundle())
    }

    fun setAnimExit(activity: Activity) {
        activity.overridePendingTransition(R.anim.slide_in_left_small, R.anim.slide_out_right)
    }

    fun clearBackStack(manager: FragmentManager) {
        val count = manager.backStackEntryCount
        for (i in 0 until count) {
            manager.popBackStack()
        }
    }

    fun delFileInFolder(context: Context, nameFolder: String, nameFile: String) {
        val dir = File(getStore(context) + "/" + nameFolder + "/" + nameFile)
        if (dir.isDirectory) {
            val children = dir.listFiles()
            if (children != null)
                for (file in children) {
                    if (file.isFile) File(dir, file.name).delete()
                    else if (file.isDirectory) delFileInFolder(context, nameFolder, file.name)
                }
        }
    }

    fun writeToFileText(context: Context, data: String, name: String) {
        try {
            OutputStreamWriter(context.openFileOutput(name, Context.MODE_PRIVATE)).apply {
                write(data)
                close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun readFromFile(context: Context, name: String?): String {
        var data = ""
        try {
            val inputStream = context.openFileInput(name)
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var receiveString: String?
                val stringBuilder = java.lang.StringBuilder()
                while (bufferedReader.readLine().also { receiveString = it } != null) {
                    stringBuilder.append("\n").append(receiveString)
                }
                inputStream.close()
                data = stringBuilder.toString()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return data
    }

    fun createFileDescriptorFromByteArray(context: Context, mp3SoundByteArray: ByteArray?): FileDescriptor? {
        try {
            // create temp file that will hold byte array
            val tempMp3 = File.createTempFile(Constant.LINK_APP, "mp3", context.cacheDir)
            tempMp3.deleteOnExit()
            val fos = FileOutputStream(tempMp3)
            fos.write(mp3SoundByteArray)
            fos.close()
            val fis = FileInputStream(tempMp3)
            return fis.fd
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return null
    }

    fun convertListToString(lstInt: List<Int?>): String {
        val str = StringBuilder()
        val iterator = lstInt.iterator()
        while (iterator.hasNext()) {
            str.append(iterator.next())
            if (iterator.hasNext()) str.append("_")
        }
        return str.toString()
    }

    fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) return true
        }
        return false
    }

    fun underLine(strUnder: String?): SpannableString {
        val underLine = SpannableString(strUnder)
        underLine.setSpan(UnderlineSpan(), 0, underLine.length, 0)
        return underLine
    }

    fun getMIMEType(url: String?): String? {
        val mType: String?
        val mExtension: String = MimeTypeMap.getFileExtensionFromUrl(url)
        mType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mExtension)
        return mType
    }

    fun getTypeFace(fontFolder: String, nameFont: String, context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, "fonts/$fontFolder/$nameFont")
    }

    fun saveImage(context: Context, bitmap: Bitmap?, namePic: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (bitmap != null) {
                val fileName = makeFilename(context, "$namePic-IMG-${Calendar.getInstance(Locale.getDefault()).time}")
                val outFile = File(fileName)
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, outFile.name)
                    put(MediaStore.MediaColumns.MIME_TYPE, getMIMEType(outFile.path))
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val contentResolver = context.applicationContext.contentResolver
                val contentUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                var newUri: Uri? = null
                val output: OutputStream?
                try {
                    newUri = contentResolver.insert(contentUri, values)
                    output = contentResolver.openOutputStream(newUri!!)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)

                } catch (e: IOException) {
                    contentResolver.delete(newUri!!, null, null)
                }
            }
        } else {
            if (bitmap != null) {
                try {
                    val fileName = makeFilename(context, "$namePic-IMG-${Calendar.getInstance(Locale.getDefault()).time}")
                    val outFile = File(fileName)
                    val values = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, outFile.name)
                        put(MediaStore.Images.Media.MIME_TYPE, getMIMEType(outFile.path))
                        put(MediaStore.MediaColumns.DATA, outFile.path)
                    }
                    val contentResolver = context.contentResolver
                    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    val output: OutputStream?
                    try {
                        output = contentResolver.openOutputStream(uri!!)
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
                    } catch (e: IOException) {
                        if (uri != null) contentResolver.delete(uri, null, null)
                    }
                } catch (e: java.lang.Exception) {
                    Log.d("onBtnSavePng", e.toString()) // java.io.IOException: Operation not permitted
                }
            }
        }
    }

    fun makeFilename(context: Context, namePic: String): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            return getStore(context) + "/" + namePic + ".png"
        var externalRootDir = Environment.getExternalStorageDirectory().path
        if (!externalRootDir.endsWith("/")) externalRootDir += "/"
        val subdir = "media/image/"
        var parentDir = externalRootDir + subdir

        // Create the parent directory
        val parentDirFile = File(parentDir)
        parentDirFile.mkdirs()

        // If we can't write to that special path, try just writing directly to the sdcard
        if (!parentDirFile.isDirectory) parentDir = externalRootDir

        // Turn the title into a filename
        val filename = StringBuilder()
        for (i in 0 until "remi-tattoo-on-photo".length) {
            if (Character.isLetterOrDigit("remi-tattoo-on-photo"[i])) filename.append("remi-tattoo-on-photo"[i])
        }

        // Try to make the filename unique
        var path: String? = null
        for (i in 0..99) {
            val testPath = if (i > 0) "$parentDir$filename$i.png" else "$parentDir$filename.png"
            try {
                val f = RandomAccessFile(File(testPath), "r")
                f.close()
            } catch (e: Exception) {
                // Good, the file didn't exist
                path = testPath
                break
            }
        }
        return path
    }

    fun getStore(c: Context): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val f = c.getExternalFilesDir(null)
            if (f != null) f.absolutePath else "/storage/emulated/0/Android/data/" + c.packageName
        } else Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + c.packageName
    }

    fun makeFolder(c: Context, nameFolder: String): String {
        val path = getStore(c) + "/" + nameFolder
        val f = File(path)
        if (!f.exists()) f.mkdirs()
        return path
    }

    fun openFacebook(c: Context) {
        try {
            val applicationInfo: ApplicationInfo = c.packageManager.getApplicationInfo("com.facebook.katana", 0)
            if (applicationInfo.enabled) {
                val uri = Uri.parse("fb://page/" + "111335474750038")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                c.startActivity(intent)
            }
        } catch (ignored: Exception) {
            openLink(c, "https://www.facebook.com/REMI-Studio-111335474750038")
        }
    }

    fun openLink(c: Context, url: String) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url.replace("HTTPS", "https")))
            c.startActivity(i)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(c, c.getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
    }

    fun openInstagram(c: Context) {
        val appUri = Uri.parse("https://instagram.com/_u/" + "remi_studio_app/")
        val browserUri = Uri.parse("https://instagram.com/" + "remi_studio_app/")
        try { //first try to open in instagram app
            val appIntent: Intent? = c.packageManager.getLaunchIntentForPackage("com.instagram.android")
            if (appIntent != null) {
                appIntent.action = Intent.ACTION_VIEW
                appIntent.data = appUri
                c.startActivity(appIntent)
            } else {
                val browserIntent = Intent(Intent.ACTION_VIEW, browserUri)
                c.startActivity(browserIntent)
            }
        } catch (e: Exception) { //or else open in browser
            val browserIntent = Intent(Intent.ACTION_VIEW, browserUri)
            c.startActivity(browserIntent)
        }
    }

    fun rateApp(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://play.google.com/store/apps/details?id=" + context.packageName)
        context.startActivity(intent)
    }

    fun shareApp(context: Context) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.app_name))
            var shareMessage = "Let me recommend you this application\nDownload now:\n\n"
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + context.packageName
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            context.startActivity(Intent.createChooser(shareIntent, ""))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendFeedback(context: Context) {
        val selectorIntent = Intent(Intent.ACTION_SENDTO)
        selectorIntent.data = Uri.parse("mailto:")
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(Constant.GMAIL))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.app_name) + " feedback")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "The email body...")
        emailIntent.selector = selectorIntent
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }

    fun moreApps(context: Context) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + Constant.NAME_DEV)))
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:" + Constant.NAME_DEV)))
        }
    }

    fun privacyApp(context: Context) {
        val linkPrivacy = "https://myweatherforecastandwidget.blogspot.com/"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(linkPrivacy))
        context.startActivity(browserIntent)
    }

    fun shareFile(context: Context, bitmap: Bitmap, application: String?) {
        val uri = saveImageExternal(context, bitmap)
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.setPackage(application)
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.setDataAndType(uri, "image/*")
        context.startActivity(Intent.createChooser(shareIntent, context.resources.getString(R.string.app_name)))
    }

    fun saveImageExternal(context: Context, image: Bitmap): Uri? {
        //TODO - Should be processed in another thread
        var uri: Uri? = null
        try {
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "remi-tattoo-editor.png")
            val stream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
            uri = FileProvider.getUriForFile(context, "com.tattoo.tattoomaker.on.myphoto", file)
        } catch (e: IOException) {
            Log.d("TAG", "IOException while trying to write file for sharing: " + e.message)
        }
        return uri
    }
}