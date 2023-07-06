package com.tattoo.tattoomaker.on.myphoto.addview.viewedit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom
import com.tattoo.tattoomaker.on.myphoto.viewcustom.CustomSeekbar

@SuppressLint("ResourceType")
class ViewColor(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val ivPickColor: ImageView
    val rcv: RecyclerView
    val vOpacity: ViewOpacity

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(ContextCompat.getColor(context, R.color.black_bottom2))

        val rl = RelativeLayout(context).apply { id = 1221 }
        ivPickColor = ImageView(context).apply {
            id = 1222
            setImageResource(R.drawable.ic_pick_color)
        }
        rl.addView(ivPickColor, LayoutParams((7.778f * w).toInt(), (7.778f * w).toInt()).apply {
            leftMargin = (2.778f * w).toInt()
        })

        rcv = RecyclerView(context).apply { isHorizontalScrollBarEnabled = false }
        rl.addView(rcv, LayoutParams(-1, (7.778f * w).toInt()).apply {
            addRule(RIGHT_OF, ivPickColor.id)
        })
        addView(rl, LayoutParams(-1, (7.778f * w).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
            bottomMargin = (2.778f * w).toInt()
        })

        vOpacity = ViewOpacity(context)
        addView(vOpacity, LayoutParams(-1, (4.44f * w).toInt()).apply {
            addRule(ABOVE, rl.id)
            bottomMargin = (3.889f * w).toInt()
        })
    }
}