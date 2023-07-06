package com.tattoo.tattoomaker.on.myphoto.addview.viewitem

import android.content.Context
import android.widget.ImageView
import android.widget.RelativeLayout
import com.makeramen.roundedimageview.RoundedImageView
import com.tattoo.tattoomaker.on.myphoto.R

class ViewItemProject(context: Context): RelativeLayout(context) {

    companion object{
        var w = 0F
    }

    val iv: RoundedImageView
    val ivTick: ImageView

    init {
        w = resources.displayMetrics.widthPixels / 100F

        iv = RoundedImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            cornerRadius = 2.5f * w
        }
        addView(iv, LayoutParams(-1, -1))

        ivTick = ImageView(context).apply {
            visibility = GONE
            setImageResource(R.drawable.ic_item_un_tick)
        }
        addView(ivTick, LayoutParams((5.556f * w).toInt(), (5.556f * w).toInt()).apply {
            addRule(ALIGN_PARENT_END, TRUE)
            rightMargin = (2.22f * w).toInt()
            topMargin = (2.22f * w).toInt()
        })
    }
}