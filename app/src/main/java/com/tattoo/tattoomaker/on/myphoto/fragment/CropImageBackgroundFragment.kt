package com.tattoo.tattoomaker.on.myphoto.fragment

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.tattoo.tattoomaker.on.myphoto.addview.viewedit.ViewCropImageBackground
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackCheck
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.model.picture.PicModel
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsBitmap

class CropImageBackgroundFragment(picture: PicModel, callBack: ICallBackItem, isFinish: ICallBackCheck): Fragment() {

    private lateinit var viewCropImageBackground: ViewCropImageBackground
    private val picture: PicModel
    private val callBack: ICallBackItem
    private val isFinish: ICallBackCheck

    companion object {
        fun newInstance(picture: PicModel, callBack: ICallBackItem, isFinish: ICallBackCheck): CropImageBackgroundFragment {
            val args = Bundle()

            val fragment = CropImageBackgroundFragment(picture, callBack, isFinish)
            fragment.arguments = args
            return fragment
        }
    }

    init {
        this.picture = picture
        this.callBack = callBack
        this.isFinish = isFinish
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewCropImageBackground = ViewCropImageBackground(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return viewCropImageBackground
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        evenClick()
    }

    private fun initView() {
        viewCropImageBackground.viewLoading.visibility = View.VISIBLE
        Thread {
            val bitmap = if (!picture.bucket.equals("tattoo_background"))
                UtilsBitmap.modifyOrientation(
                    requireContext(),
                    UtilsBitmap.getBitmapFromUri(requireContext(), Uri.parse(picture.uri))!!,
                    Uri.parse(picture.uri)
                )
            else
                UtilsBitmap.getBitmapFromAsset(requireContext(), "tattoo/background", picture.uri)!!

            Handler(Looper.getMainLooper()).post {
                viewCropImageBackground.viewLoading.visibility = View.GONE
                viewCropImageBackground.viewCrop.setImageBitmap(bitmap)
            }
        }.start()
    }

    private fun evenClick() {
        viewCropImageBackground.viewBottomCrop.viewRotate.setOnClickListener {
            viewCropImageBackground.viewCrop.rotateImage(90)
        }

        viewCropImageBackground.viewBottomCrop.viewFlipY.setOnClickListener {
            viewCropImageBackground.viewCrop.flipImageVertically()
        }

        viewCropImageBackground.viewBottomCrop.viewFlipX.setOnClickListener {
            viewCropImageBackground.viewCrop.flipImageHorizontally()
        }

        viewCropImageBackground.viewToolbar.ivRight.setOnClickListener {
            callBack.callBack(viewCropImageBackground.viewCrop.getCroppedImage()!!, -1)
            Utils.clearBackStack(parentFragmentManager)
        }
        viewCropImageBackground.viewToolbar.ivBack.setOnClickListener {
            isFinish.check(true)
            Utils.clearBackStack(parentFragmentManager)
        }
    }
}