package com.tattoo.tattoomaker.on.myphoto.addview.viewdialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.renderer.FlowerColorWheelRenderer
import com.flask.colorpicker.slider.AlphaSlider
import com.flask.colorpicker.slider.LightnessSlider
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom

@SuppressLint("ResourceType")
class ViewDialogColor(context: Context) : RelativeLayout(context) {

    companion object {
        var w = 0
    }

    val colorPicker: ColorPickerView
    val lightnessSlide: LightnessSlider
    val alphaSlide: AlphaSlider

    val tvOk: TextView
    val tvCancel: TextView

    init {
        w = resources.displayMetrics.widthPixels
        setBackgroundResource(R.drawable.boder_dialog)

        val tvTitle = TextView(context).apply {
            id = 1221
            textCustom(
                resources.getString(R.string.color),
                ContextCompat.getColor(context, R.color.white),
                4.44f * w / 100,
                "solway_medium",
                context
            )
        }
        addView(tvTitle, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL)
            topMargin = (5.556f * w / 100).toInt()
        })

        lightnessSlide = LightnessSlider(context).apply { id = 1223 }

        alphaSlide = AlphaSlider(context)

        colorPicker = ColorPickerView(context).apply {
            id = 1225
            setAlphaSlider(alphaSlide)
            setLightnessSlider(lightnessSlide)
            setDensity(15)
            setRenderer(FlowerColorWheelRenderer())
        }
        addView(colorPicker,  LayoutParams((48.61f * w / 100).toInt(), (48.61f * w / 100).toInt()).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(BELOW, tvTitle.id)
            topMargin = (3.33f * w / 100).toInt()
        })

        addView(lightnessSlide, LayoutParams(-1, (4.44f * w / 100).toInt()).apply {
            addRule(BELOW, colorPicker.id)
            topMargin = (1.94f * w / 100).toInt()
            leftMargin = (5f * w / 100).toInt()
            rightMargin = (5f * w / 100).toInt()
        })

        addView(alphaSlide, LayoutParams(-1, (4.44f * w / 100).toInt()).apply {
            addRule(BELOW, lightnessSlide.id)
            topMargin = (2.778f * w / 100).toInt()
            leftMargin = (5f * w / 100).toInt()
            rightMargin = (5f * w / 100).toInt()
        })

        val ll = LinearLayout(context).apply {
            id = 1778
            orientation = LinearLayout.HORIZONTAL
        }

        tvCancel = TextView(context).apply {
            textCustom(
                resources.getString(R.string.cancel),
                ContextCompat.getColor(context, R.color.red),
                4.44f * w / 100,
                "solway_medium",
                context
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvCancel, LinearLayout.LayoutParams(-1, -1, 1F))

        val v = View(context).apply { setBackgroundColor(ContextCompat.getColor(context, R.color.gray_star)) }
        ll.addView(v, LinearLayout.LayoutParams((0.138f * w / 100).toInt(), -1))

        tvOk = TextView(context).apply {
            textCustom(
                resources.getString(R.string.ok),
                ContextCompat.getColor(context, R.color.blue),
                4.44f * w / 100,
                "solway_medium",
                context
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvOk, LinearLayout.LayoutParams(-1, -1, 1F))

        addView(ll, LayoutParams(-1, (12.22f * w / 100).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
        })

        val v2 = View(context).apply { setBackgroundColor(ContextCompat.getColor(context, R.color.gray_star)) }
        addView(v2, LayoutParams(-1, (0.138f * w / 100).toInt()).apply {
            addRule(ABOVE, ll.id)
        })
    }
}