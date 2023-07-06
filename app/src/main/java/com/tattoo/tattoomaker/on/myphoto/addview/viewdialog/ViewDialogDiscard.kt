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
class ViewDialogDiscard(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val tvSave: TextView
    val tvDiscard: TextView
    val tvCancel: TextView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundResource(R.drawable.boder_dialog_white)

        val tvTitle = TextView(context).apply {
            id = 1221
            textCustom(
                resources.getString(R.string.discard),
                ContextCompat.getColor(context, R.color.black_text),
                4.44f * w, "solway_medium", context
            )
        }
        addView(tvTitle, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (5.556f * w).toInt()
        })

        val tvDes = TextView(context).apply {
            textCustom(
                resources.getString(R.string.do_you_want_to_discard_creating),
                ContextCompat.getColor(context, R.color.black_text),
                3.889f * w, "solway_regular", context
            )
        }
        addView(tvDes, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(BELOW, tvTitle.id)
        })

        val ll = LinearLayout(context).apply {
            id = 1222
            orientation = LinearLayout.HORIZONTAL
        }

        tvCancel = TextView(context).apply {
            textCustom(
                resources.getString(R.string.cancel),
                ContextCompat.getColor(context, R.color.red),
                4.44f * w,
                "solway_medium",
                context
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvCancel, LinearLayout.LayoutParams(-1, -1, 1F))

        val v1 = View(context).apply { setBackgroundColor(ContextCompat.getColor(context, R.color.black_line)) }
        ll.addView(v1, LinearLayout.LayoutParams((0.138f * w).toInt(), -1))

        tvDiscard = TextView(context).apply {
            textCustom(
                resources.getString(R.string.discard),
                ContextCompat.getColor(context, R.color.blue),
                4.44f * w,
                "solway_medium",
                context
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvDiscard, LinearLayout.LayoutParams(-1, -1, 1F))

        val v2 = View(context).apply { setBackgroundColor(ContextCompat.getColor(context, R.color.black_line)) }
        ll.addView(v2, LinearLayout.LayoutParams((0.138f * w).toInt(), -1))

        tvSave = TextView(context).apply {
            textCustom(
                resources.getString(R.string.save),
                ContextCompat.getColor(context, R.color.blue),
                4.44f * w,
                "solway_medium",
                context
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvSave, LinearLayout.LayoutParams(-1, -1, 1F))

        addView(ll, LayoutParams(-1, (12.22f * w).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
        })

        val v3 = View(context).apply { setBackgroundColor(ContextCompat.getColor(context, R.color.black_line)) }
        addView(v3, LayoutParams(-1, (0.138f * w).toInt()).apply {
            addRule(ABOVE, ll.id)
        })
    }
}