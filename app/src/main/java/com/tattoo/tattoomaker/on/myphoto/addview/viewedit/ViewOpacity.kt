package com.tattoo.tattoomaker.on.myphoto.addview.viewedit

import android.annotation.SuppressLint
import android.content.Context
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom
import com.tattoo.tattoomaker.on.myphoto.viewcustom.CustomSeekbar

@SuppressLint("ResourceType")
class ViewOpacity(context: Context) : RelativeLayout(context) {

    companion object {
        var w = 0
    }

    val sbOpacity: CustomSeekbar
    val tvOpacity: TextView

    init {
        w = resources.displayMetrics.widthPixels

        tvOpacity = TextView(context).apply {
            id = 1222
            textCustom(
                resources.getString(R.string.opacity),
                ContextCompat.getColor(context, R.color.white),
                3.33f * w / 100,
                "solway_regular",
                context
            )
        }
        addView(tvOpacity, LayoutParams(-2, -2).apply {
            leftMargin = (2.5f * w / 100).toInt()
        })

        sbOpacity = CustomSeekbar(context).apply { setMax(100) }
        addView(sbOpacity, LayoutParams(-1, (4.44f * w / 100).toInt()).apply {
            addRule(RIGHT_OF, tvOpacity.id)
            leftMargin = (3.889f * w / 100).toInt()
            rightMargin = (5.556f * w / 100).toInt()
        })
    }
}