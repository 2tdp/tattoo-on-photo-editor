package com.tattoo.tattoomaker.on.myphoto.addview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.addview.viewedit.ViewEdit
import com.tattoo.tattoomaker.on.myphoto.addview.viewhome.ViewChooseBackground
import com.tattoo.tattoomaker.on.myphoto.addview.viewitem.ViewItemMyStory
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsDrawable
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom

@SuppressLint("ResourceType")
class ViewMyStory(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val viewToolbar: ViewToolbar
    val tvComplete: TextView
    val tvDrafts: TextView
    val viewPager: ViewPager2

    val viewLoading: LottieAnimationView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(Color.WHITE)

        viewToolbar = ViewToolbar(context).apply {
            id = 1221
            UtilsDrawable.changeIcon(
                context, "back", 1, R.drawable.ic_back, ivBack,
                ContextCompat.getColor(context, R.color.black_text),
                ContextCompat.getColor(context, R.color.black_text)
            )
            ivRight.setImageResource(R.drawable.ic_item_tick)
            ivRight.visibility = VISIBLE
            tvTitle.text = resources.getString(R.string.my_story)
        }
        addView(viewToolbar, LayoutParams(-1, -2).apply {
            topMargin = (13.61f * w).toInt()
        })

        val ll = LinearLayout(context).apply {
            id = 1222
            orientation = LinearLayout.HORIZONTAL
        }

        tvComplete = TextView(context).apply {
            textCustom(
                resources.getString(R.string.completed),
                ContextCompat.getColor(context, R.color.white),
                4.44f * w,
                "solway_regular",
                context
            )
            gravity = Gravity.CENTER
            background = Utils.createBackground(
                intArrayOf(
                    ContextCompat.getColor(context, R.color.main_color_1),
                    ContextCompat.getColor(context, R.color.main_color_2)
                ), (3.5f * w).toInt(), -1, -1
            )
        }
        ll.addView(tvComplete, LinearLayout.LayoutParams(-1, -1, 1F).apply {
            rightMargin = (2.22f * w).toInt()
        })

        tvDrafts = TextView(context).apply {
            textCustom(
                resources.getString(R.string.drafts),
                ContextCompat.getColor(context, R.color.white),
                4.44f * w,
                "solway_regular",
                context
            )
            gravity = Gravity.CENTER
            background = Utils.createBackground(
                intArrayOf(
                    ContextCompat.getColor(context, R.color.main_color_1),
                    ContextCompat.getColor(context, R.color.main_color_2)
                ), (3.5f * w).toInt(), -1, -1
            )
        }
        ll.addView(tvDrafts, LinearLayout.LayoutParams(-1, -1, 1F).apply {
            leftMargin = (2.22f * w).toInt()
        })
        addView(ll, LayoutParams(-1, (16.111f * w).toInt()).apply {
            addRule(BELOW, viewToolbar.id)
            leftMargin = (4.44f * w).toInt()
            rightMargin = (4.44f * w).toInt()
            topMargin = (7.778f * w).toInt()
        })
        clickOption(0)

        viewPager = ViewPager2(context).apply { isHorizontalScrollBarEnabled = false }
        addView(viewPager, LayoutParams(-1, -1).apply {
            addRule(BELOW, ll.id)
            topMargin = (8.33f * w).toInt()
            leftMargin = (2.22f * w).toInt()
            rightMargin = (2.22f * w).toInt()
        })

        viewLoading = LottieAnimationView(context).apply {
            visibility = GONE
            setAnimation(R.raw.loading)
            repeatCount = LottieDrawable.INFINITE
        }
        viewLoading.playAnimation()
        addView(viewLoading, LayoutParams(-1, (84.22f * w).toInt()).apply {
            addRule(CENTER_IN_PARENT, TRUE)
            leftMargin = (5.556f * w).toInt()
            rightMargin = (5.556f * w).toInt()
        })
    }

    fun clickOption(option: Int) {
        when(option) {
            0 -> {
                tvComplete.background = Utils.createBackground(
                    intArrayOf(
                        ContextCompat.getColor(context, R.color.main_color_1),
                        ContextCompat.getColor(context, R.color.main_color_2)
                    ), (3.5f * w).toInt(), -1, -1
                )

                tvDrafts.background = Utils.createBackground(
                    intArrayOf(
                        ContextCompat.getColor(context, R.color.main_color_1_alpha),
                        ContextCompat.getColor(context, R.color.main_color_2_alpha)
                    ), (3.5f * w).toInt(), -1, -1
                )
            }
            1 -> {
                tvComplete.background = Utils.createBackground(
                    intArrayOf(
                        ContextCompat.getColor(context, R.color.main_color_1_alpha),
                        ContextCompat.getColor(context, R.color.main_color_2_alpha)
                    ), (3.5f * w).toInt(), -1, -1
                )

                tvDrafts.background = Utils.createBackground(
                    intArrayOf(
                        ContextCompat.getColor(context, R.color.main_color_1),
                        ContextCompat.getColor(context, R.color.main_color_2)
                    ), (3.5f * w).toInt(), -1, -1
                )
            }
        }
    }
}