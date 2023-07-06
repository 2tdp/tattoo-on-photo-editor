package com.tattoo.tattoomaker.on.myphoto.addview.viewedit

import android.annotation.SuppressLint
import android.content.Context
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import codes.side.andcolorpicker.hsl.HSLColorPickerSeekBar
import com.tattoo.tattoomaker.on.myphoto.R

@SuppressLint("ResourceType")
class ViewColorPremium(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val sbColor: HSLColorPickerSeekBar
    val vOpacity: ViewOpacity

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(ContextCompat.getColor(context, R.color.black_bottom2))

        sbColor = HSLColorPickerSeekBar(context).apply {
            id = 1221
            mode = HSLColorPickerSeekBar.Mode.MODE_HUE
            coloringMode = HSLColorPickerSeekBar.ColoringMode.PURE_COLOR
        }
        val paramsColor = LayoutParams(-1, (6.667f * w).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
            bottomMargin = (5.556f * w).toInt()
        }
        addView(sbColor, paramsColor)

        vOpacity = ViewOpacity(context)
        addView(vOpacity, LayoutParams(-1, (4.44f * w).toInt()).apply {
            addRule(ABOVE, sbColor.id)
            bottomMargin = (3.889f * w).toInt()
        })
    }
}