package com.tattoo.tattoomaker.on.myphoto.addview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsDrawable
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom

@SuppressLint("ResourceType")
class ViewSettings(context: Context) : RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val viewToolbar: ViewToolbar
    val tvPremium: TextView
    val viewMoreApp: ViewItemSetting
    val viewRateUs: ViewItemSetting
    val viewShareApp: ViewItemSetting
    val viewFeedBack: ViewItemSetting
    val viewPP: ViewItemSetting

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(Color.WHITE)

        viewToolbar = ViewToolbar(context).apply {
            id = 1221
            UtilsDrawable.changeIcon(
                context, "back", 1, R.drawable.ic_back, ivBack,
                ContextCompat.getColor(context, R.color.black_text),
                ContextCompat.getColor(context, R.color.black_text)
            )
            ivRight.visibility = GONE
            tvTitle.text = resources.getString(R.string.settings)
        }
        addView(viewToolbar, LayoutParams(-1, -2).apply {
            topMargin = (13.61f * w).toInt()
        })

        val ivBg = ImageView(context).apply {
            id = 1222
            setImageResource(R.drawable.im_bg_settings)
            scaleType = ImageView.ScaleType.FIT_XY
        }
        addView(ivBg, LayoutParams(-1, (43.89f * w).toInt()).apply {
            addRule(BELOW, viewToolbar.id)
            topMargin = (6.667f * w).toInt()
            leftMargin = (4.44f * w).toInt()
            rightMargin = (4.44f * w).toInt()
        })

        tvPremium = TextView(context).apply {
            textCustom(
                resources.getString(R.string.try_premium).uppercase(),
                ContextCompat.getColor(context, R.color.white),
                3.33f * w,
                "solway_rooters",
                context
            )
            gravity = Gravity.CENTER
            background = Utils.createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.color_main)),
                (2.5f * w).toInt(), (0.55f * w).toInt(),
                ContextCompat.getColor(context, R.color.white)
            )
        }
        addView(tvPremium, LayoutParams((41.11f * w).toInt(), (9.72f * w).toInt()).apply {
            addRule(ALIGN_PARENT_END, TRUE)
            rightMargin = (7.778f * w).toInt()
            topMargin = (53.889f * w).toInt()
        })

        viewMoreApp = ViewItemSetting(context).apply {
            id = 1223
            iv.setImageResource(R.drawable.ic_more_app)
            tv.text = resources.getString(R.string.more_app)
        }
        addView(viewMoreApp, LayoutParams(-1, (8.889f * w).toInt()).apply {
            addRule(BELOW, ivBg.id)
            topMargin = (7.778f * w).toInt()
        })

        viewRateUs = ViewItemSetting(context).apply {
            id = 1224
            iv.setImageResource(R.drawable.ic_rate_app)
            tv.text = resources.getString(R.string.rate_us)
        }
        val paramsRateUs = LayoutParams(-1, (8.889f * w).toInt()).apply {
            addRule(BELOW, viewMoreApp.id)
            topMargin = (6.667f * w).toInt()
        }
        addView(viewRateUs, paramsRateUs)

        viewShareApp = ViewItemSetting(context).apply {
            id = 1225
            iv.setImageResource(R.drawable.ic_share_app)
            tv.text = resources.getString(R.string.share_app)
        }
        val paramsShareApp = LayoutParams(-1, (8.889f * w).toInt()).apply {
            addRule(BELOW, viewRateUs.id)
            topMargin = (6.667f * w).toInt()
        }
        addView(viewShareApp, paramsShareApp)

        viewFeedBack = ViewItemSetting(context).apply {
            id = 1226
            iv.setImageResource(R.drawable.ic_feedback)
            tv.text = resources.getString(R.string.feedback)
        }
        val paramsFeedback = LayoutParams(-1, (8.889f * w).toInt()).apply {
            addRule(BELOW, viewShareApp.id)
            topMargin = (6.667f * w).toInt()
        }
        addView(viewFeedBack, paramsFeedback)

        viewPP = ViewItemSetting(context).apply {
            id = 1227
            iv.setImageResource(R.drawable.ic_pp)
            tv.text = resources.getString(R.string.pp)
        }
        val paramsPP = LayoutParams(-1, (8.889f * w).toInt()).apply {
            addRule(BELOW, viewFeedBack.id)
            topMargin = (6.667f * w).toInt()
        }
        addView(viewPP, paramsPP)

    }

    class ViewItemSetting(context: Context) : RelativeLayout(context) {

        val iv: ImageView
        val tv: TextView

        init {
            iv = ImageView(context).apply { id = 1221 }
            addView(iv, LayoutParams((8.889f * w).toInt(), -1).apply {
                leftMargin = (4.44f * w).toInt()
            })

            tv = TextView(context).apply {
                textCustom(
                    "",
                    ContextCompat.getColor(context, R.color.black_text2),
                    4.44f * w,
                    "solway_regular",
                    context
                )
            }
            addView(tv, LayoutParams(-2, -2).apply {
                addRule(CENTER_VERTICAL, TRUE)
                addRule(RIGHT_OF, iv.id)
                leftMargin = (4.44f * w).toInt()
            })

            val ivRight = ImageView(context).apply { setImageResource(R.drawable.ic_right) }
            addView(ivRight, LayoutParams((5.556f * w).toInt(), (5.556f * w).toInt()).apply {
                addRule(ALIGN_PARENT_END, TRUE)
                addRule(CENTER_VERTICAL, TRUE)
                rightMargin = (4.44f * w).toInt()
            })
        }
    }
}