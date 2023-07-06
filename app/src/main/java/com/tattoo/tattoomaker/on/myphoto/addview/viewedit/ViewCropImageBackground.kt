package com.tattoo.tattoomaker.on.myphoto.addview.viewedit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.canhub.cropper.CropImageView
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.addview.ViewToolbar
import com.tattoo.tattoomaker.on.myphoto.addview.viewitem.ViewItemBottomEdit
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsDrawable

@SuppressLint("ResourceType")
class ViewCropImageBackground(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val viewToolbar: ViewToolbar
    val viewCrop: CropImageView
    val viewBottomCrop: ViewBottomCrop
    val viewLoading: LottieAnimationView

    init {
        w = resources.displayMetrics.widthPixels / 100f
        setBackgroundColor(Color.WHITE)
        isClickable = true
        isFocusable = true

        viewToolbar = ViewToolbar(context).apply {
            id = 1221
            UtilsDrawable.changeIcon(
                context, "back", 1, R.drawable.ic_back, ivBack,
                ContextCompat.getColor(context, R.color.black_text),
                ContextCompat.getColor(context, R.color.black_text)
            )
            ivRight.setImageResource(R.drawable.ic_tick)
            tvTitle.text = resources.getString(R.string.crop_a_picture)
        }
        addView(viewToolbar, LayoutParams(-1, -2).apply {
            topMargin = (13.61f * w).toInt()
        })

        viewBottomCrop = ViewBottomCrop(context)
        addView(viewBottomCrop, LayoutParams(-1, (21.389f * w).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
        })

        val rlMain = RelativeLayout(context)
        addView(rlMain, LayoutParams(-1, -1).apply {
            addRule(BELOW, viewToolbar.id)
            topMargin = (6.11f * w).toInt()
            bottomMargin = (50f * w).toInt()
            leftMargin = (2.778f * w).toInt()
            rightMargin = (2.778f * w).toInt()
        })

        viewCrop = CropImageView(context)
        rlMain.addView(viewCrop, LayoutParams(-1, -1).apply {
            addRule(CENTER_IN_PARENT, TRUE)
        })

        viewLoading = LottieAnimationView(context).apply {
            visibility = GONE
            setAnimation(R.raw.loading)
            repeatCount = LottieDrawable.INFINITE
        }
        viewLoading.playAnimation()
        addView(viewLoading, LayoutParams(-1, (84.22f * ViewEdit.w).toInt()).apply {
            addRule(CENTER_IN_PARENT, TRUE)
            leftMargin = (5.556f * w).toInt()
            rightMargin = (5.556f * w).toInt()
        })
    }

    inner class ViewBottomCrop(context: Context): LinearLayout(context) {

        val viewRotate: ViewItemBottomEdit
        val viewFlipY: ViewItemBottomEdit
        val viewFlipX: ViewItemBottomEdit

        init {
            orientation = HORIZONTAL
            setBackgroundColor(ContextCompat.getColor(context, R.color.black_bottom))

            viewRotate = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_rotate_crop)
                tv.text = resources.getText(R.string.rotate)
            }
            addView(viewRotate, LayoutParams(-1, -2, 1F).apply {
                gravity = Gravity.CENTER
            })

            viewFlipY = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_flip_y_crop)
                tv.text = resources.getText(R.string.flip_y)
            }
            addView(viewFlipY, LayoutParams(-1, -2, 1F).apply {
                gravity = Gravity.CENTER
            })

            viewFlipX = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_flip_x_crop)
                tv.text = resources.getText(R.string.flip_x)
            }
            addView(viewFlipX, LayoutParams(-1, -2, 1F).apply {
                gravity = Gravity.CENTER
            })
        }
    }
}