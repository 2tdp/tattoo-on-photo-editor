package com.tattoo.tattoomaker.on.myphoto.addview.viewdialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.viewcustom.SimpleRatingBar

@SuppressLint("ResourceType")
class ViewDialogRattingApp(context: Context) : RelativeLayout(context) {

    companion object {
        var w = 0
    }

    val ratingBar: SimpleRatingBar
    val tvRateUs: TextView
    val tvCancel: TextView

    init {
        w = resources.displayMetrics.widthPixels
        setBackgroundResource(R.drawable.boder_dialog_white)

        val tvTitle = TextView(context).apply {
            id = 1222
            textCustom(
                resources.getString(R.string.rate_app),
                ContextCompat.getColor(context, R.color.black_text),
                4.44f * w / 100,
                "solway_medium",
                context
            )
        }
        addView(tvTitle, LayoutParams(-2, -2).apply {
            topMargin = (5.556f * w / 100).toInt()
            addRule(CENTER_HORIZONTAL, TRUE)
        })

        val tvDes = TextView(context).apply {
            id = 1223
            textCustom(
                resources.getString(R.string.do_you_love_this_app),
                ContextCompat.getColor(context, R.color.black_text),
                3.889f * w / 100,
                "solway_regular",
                context
            )
        }
        addView(tvDes, LayoutParams(-2, -2).apply {
            topMargin = (0.556f * w / 100).toInt()
            addRule(BELOW, tvTitle.id)
            addRule(CENTER_HORIZONTAL, TRUE)
        })

        ratingBar = SimpleRatingBar(context).apply {
            id = 1224
            numberOfStars = 5
            rating = 0f
            stepSize = 0.1f
            starSize = 10f * w / 100
            starCornerRadius = 3.5f * w / 100
            fillColor = ContextCompat.getColor(context, R.color.gray_star)
            pressedStarBackgroundColor = ContextCompat.getColor(context, R.color.gold)
            pressedFillColor = ContextCompat.getColor(context, R.color.gray_star)
            starBackgroundColor = ContextCompat.getColor(context, R.color.gold)

        }
        addView(ratingBar, LayoutParams(-1, 10 * w / 100).apply {
            addRule(CENTER_HORIZONTAL)
            addRule(BELOW, tvDes.id)
            topMargin = (3.33f * w / 100).toInt()
        })

        val ll = LinearLayout(context).apply {
            id = 1221
            orientation = LinearLayout.HORIZONTAL
        }

        tvCancel = TextView(context).apply {
            textCustom(
                "",
                ContextCompat.getColor(context, R.color.red),
                4.44f * w / 100,
                "solway_medium",
                context
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvCancel, LinearLayout.LayoutParams(-1, -1, 1F))

        val v = View(context).apply { setBackgroundColor(ContextCompat.getColor(context, R.color.black_line)) }
        ll.addView(v, LinearLayout.LayoutParams((0.138f * w / 100).toInt(), -1))

        tvRateUs = TextView(context).apply {
            textCustom(
                "",
                ContextCompat.getColor(context, R.color.blue),
                4.44f * w / 100,
                "solway_medium",
                context
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvRateUs, LinearLayout.LayoutParams(-1, -1, 1F))

        addView(ll, LayoutParams(-1, (12.22f * w / 100).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
        })

        val v2 = View(context).apply { setBackgroundColor(ContextCompat.getColor(context, R.color.black_line)) }
        addView(v2, LayoutParams(-1, (0.138f * w / 100).toInt()).apply {
            addRule(ABOVE, ll.id)
        })
    }
}