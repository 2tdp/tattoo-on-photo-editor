package com.tattoo.tattoomaker.on.myphoto.addview.viewpremium

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom

@SuppressLint("ResourceType")
class ViewPremium(context: Context) : RelativeLayout(context) {

    companion object {
        var w = 0
    }

    val ivExit: ImageView
    val tvContinue: TextView

    init {
        w = resources.displayMetrics.widthPixels

        val ivBg = ImageView(context).apply {
            scaleType = ImageView.ScaleType.FIT_XY
            setImageResource(R.drawable.im_bg_premium_1)
        }
        addView(ivBg, -1, -1)

        ivExit = ImageView(context).apply { setImageResource(R.drawable.ic_exit) }
        addView(ivExit, LayoutParams((6.667f * w / 100).toInt(), (6.667f * w / 100).toInt()).apply {
            topMargin = (15.833f * w / 100).toInt()
            leftMargin = (5.556f * w / 100).toInt()
        })

        val ivTitle = ImageView(context).apply {
            id = 1221
            setImageResource(R.drawable.im_splash_1)
        }
        addView(ivTitle, LayoutParams((31.94f * w / 100).toInt(), (38.61f * w / 100).toInt()).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (10.83f * w / 100).toInt()
        })

        val tvTitle = TextView(context).apply {
            id = 1222
            textCustom(
                resources.getString(R.string.start_like_a_pro).uppercase(),
                ContextCompat.getColor(context, R.color.white),
                5.556f * w / 100,
                "solway_rooters",
                context
            )
        }
        addView(tvTitle, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(BELOW, ivTitle.id)
            topMargin = (1.389f * w / 100).toInt()
        })

        val tvDes = TextView(context).apply {
            textCustom(
                resources.getString(R.string.unlock_all_feature),
                ContextCompat.getColor(context, R.color.white),
                3.333f * w / 100,
                "solway_regular",
                context
            )
        }
        val paramsTextDes = LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(BELOW, tvTitle.id)
        }
        addView(tvDes, paramsTextDes)

        val iv = ImageView(context).apply { setImageResource(R.drawable.im_premium_1) }
        addView(iv, LayoutParams(-1, (70.83f * w / 100).toInt()).apply {
            addRule(CENTER_IN_PARENT, TRUE)
            leftMargin = (4.44f * w / 100).toInt()
            rightMargin = (4.44f * w / 100).toInt()
        })

        val tvDes2 = TextView(context).apply {
            id = 1223
            textCustom(
                resources.getString(R.string.subscription_will_auto_renew_cancel_anytime),
                ContextCompat.getColor(context, R.color.gray_light_2),
                3.33f * w / 100,
                "solway_bold",
                context
            )
        }
        addView(tvDes2, LayoutParams(-2, -2).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
            addRule(CENTER_HORIZONTAL, TRUE)
            bottomMargin = (11.94f * w / 100).toInt()
        })

        tvContinue = TextView(context).apply {
            background = Utils.createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.color_main)),
                (2.5f * w / 100).toInt(),
                (0.55f * w / 100).toInt(),
                ContextCompat.getColor(context, R.color.white)
            )
            textCustom(
                resources.getString(R.string.continue_text),
                ContextCompat.getColor(context, R.color.white),
                4.44f * w / 100,
                "solway_medium",
                context
            )
            gravity = android.view.Gravity.CENTER
        }
        addView(tvContinue, LayoutParams(-1, (14.722f * w / 100).toInt()).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(ABOVE, tvDes2.id)
            leftMargin = (28.33f * w / 100).toInt()
            rightMargin = (28.33f * w / 100).toInt()
            bottomMargin = (3.33f * w / 100).toInt()
        })
    }
}