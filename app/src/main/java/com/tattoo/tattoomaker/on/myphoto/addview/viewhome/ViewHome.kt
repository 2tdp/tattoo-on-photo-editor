package com.tattoo.tattoomaker.on.myphoto.addview.viewhome

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom

@SuppressLint("ResourceType")
class ViewHome(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val ivPremium : ImageView
    val tvCreate: TextView
    val tvMyStore: TextView
    val tvSettings: TextView

    init {
        w = resources.displayMetrics.widthPixels / 100F

        val ivBg = ImageView(context).apply {
            setImageResource(R.drawable.im_bg_home)
            scaleType = ImageView.ScaleType.FIT_XY
        }
        addView(ivBg, -1, -1)

        ivPremium = ImageView(context).apply {
            id = 1221
            setImageResource(R.drawable.ic_vip)
        }
        addView(ivPremium, LayoutParams((8.889f * w).toInt(), (8.889f * w).toInt()).apply {
            addRule(ALIGN_PARENT_END, TRUE)
            topMargin = (15f * w).toInt()
            rightMargin = (4.167f * w).toInt()
        })

        val iv = ImageView(context).apply { setImageResource(R.drawable.im_splash_2) }
        addView(iv, LayoutParams(-1, (33.889f * w).toInt()).apply {
            addRule(BELOW, ivPremium.id)
            topMargin = (22.22f * w).toInt()
            leftMargin = (5.556f * w).toInt()
            rightMargin = (5.556f * w).toInt()
        })

        tvSettings = TextView(context).apply {
            id = 1222
            textCustom(
                resources.getString(R.string.settings).uppercase(),
                ContextCompat.getColor(context, R.color.white),
                5.556f * w,
                "solway_rooters",
                context
            )
            background = Utils.createBackground(
                intArrayOf(
                    ContextCompat.getColor(context, R.color.main_color_1),
                    ContextCompat.getColor(context, R.color.main_color_2)
                ), (2.5f * w).toInt(),
                (0.55f * w).toInt(), ContextCompat.getColor(context, R.color.white)
            )
            gravity = Gravity.CENTER
        }
        addView(tvSettings, LayoutParams(-1, (16.11f * w).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
            bottomMargin = (25f * w).toInt()
            leftMargin = (16.389f * w).toInt()
            rightMargin = (16.389f * w).toInt()
        })

        tvMyStore = TextView(context).apply {
            id = 1223
            textCustom(
                resources.getString(R.string.my_story).uppercase(),
                ContextCompat.getColor(context, R.color.white),
                5.556f * w,
                "solway_rooters",
                context
            )
            background = Utils.createBackground(
                intArrayOf(
                    ContextCompat.getColor(context, R.color.main_color_1),
                    ContextCompat.getColor(context, R.color.main_color_2)
                ), (2.5f * w).toInt(),
                (0.55f * w).toInt(), ContextCompat.getColor(context, R.color.white)
            )
            gravity = Gravity.CENTER
        }
        addView(tvMyStore, LayoutParams(-1, (16.11f * w).toInt()).apply {
            addRule(ABOVE, tvSettings.id)
            bottomMargin = (10f * w).toInt()
            leftMargin = (16.389f * w).toInt()
            rightMargin = (16.389f * w).toInt()
        })

        tvCreate = TextView(context).apply {
            id = 1224
            textCustom(
                resources.getString(R.string.create).uppercase(),
                ContextCompat.getColor(context, R.color.white),
                5.556f * w,
                "solway_rooters",
                context
            )
            background = Utils.createBackground(
                intArrayOf(
                    ContextCompat.getColor(context, R.color.main_color_1),
                    ContextCompat.getColor(context, R.color.main_color_2)
                ), (2.5f * w).toInt(),
                (0.55f * w).toInt(), ContextCompat.getColor(context, R.color.white)
            )
            gravity = Gravity.CENTER
        }
        addView(tvCreate, LayoutParams(-1, (16.11f * w).toInt()).apply {
            addRule(ABOVE, tvMyStore.id)
            bottomMargin = (10f * w).toInt()
            leftMargin = (16.389f * w).toInt()
            rightMargin = (16.389f * w).toInt()
        })
    }
}