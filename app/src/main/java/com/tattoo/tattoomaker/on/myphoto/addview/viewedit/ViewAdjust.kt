package com.tattoo.tattoomaker.on.myphoto.addview.viewedit

import android.annotation.SuppressLint
import android.content.Context
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom
import com.tattoo.tattoomaker.on.myphoto.viewcustom.CustomSeekbarTwoWay
import com.tattoo.tattoomaker.on.myphoto.viewcustom.SimpleRatingBar.Gravity

@SuppressLint("ResourceType")
class ViewAdjust(context: Context): RelativeLayout(context) {

    companion object{
        var w = 0F
    }

    val tvTitle: TextView
    val sbTwoWay: CustomSeekbarTwoWay

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(ContextCompat.getColor(context, R.color.black_bottom2))

        tvTitle = TextView(context).apply {
            id = 1221
            textCustom(
                "",
                ContextCompat.getColor(context, R.color.white),
                3.33f * w,
                "solway_regular",
                context
            )
            gravity = android.view.Gravity.CENTER
        }
        addView(tvTitle, LayoutParams((17.778f * w).toInt(), -2).apply {
            addRule(CENTER_VERTICAL, TRUE)
            leftMargin = (2.778f * w).toInt()
        })

        sbTwoWay = CustomSeekbarTwoWay(context).apply { setMax(100) }
        addView(sbTwoWay, LayoutParams(-1, (4.44f * w).toInt()).apply {
            addRule(CENTER_VERTICAL, TRUE)
            addRule(RIGHT_OF, tvTitle.id)
            leftMargin = (4.44f * w).toInt()
            rightMargin = (5.556f * w).toInt()
        })
    }
}