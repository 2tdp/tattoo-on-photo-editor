package com.tattoo.tattoomaker.on.myphoto.addview.viewitem

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom

@SuppressLint("ResourceType")
class ViewItemBottomEdit(context: Context) : RelativeLayout(context) {

    companion object {
        var w = 0
    }

    val iv: ImageView
    val tv: TextView

    init {
        w = resources.displayMetrics.widthPixels

        iv = ImageView(context).apply {
            id = 1991
            setPadding((2f * w / 100).toInt())
        }
        addView(iv, LayoutParams((11.667f * w / 100).toInt(), (11.667f * w / 100).toInt()).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
        })

        tv = TextView(context).apply {
            textCustom(
                "",
                ContextCompat.getColor(context, R.color.white),
                3.33f * w / 100,
                "solway_regular",
                context
            )
        }
        addView(tv, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(BELOW, iv.id)
            topMargin = (1.667f * w / 100).toInt()
        })
    }
}