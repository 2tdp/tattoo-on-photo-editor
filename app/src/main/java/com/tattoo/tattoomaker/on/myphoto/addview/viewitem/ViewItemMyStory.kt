package com.tattoo.tattoomaker.on.myphoto.addview.viewitem

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.addview.ViewMyStory
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom

@SuppressLint("ResourceType")
class ViewItemMyStory(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val rlNoData: RelativeLayout
    val tvMove: TextView
    val rcv: RecyclerView

    val viewLoading: LottieAnimationView

    init {
        w = resources.displayMetrics.widthPixels / 100F

        rlNoData = RelativeLayout(context).apply { setBackgroundResource(R.drawable.border_shadow) }
        val tv = TextView(context).apply {
            id = 1221
            textCustom(
                resources.getString(R.string.go_to_edit_photo_now),
                ContextCompat.getColor(context, R.color.gray_light_3),
                4.44f * w, "solway_medium", context
            )
        }
        rlNoData.addView(tv, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (6.11f * w).toInt()
        })

        tvMove = TextView(context).apply {
            textCustom(
                resources.getString(R.string.move),
                ContextCompat.getColor(context, R.color.white),
                4.44f * w, "solway_medium", context
            )
            background = Utils.createBackground(
                intArrayOf(
                    ContextCompat.getColor(context, R.color.main_color_1),
                    ContextCompat.getColor(context, R.color.main_color_2)
                ), (3.5f * w).toInt(), -1, -1
            )
            gravity = Gravity.CENTER
        }
        rlNoData.addView(tvMove, LayoutParams((29.44f * w).toInt(), (10f * w).toInt()).apply {
            addRule(BELOW, tv.id)
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (4.167f * w).toInt()
        })
        addView(rlNoData, LayoutParams(-1, (31.389f * w).toInt()).apply {
            rightMargin = (2.22f * w).toInt()
            leftMargin = (2.22f * w).toInt()
        })

        rcv = RecyclerView(context).apply { isVerticalScrollBarEnabled = false }
        addView(rcv, -1, -1)

        viewLoading = LottieAnimationView(context).apply {
            visibility = GONE
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