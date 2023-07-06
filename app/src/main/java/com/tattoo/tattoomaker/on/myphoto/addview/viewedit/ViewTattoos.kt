package com.tattoo.tattoomaker.on.myphoto.addview.viewedit

import android.annotation.SuppressLint
import android.content.Context
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tattoo.tattoomaker.on.myphoto.R

@SuppressLint("ResourceType")
class ViewTattoos(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val ivNone: ImageView
    val ivPremium: ImageView
    val rcv: RecyclerView
    val rcvPremium: RecyclerView

    init {
        w = resources.displayMetrics.widthPixels / 100f
        setBackgroundColor(ContextCompat.getColor(context, R.color.black_bottom2))

        ivNone = ImageView(context).apply {
            id = 1221
            setImageResource(R.drawable.ic_none_item)
        }
        addView(ivNone, LayoutParams((7.778f * w).toInt(), (10.556f * w).toInt()).apply {
            addRule(CENTER_VERTICAL, TRUE)
            leftMargin = (2.778f * w).toInt()
        })

        ivPremium = ImageView(context).apply {
            id = 1222
            setImageResource(R.drawable.ic_vip_tattoos)
        }
        addView(ivPremium, LayoutParams((7.778f * w).toInt(), (10.556f * w).toInt()).apply {
            addRule(CENTER_VERTICAL, TRUE)
            addRule(ALIGN_PARENT_END, TRUE)
            rightMargin = (2.778f * w).toInt()
        })

        rcv = RecyclerView(context).apply { isHorizontalScrollBarEnabled = false }
        addView(rcv, LayoutParams(-1, (10.556f * w).toInt()).apply {
            addRule(RIGHT_OF, ivNone.id)
            addRule(LEFT_OF, ivPremium.id)
            addRule(CENTER_IN_PARENT, TRUE)
        })

        rcvPremium = RecyclerView(context).apply {
            isHorizontalScrollBarEnabled = false
            visibility = GONE
        }
        addView(rcvPremium, LayoutParams(-1, (10.556f * w).toInt()).apply {
            addRule(RIGHT_OF, ivNone.id)
            addRule(LEFT_OF, ivPremium.id)
            addRule(CENTER_IN_PARENT, TRUE)
        })
    }

    fun clickPremium(option: String) {
       when(option) {
           "premium" ->
               if (!rcvPremium.isVisible) {
                   rcv.animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
                   rcv.visibility = GONE

                   rcvPremium.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
                   rcvPremium.visibility = VISIBLE
               }
           "free" ->
               if (rcvPremium.isVisible) {
                   rcvPremium.animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
                   rcvPremium.visibility = GONE

                   rcv.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
                   rcv.visibility = VISIBLE
               }
       }
    }
}