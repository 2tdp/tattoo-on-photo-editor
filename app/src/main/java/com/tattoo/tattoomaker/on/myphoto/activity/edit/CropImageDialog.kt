package com.tattoo.tattoomaker.on.myphoto.activity.edit

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.databinding.DialogCropImageBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsBitmap

class CropImageDialog(private val context: AppCompatActivity, private val fromAssets: Boolean, private val uriImage: String) : AlertDialog(context, R.style.SheetDialog) {

    private val binding: DialogCropImageBinding = DialogCropImageBinding.inflate(LayoutInflater.from(context))

    var callBack: ICallBackItem? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(Color.BLACK.toDrawable())
        setView(binding.root)
        setCancelable(false)
        window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.llRotate.setOnClickListener { binding.vCrop.rotateImage(90) }
        binding.llFlipY.setOnClickListener { binding.vCrop.flipImageVertically() }
        binding.llFlipX.setOnClickListener { binding.vCrop.flipImageHorizontally() }
        binding.ivDone.setOnUnDoubleClickListener {
            callBack?.callBack(binding.vCrop.getCroppedImage(), -1)
        }
        binding.ivBack.setOnClickListener { dismiss() }
    }

    fun showDialog() {
        show()

        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        window?.decorView?.systemUiVisibility = hideSystemBars()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

        loadImage()
    }

    @Suppress("DEPRECATION")
    private fun hideSystemBars(): Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

    private fun loadImage(): Bitmap? {
        return if (!fromAssets)
            UtilsBitmap.modifyOrientation(context, UtilsBitmap.getBitmapFromUri(context, uriImage.toUri())!!, uriImage.toUri())
        else UtilsBitmap.getBitmapFromAsset(context, "tattoo/background", uriImage)
    }
}