package com.tattoo.tattoomaker.on.myphoto.addview.viewedit

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tattoo.tattoomaker.on.myphoto.R

@SuppressLint("ResourceType")
class ViewRecycle(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val ivNone: ImageView
    val rcv: RecyclerView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(ContextCompat.getColor(context, R.color.black_bottom2))

        ivNone = ImageView(context).apply {
            id = 1221
            setImageResource(R.drawable.ic_none_item)
        }
        addView(ivNone, LayoutParams((7.778f * w).toInt(), (10.056f * w).toInt()).apply {
            addRule(CENTER_VERTICAL, TRUE)
            leftMargin = (2.778f * w).toInt()
        })

        rcv = RecyclerView(context).apply { isHorizontalScrollBarEnabled = false }
        addView(rcv, LayoutParams(-1, (10.556f * w).toInt()).apply {
            addRule(CENTER_VERTICAL, TRUE)
            addRule(RIGHT_OF, ivNone.id)
        })
    }
}