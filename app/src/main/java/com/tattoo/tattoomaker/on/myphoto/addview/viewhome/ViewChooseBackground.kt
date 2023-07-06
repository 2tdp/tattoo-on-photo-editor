package com.tattoo.tattoomaker.on.myphoto.addview.viewhome

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.addview.ViewToolbar
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsDrawable
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom

@SuppressLint("ResourceType")
class ViewChooseBackground(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val viewToolbar: ViewToolbar
    val tvCamera: TextView
    val tvGallery: TextView
    val rcv: RecyclerView
    val viewLoading: LottieAnimationView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(Color.WHITE)
        isClickable = true
        isFocusable = true


        viewToolbar = ViewToolbar(context).apply {
            tvTitle.text = resources.getString(R.string.pick_a_picture)
            id = 1221
            UtilsDrawable.changeIcon(
                context, "back", 1, R.drawable.ic_back, ivBack,
                ContextCompat.getColor(context, R.color.black_text),
                ContextCompat.getColor(context, R.color.black_text)
            )
            ivRight.visibility = GONE
        }
        addView(viewToolbar, LayoutParams(-1, -2).apply {
            topMargin = (13.61f * w).toInt()
        })

        val ll = LinearLayout(context).apply {
            id = 1222
            orientation = LinearLayout.HORIZONTAL
        }

        tvCamera = TextView(context).apply {
            textCustom(
                resources.getString(R.string.camera),
                ContextCompat.getColor(context, R.color.white),
                4.44f * w,
                "solway_regular",
                context
            )
            gravity = Gravity.CENTER
            background = Utils.createBackground(
                intArrayOf(
                    ContextCompat.getColor(context, R.color.main_color_1),
                    ContextCompat.getColor(context, R.color.main_color_2)
                ),
                (3.5f * w).toInt(), -1, -1
            )
        }
        ll.addView(tvCamera, LinearLayout.LayoutParams(-1, -1, 1F).apply {
            rightMargin = (2.22f * w).toInt()
        })

        tvGallery = TextView(context).apply {
            textCustom(
                resources.getString(R.string.gallery),
                ContextCompat.getColor(context, R.color.white),
                4.44f * w,
                "solway_regular",
                context
            )
            gravity = Gravity.CENTER
            background = Utils.createBackground(
                intArrayOf(
                    ContextCompat.getColor(context, R.color.main_color_1),
                    ContextCompat.getColor(context, R.color.main_color_2)
                ),
                (3.5f * w).toInt(), -1, -1
            )
        }
        ll.addView(tvGallery, LinearLayout.LayoutParams(-1, -1, 1F).apply {
            leftMargin = (2.22f * w).toInt()
        })

        addView(ll, LayoutParams(-1, (16.111f * w).toInt()).apply {
            addRule(BELOW, viewToolbar.id)
            leftMargin = (4.44f * w).toInt()
            rightMargin = (4.44f * w).toInt()
            topMargin = (7.778f * w).toInt()
        })

        rcv = RecyclerView(context).apply { isVerticalScrollBarEnabled = false }
        addView(rcv, LayoutParams(-1, -1).apply {
            addRule(BELOW, ll.id)
            topMargin = (5.556f * w).toInt()
            leftMargin = (2.5f * w ).toInt()
            rightMargin = (2.5f * w ).toInt()
        })

        viewLoading = LottieAnimationView(context).apply {
            setAnimation(R.raw.loading)
            repeatCount = LottieDrawable.INFINITE
        }
        viewLoading.playAnimation()
        addView(viewLoading, LayoutParams(-1, (84.22f * w).toInt()).apply {
            addRule(CENTER_IN_PARENT, TRUE)
            leftMargin = (5.556f * w).toInt()
            rightMargin = (5.556f * w).toInt()
        })
    }
}