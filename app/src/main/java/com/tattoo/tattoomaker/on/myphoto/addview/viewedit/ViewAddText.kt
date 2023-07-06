package com.tattoo.tattoomaker.on.myphoto.addview.viewedit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.addview.ViewSettings
import com.tattoo.tattoomaker.on.myphoto.addview.ViewToolbar
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsDrawable
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom
import com.tattoo.tattoomaker.on.myphoto.viewcustom.SimpleRatingBar.Gravity

@SuppressLint("ResourceType")
class ViewAddText(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val viewToolbar: ViewToolbar
    val vAddText: EditText
    val rcv: RecyclerView

    init {
        w = resources.displayMetrics.widthPixels / 100f
        setBackgroundColor(Color.WHITE)
        isClickable = true
        isFocusable = true

        viewToolbar = ViewToolbar(context).apply {
            id = 1221
            UtilsDrawable.changeIcon(
                context, "back", 1, R.drawable.ic_back, ivBack,
                ContextCompat.getColor(context, R.color.black_text),
                ContextCompat.getColor(context, R.color.black_text)
            )
            ivRight.setImageResource(R.drawable.ic_tick)
            tvTitle.text = resources.getString(R.string.text)
        }
        addView(viewToolbar, LayoutParams(-1, -2).apply {
            topMargin = (13.61f * w).toInt()
        })

        vAddText = EditText(context).apply {
            id = 1222
            hint = resources.getString(R.string.tattoo)
            setHintTextColor(Color.parseColor("#A6A6A6"))
            textCustom(
                "", ContextCompat.getColor(context, R.color.black_text),
                5.556f * w, "solway_medium", context
            )
            background = Utils.createBackground(
                intArrayOf(Color.TRANSPARENT), (1.5f * w).toInt(),
                (0.34f * w).toInt(), ContextCompat.getColor(context, R.color.black_text)
            )
            gravity = android.view.Gravity.CENTER
        }
        addView(vAddText, LayoutParams(-1, (44.4f * w).toInt()).apply {
            addRule(BELOW, viewToolbar.id)
            topMargin = (7.778f * w).toInt()
            leftMargin = (4.44f * w).toInt()
            rightMargin = (4.44f * w).toInt()
        })

        rcv = RecyclerView(context).apply { isVerticalScrollBarEnabled = false }
        addView(rcv, LayoutParams(-1, -1).apply {
            addRule(BELOW, vAddText.id)
            topMargin = (4.44f * w).toInt()
            leftMargin = (2.22f * w).toInt()
            rightMargin = (2.22f * w).toInt()
        })
    }
}