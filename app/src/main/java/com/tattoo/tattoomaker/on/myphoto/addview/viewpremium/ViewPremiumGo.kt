package com.tattoo.tattoomaker.on.myphoto.addview.viewpremium

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom

@SuppressLint("ResourceType")
class ViewPremiumGo(context: Context) : RelativeLayout(context) {

    companion object {
        var w = 0
    }

    val ivBack: ImageView
    val scrollHorizontal: HorizontalScrollView
    val ll: LinearLayout
    val tvUpgrate: TextView

    init {
        w = resources.displayMetrics.widthPixels

        val ivBackground = ImageView(context).apply {
            scaleType = ImageView.ScaleType.FIT_XY
            setImageResource(R.drawable.im_bg_premium_2)
        }
        addView(ivBackground, -1, -1)

        ivBack = ImageView(context).apply { setImageResource(R.drawable.ic_back) }
        addView(ivBack, LayoutParams((8.33f * w / 100).toInt(), (8.33f * w / 100).toInt()).apply {
            topMargin = (13.833f * w / 100).toInt()
            leftMargin = (5.556f * w / 100).toInt()
        })

        val ivBg = ImageView(context).apply {
            id = 1221
            setImageResource(R.drawable.im_premium_2)
        }
        addView(ivBg, LayoutParams(-1, (37.22f * w / 100).toInt()).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (28.0556f * w / 100).toInt()
        })

        val ivBg2 = ImageView(context).apply {
            id = 1223
            setImageResource(R.drawable.im_splash_2)
        }
        addView(ivBg2, LayoutParams(-1, (20.556f * w / 100).toInt()).apply {
            addRule(BELOW, ivBg.id)
            topMargin = (4.167f * w / 100).toInt()
            leftMargin = (12.22f * w / 100).toInt()
            rightMargin = (12.22f * w / 100).toInt()
        })

        val rl1 = RelativeLayout(context)
        val rl1Center = RelativeLayout(context)

        val tvDes2 = TextView(context).apply {
            id = 1331
            textCustom(
                resources.getString(R.string.des_vip_2),
                ContextCompat.getColor(context, R.color.gray_light_2),
                3.33f * w / 100,
                "solway_bold",
                context
            )
            gravity = Gravity.CENTER
        }
        addView(tvDes2, LayoutParams(-1, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
            bottomMargin = (5.556f * w / 100).toInt()
            leftMargin = (4.44f * w / 100).toInt()
            rightMargin = (4.44f * w / 100).toInt()
        })

        tvUpgrate = TextView(context).apply {
            id = 1332
            textCustom(
                resources.getString(R.string.upgrade_premium),
                ContextCompat.getColor(context, R.color.white),
                4.44f * w / 100,
                "solway_medium",
                context
            )
            background = Utils.createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.color_main)),
                (2.5f * w / 100).toInt(),
                (0.55f * w / 100).toInt(),
                ContextCompat.getColor(context, R.color.white)
            )
            gravity = Gravity.CENTER
        }
        addView(tvUpgrate, LayoutParams(-1, (14.72f * w / 100).toInt()).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(ABOVE, tvDes2.id)
            bottomMargin = (3.33f * w / 100).toInt()
            leftMargin = (18.05f * w / 100).toInt()
            rightMargin = (18.05f * w / 100).toInt()
        })

        scrollHorizontal = HorizontalScrollView(context).apply {
            id = 1224
            isHorizontalScrollBarEnabled = false
        }
        ll = LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL }
        val item1 = ViewItemSlide(context).apply {
            iv.setImageResource(R.drawable.ic_create_tattoo)
            tv.text = resources.getString(R.string.create_tattoo)
        }
        ll.addView(item1, LinearLayout.LayoutParams(-2, (14.44f * w / 100).toInt()).apply {
            leftMargin = (4.44f * w / 100).toInt()
        })

        val item2 = ViewItemSlide(context).apply {
            iv.setImageResource(R.drawable.ic_unlook_all_tattoo)
            tv.text = resources.getString(R.string.unlock_all_tattoos)
        }
        ll.addView(item2, LinearLayout.LayoutParams(-2, (14.44f * w / 100).toInt()).apply {
            leftMargin = (4.44f * w / 100).toInt()
        })

        val item3 = ViewItemSlide(context).apply {
            iv.setImageResource(R.drawable.ic_remove_all_ads)
            tv.text = resources.getString(R.string.remove_all_ads)
        }
        ll.addView(item3, LinearLayout.LayoutParams(-2, (14.44f * w / 100).toInt()).apply {
            leftMargin = (4.44f * w / 100).toInt()
        })

        val item4 = ViewItemSlide(context).apply {
            iv.setImageResource(R.drawable.ic_full_feature)
            tv.text = resources.getString(R.string.full_feature)
        }
        ll.addView(item4, LinearLayout.LayoutParams(-2, (14.44f * w / 100).toInt()).apply {
            leftMargin = (4.44f * w / 100).toInt()
        })

        val item5 = ViewItemSlide(context).apply {
            iv.setImageResource(R.drawable.ic_life_time)
            tv.text = resources.getString(R.string.lifetime_support)
        }
        ll.addView(item5, LayoutParams(LinearLayout.LayoutParams(-2, (14.44f * w / 100).toInt()).apply {
            leftMargin = (4.44f * w / 100).toInt()
            rightMargin = (4.44f * w / 100).toInt()
        }))

        scrollHorizontal.addView(ll, -2, -2)
        rl1Center.addView(scrollHorizontal, -2, -2)

        val rlPrice = RelativeLayout(context)

        val rl = RelativeLayout(context).apply {
            background = Utils.createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.trans)),
                (2.5f * w / 100).toInt(),
                (0.55f * w / 100).toInt(),
                ContextCompat.getColor(context, R.color.color_main)
            )
        }
        val tvPrice = TextView(context).apply {
            id = 1884
            textCustom(
                resources.getString(R.string.lifetime_price),
                ContextCompat.getColor(context, R.color.white),
                5.556f * w / 100,
                "solway_medium",
                context
            )
        }
        rl.addView(tvPrice, LayoutParams(-2, -2).apply {
            topMargin = (7.22f * w / 100).toInt()
            addRule(CENTER_HORIZONTAL, TRUE)
        })
        val tvLifetime = TextView(context).apply {
            textCustom(
                resources.getString(R.string.lifetime),
                ContextCompat.getColor(context, R.color.gray_light_2),
                3.889f * w / 100,
                "solway_regular",
                context
            )
        }
        rl.addView(tvLifetime, LayoutParams(-2, -2).apply {
            topMargin = (2.778f * w / 100).toInt()
            bottomMargin = (3.32f * w / 100).toInt()
            addRule(BELOW, tvPrice.id)
            addRule(CENTER_HORIZONTAL, TRUE)
        })
        rlPrice.addView(rl, LayoutParams(-1, (28.056f * w / 100).toInt()).apply {
            topMargin = (3.33f * w / 100).toInt()
        })

        val tvPriceTitle = TextView(context).apply {
            textCustom(
                resources.getString(R.string.best_seller),
                Color.parseColor("#140E25"),
                4.44f * w / 100,
                "solway_medium",
                context
            )
            background = Utils.createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.color_main)),
                (2.5f * w / 100).toInt(), -1, -1
            )
            gravity = Gravity.CENTER
            setPadding(
                (4.44f * w / 100).toInt(),
                (1.11f * w / 100).toInt(),
                (4.44f * w / 100).toInt(),
                (1.11f * w / 100).toInt()
            )
        }
        rlPrice.addView(tvPriceTitle, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
        })
        rl1Center.addView(rlPrice, LayoutParams(-1, -2).apply {
            addRule(BELOW, scrollHorizontal.id)
            topMargin = (8.889f * w / 100).toInt()
            leftMargin = (25.556f * w / 100).toInt()
            rightMargin = (25.556f * w / 100).toInt()
        })
        rl1.addView(rl1Center, LayoutParams(-2, -2).apply { addRule(CENTER_IN_PARENT, TRUE) })

        addView(rl1, LayoutParams(-1, -1).apply {
            topMargin = (5.556f * w / 100).toInt()
            bottomMargin = (5.556f * w / 100).toInt()
            addRule(BELOW, ivBg2.id)
            addRule(ABOVE, tvUpgrate.id)
        })
    }

    class ViewItemSlide(context: Context) : RelativeLayout(context) {

        val iv: ImageView
        val tv: TextView

        init {
            background = Utils.createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.trans)),
                (2.5f * w / 100).toInt(),
                (0.55f * w / 100).toInt(),
                ContextCompat.getColor(context, R.color.white)
            )

            iv = ImageView(context).apply { id = 1221 }
            val paramsImage =
                LayoutParams((8.889f * w / 100).toInt(), (8.889f * w / 100).toInt()).apply {
                    leftMargin = (2.778f * w / 100).toInt()
                    addRule(CENTER_VERTICAL)
                }
            addView(iv, paramsImage)

            tv = TextView(context).apply {
                textCustom(
                    "",
                    ContextCompat.getColor(context, R.color.white),
                    3.889f * w / 100,
                    "solway_regular",
                    context
                )
            }
            addView(tv, LayoutParams(-2, -2).apply {
                addRule(CENTER_VERTICAL, TRUE)
                addRule(RIGHT_OF, iv.id)
                leftMargin = (4.44f * w / 100).toInt()
                rightMargin = (4.44f * w / 100).toInt()
            })
        }
    }
}