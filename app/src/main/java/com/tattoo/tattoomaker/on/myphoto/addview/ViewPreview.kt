package com.tattoo.tattoomaker.on.myphoto.addview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.addview.viewedit.ViewEdit
import com.tattoo.tattoomaker.on.myphoto.addview.viewitem.ViewItemBottomEdit
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsDrawable
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom

@SuppressLint("ResourceType")
class ViewPreview(context: Context) : RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val viewToolbar: ViewToolbar
    val iv: ImageView

    val viewBottomPreview : ViewBottomPreview

    val viewLoading: LottieAnimationView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(Color.WHITE)
        isClickable = true
        isFocusable = true

        viewToolbar = ViewToolbar(context).apply {
            id = 1221
            ivBack.setImageResource(R.drawable.ic_home)
            tvTitle.text = resources.getString(R.string.complete)
            ivRight.setImageResource(R.drawable.ic_del)
            ivRight.visibility = GONE
        }
        addView(viewToolbar, LayoutParams(-1, (6.667f * w).toInt()).apply {
            topMargin = (15.833f * w).toInt()
        })

        val rl = RelativeLayout(context)
        iv = ImageView(context).apply { id = 1225 }
        rl.addView(iv, LayoutParams(-1, (141.11f * w).toInt()).apply {
            addRule(CENTER_IN_PARENT, TRUE)
        })

        addView(rl, LayoutParams(-1, -1).apply {
            addRule(BELOW, viewToolbar.id)
            bottomMargin = (25f * w).toInt()
        })

        viewBottomPreview = ViewBottomPreview(context)
        addView(viewBottomPreview, LayoutParams(-1, (21.389f * w).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
        })

        viewLoading = LottieAnimationView(context).apply {
            visibility = GONE
            setAnimation(R.raw.loading)
            repeatCount = LottieDrawable.INFINITE
        }
        viewLoading.playAnimation()
        addView(viewLoading, LayoutParams(-1, (84.22f * w).toInt()).apply {
            addRule(CENTER_IN_PARENT, TRUE)
            leftMargin = (5.556f * ViewEdit.w).toInt()
            rightMargin = (5.556f * ViewEdit.w).toInt()
        })
    }

    fun chooseTypeView(option: Int) {
        when(option) {
            0 -> {
                viewToolbar.ivBack.setImageResource(R.drawable.ic_home)

                if (viewBottomPreview.vEdit.isVisible) viewBottomPreview.vEdit.visibility = GONE
            }

            1 -> {
                UtilsDrawable.changeIcon(
                    context, "back", 1, R.drawable.ic_back, viewToolbar.ivBack,
                    ContextCompat.getColor(context, R.color.black_text),
                    ContextCompat.getColor(context, R.color.black_text)
                )

                if (!viewBottomPreview.vEdit.isVisible) viewBottomPreview.vEdit.visibility = VISIBLE
            }

            3 -> {
                UtilsDrawable.changeIcon(
                    context, "back", 1, R.drawable.ic_back, viewToolbar.ivBack,
                    ContextCompat.getColor(context, R.color.black_text),
                    ContextCompat.getColor(context, R.color.black_text)
                )

                if (viewBottomPreview.vEdit.isVisible) viewBottomPreview.vEdit.visibility = GONE
            }
        }
    }

    inner class ViewBottomPreview(context: Context): LinearLayout(context) {

        val vDownload: ViewItemBottomEdit
        val vShare: ViewItemBottomEdit
        val vWallpaper: ViewItemBottomEdit
        val vEdit: ViewItemBottomEdit

        init {
            orientation = HORIZONTAL
            setBackgroundColor(ContextCompat.getColor(context, R.color.black_bottom))

            vDownload = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_download)
                tv.text = resources.getText(R.string.download)
            }
            addView(vDownload, LayoutParams(-1, -2, 1F).apply {
                gravity = Gravity.CENTER
            })

            vShare = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_share_preview)
                tv.text = resources.getText(R.string.share)
            }
            addView(vShare, LayoutParams(-1, -2, 1F).apply {
                gravity = Gravity.CENTER
            })

            vWallpaper = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_wallpaper)
                tv.text = resources.getText(R.string.wallpaper)
            }
            addView(vWallpaper, LayoutParams(-1, -2, 1F).apply {
                gravity = Gravity.CENTER
            })

            vEdit = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_edit)
                tv.text = resources.getText(R.string.edit)
            }
            addView(vEdit, LayoutParams(-1, -2, 1F).apply {
                gravity = Gravity.CENTER
            })
        }
    }
}