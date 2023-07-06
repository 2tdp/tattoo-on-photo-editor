package com.tattoo.tattoomaker.on.myphoto.addview

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom

@SuppressLint("ResourceType")
class ViewToolbar(context: Context) : RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val ivBack: ImageView
    val tvTitle: TextView
    val ivRight: ImageView
    val ivRight2: ImageView
    val ll: LinearLayout
    val ivUndo: ImageView
    val ivRedo: ImageView

    init {
        w = resources.displayMetrics.widthPixels / 100F

        ivBack = ImageView(context).apply {
            setImageResource(R.drawable.ic_back)
            setPadding(w.toInt())
        }
        addView(ivBack, LayoutParams((8.33f * w).toInt(), (8.33f * w).toInt()).apply {
            leftMargin = (4.44f * w).toInt()
        })

        tvTitle = TextView(context).apply {
            textCustom(
                "",
                ContextCompat.getColor(context, R.color.black_text),
                5.556f * w,
                "solway_medium",
                context
            )
        }
        addView(tvTitle, LayoutParams(-2, -2).apply { addRule(CENTER_IN_PARENT, TRUE) })

        ll = LinearLayout(context).apply {
            visibility = GONE
            orientation = LinearLayout.HORIZONTAL
        }
        ivUndo = ImageView(context).apply { setImageResource(R.drawable.ic_undo) }
        ll.addView(ivUndo, LinearLayout.LayoutParams(-1, -1, 1F).apply {
            rightMargin = (2.778f * w).toInt()
        })

        ivRedo = ImageView(context).apply { setImageResource(R.drawable.ic_redo) }
        ll.addView(ivRedo, LinearLayout.LayoutParams(-1, -1, 1F).apply {
            leftMargin = (2.778f * w).toInt()
        })

        addView(ll, LayoutParams((18.889f * w).toInt(), (6.667f * w).toInt()).apply {
            addRule(CENTER_IN_PARENT, TRUE)
        })

        ivRight = ImageView(context).apply {
            id = 1221
            setImageResource(R.drawable.ic_vip)
            setPadding((1.11f * w).toInt())
        }
        addView(ivRight, LayoutParams((8.333f * w).toInt(), (8.333f * w).toInt()).apply {
            addRule(ALIGN_PARENT_END, TRUE)
            rightMargin = (4.44f * w).toInt()

        })

        ivRight2 = ImageView(context).apply {
            visibility = GONE
            setImageResource(R.drawable.ic_vip)
            setPadding((1.11f * w).toInt())
        }
        addView(ivRight2, LayoutParams((8.889f * w).toInt(), (8.889f * w).toInt()).apply {
            addRule(LEFT_OF, ivRight.id)
            rightMargin = (12.222f * w).toInt()
        })
    }
}