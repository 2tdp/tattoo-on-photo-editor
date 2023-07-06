package com.tattoo.tattoomaker.on.myphoto.utils

import android.content.Context
import android.util.TypedValue
import android.widget.TextView

object UtilsView {

    fun TextView.textCustom(content: String, color: Int, textSize: Float, font: String, context: Context) {
        text = content
        setTextColor(color)
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        typeface = Utils.getTypeFace(font.substring(0, 6), "$font.ttf", context)
    }
}