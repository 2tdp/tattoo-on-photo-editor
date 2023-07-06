package com.tattoo.tattoomaker.on.myphoto.addview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom
import com.tattoo.tattoomaker.on.myphoto.viewcustom.CustomSeekbarLoading

@SuppressLint("ResourceType")
class ViewSplash(context: Context): RelativeLayout(context) {
    companion object {
        var w = 0
    }

    val customSeekbarLoading: CustomSeekbarLoading

    init {
        w = resources.displayMetrics.widthPixels
        setBackgroundColor(Color.BLACK)

        val ll = LinearLayout(context).apply { orientation = LinearLayout.VERTICAL }

        val ivBackground1 = ImageView(context).apply {
            setImageResource(R.drawable.im_splash_1)
            scaleType = ImageView.ScaleType.FIT_XY
        }
        ll.addView(ivBackground1, -1, (108.33f * w / 100).toInt())

        val ivBackground2 = ImageView(context).apply {
            setImageResource(R.drawable.im_splash_2)
            scaleType = ImageView.ScaleType.FIT_XY
        }
        ll.addView(ivBackground2, -1, (33.889f * w / 100).toInt())

        addView(ll, LayoutParams(-1, -2).apply {
            leftMargin = (5f * w / 100).toInt()
            rightMargin = (5f * w / 100).toInt()
            addRule(CENTER_IN_PARENT, TRUE)
        })

        val tv = TextView(context).apply {
            id = 1221
            textCustom(
                resources.getString(R.string.this_action_may_contain_ads),
                ContextCompat.getColor(context, R.color.white),
                3.889f * w / 100,
                "solway_regular",
                context
            )
        }
        addView(tv, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
            bottomMargin = (6.22f * w / 100).toInt()
        })

        customSeekbarLoading = CustomSeekbarLoading(context).apply {
            setColorStroke(ContextCompat.getColor(context, R.color.white))
            setColorPro(
                ContextCompat.getColor(context, R.color.main_color_1),
                ContextCompat.getColor(context, R.color.main_color_2))
            setColorText(ContextCompat.getColor(context, R.color.white))
            setTextSize(5.556f * w / 100)
            setSizeStroke(w / 100f)
            setProgress(0)
        }
        addView(customSeekbarLoading, LayoutParams(-1, (12.22f * w / 100).toInt()).apply {
            addRule(ABOVE, tv.id)
            bottomMargin = (4.22f * w / 100).toInt()
            leftMargin = (4.44f * w / 100).toInt()
            rightMargin = (4.44f * w / 100).toInt()
        })
    }
}