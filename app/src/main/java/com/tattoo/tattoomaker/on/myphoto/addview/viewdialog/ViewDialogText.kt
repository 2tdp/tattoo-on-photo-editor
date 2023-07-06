package com.tattoo.tattoomaker.on.myphoto.addview.viewdialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom

@SuppressLint("ResourceType")
class ViewDialogText(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val tv: TextView
    val tvYes: TextView
    val tvCancel: TextView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundResource(R.drawable.boder_dialog_white)

        tv = TextView(context).apply {
            textCustom(
                "",
                ContextCompat.getColor(context, R.color.black_text),
                4.44f * w,
                "solway_medium",
                context
            )
            gravity = Gravity.CENTER
        }
        addView(tv, LayoutParams(-2, -2).apply {
            leftMargin = (4.44f * w).toInt()
            rightMargin = (4.44f * w).toInt()
            topMargin = (5.556f * w).toInt()
        })

        val ll = LinearLayout(context).apply {
            id = 1221
            orientation = LinearLayout.HORIZONTAL
        }

        tvCancel = TextView(context).apply {
            textCustom(
                "",
                ContextCompat.getColor(context, R.color.red),
                4.44f * w,
                "solway_medium",
                context
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvCancel, LinearLayout.LayoutParams(-1, -1, 1F))

        val v = View(context).apply { setBackgroundColor(ContextCompat.getColor(context, R.color.black_line)) }
        ll.addView(v, LinearLayout.LayoutParams((0.138f * w).toInt(), -1))

        tvYes = TextView(context).apply {
            textCustom(
                "",
                ContextCompat.getColor(context, R.color.blue),
                4.44f * w,
                "solway_medium",
                context
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvYes, LinearLayout.LayoutParams(-1, -1, 1F))

        addView(ll, LayoutParams(-1, (12.22f * w).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
        })

        val v2 = View(context).apply { setBackgroundColor(ContextCompat.getColor(context, R.color.black_line)) }
        addView(v2, LayoutParams(-1, (0.138f * w).toInt()).apply {
            addRule(ABOVE, ll.id)
        })
    }
}