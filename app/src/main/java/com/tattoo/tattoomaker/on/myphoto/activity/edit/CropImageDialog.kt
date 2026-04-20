package com.tattoo.tattoomaker.on.myphoto.activity.edit

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.databinding.DialogCropImageBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CropImageDialog : DialogFragment() {

    private var _binding: DialogCropImageBinding? = null
    private val binding get() = _binding!!

    var callBack: ICallBackItem? = null

    private var fromAssets: Boolean = false
    private var uriImage: String = ""

    companion object {
        private const val ARG_FROM_ASSETS = "from_assets"
        private const val ARG_URI_IMAGE = "uri_image"

        fun newInstance(fromAssets: Boolean, uriImage: String): CropImageDialog {
            return CropImageDialog().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_FROM_ASSETS, fromAssets)
                    putString(ARG_URI_IMAGE, uriImage)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
        arguments?.let {
            fromAssets = it.getBoolean(ARG_FROM_ASSETS, false)
            uriImage = it.getString(ARG_URI_IMAGE, "")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogCropImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llRotate.setOnClickListener { binding.vCrop.rotateImage(90) }
        binding.llFlipY.setOnClickListener { binding.vCrop.flipImageVertically() }
        binding.llFlipX.setOnClickListener { binding.vCrop.flipImageHorizontally() }
        binding.ivDone.setOnUnDoubleClickListener {
            binding.vCrop.getCroppedImage()?.let { callBack?.callBack(it, -1) }
            dismiss()
        }
        binding.ivBack.setOnClickListener { dismiss() }

        // Load bitmap on IO thread to avoid ANR with large images
        lifecycleScope.launch {
            val bitmap = withContext(Dispatchers.IO) { loadImage() }
            bitmap?.let { binding.vCrop.setImageBitmap(it) }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): android.app.Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    @Suppress("DEPRECATION")
    override fun onStart() {
        super.onStart()
        dialog?.window?.let { w ->
            w.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
            w.setBackgroundDrawable(Color.BLACK.toDrawable())
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

            w.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    private fun loadImage(): Bitmap? {
        val ctx = requireActivity()
        return if (!fromAssets) {
            UtilsBitmap.getBitmapFromUri(ctx, uriImage.toUri())?.let { bm ->
                UtilsBitmap.modifyOrientation(ctx, bm, uriImage.toUri())
            }
        } else {
            UtilsBitmap.getBitmapFromAsset(ctx, "tattoo/background", uriImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}