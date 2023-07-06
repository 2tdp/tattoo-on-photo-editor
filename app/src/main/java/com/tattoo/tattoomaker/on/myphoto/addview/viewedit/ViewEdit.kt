package com.tattoo.tattoomaker.on.myphoto.addview.viewedit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.addview.ViewToolbar
import com.tattoo.tattoomaker.on.myphoto.addview.viewitem.ViewItemBottomEdit
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsDrawable
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.stickerview.StickerView

@SuppressLint("ResourceType")
class ViewEdit(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val viewToolbar: ViewToolbar
    val rlMain: RelativeLayout
    val iv: ImageView
    val ivFrame: ImageView
    val stickerView: StickerView
    val viewBottomEdit: ViewBottomEdit

    //tattoos
    val viewChildTattoo: ViewTattoos
    val viewColorTattoo: ViewColor
    val viewColorTattooPremium: ViewColorPremium

    //text
    val viewBottomEditText: ViewBottomEditText
    val viewColorText: ViewColor
    val viewShadowText: ViewColor

    //filter
    val viewEditFilter: ViewRecycle

    //frame
    val viewAddFrame: ViewRecycle

    val viewBottomEditAdjust: ViewBottomEditBackground
    val viewEditAdjust: ViewAdjust

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
            ivRight.setImageResource(R.drawable.ic_export)
            tvTitle.visibility = GONE
            ll.visibility = VISIBLE
        }
        addView(viewToolbar, LayoutParams(-1, -2).apply {
            topMargin = (13.61f * w).toInt()
        })

        rlMain = RelativeLayout(context)
        addView(rlMain, LayoutParams(-1, (141.11f * w).toInt()).apply {
            addRule(BELOW, viewToolbar.id)
            topMargin = (6.11f * w).toInt()
            bottomMargin = (50f * w).toInt()
            leftMargin = (2.778f * w).toInt()
            rightMargin = (2.778f * w).toInt()
        })

        iv = ImageView(context)
        rlMain.addView(iv, LayoutParams(-1, -1).apply {
            addRule(CENTER_IN_PARENT, TRUE)
        })

        ivFrame = ImageView(context).apply { scaleType = ImageView.ScaleType.FIT_XY }
        rlMain.addView(ivFrame, LayoutParams(-1, -1).apply {
            addRule(CENTER_IN_PARENT, TRUE)
        })

        stickerView = StickerView(context).apply { hideBorderAndIcon(1) }
        rlMain.addView(stickerView, LayoutParams(-1, -1).apply {
            addRule(CENTER_IN_PARENT, TRUE)
        })

        //main
        viewBottomEdit = ViewBottomEdit(context).apply { id = 1222 }
        addView(viewBottomEdit, LayoutParams(-1, (21.389f * w).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
        })

        viewChildTattoo = ViewTattoos(context).apply { visibility = GONE }
        addView(viewChildTattoo, LayoutParams(-1, (15.556f * w).toInt()).apply {
            addRule(ABOVE, viewBottomEdit.id)
        })

        viewColorTattoo = ViewColor(context).apply { visibility = GONE }
        addView(viewColorTattoo, LayoutParams(-1, (21.94f * w).toInt()).apply {
            addRule(ABOVE, viewBottomEdit.id)
        })

        viewColorTattooPremium = ViewColorPremium(context).apply { visibility = GONE }
        addView(viewColorTattooPremium, LayoutParams(-1, (23.616f * w).toInt()).apply {
            addRule(ABOVE, viewBottomEdit.id)
        })

        //text
        viewBottomEditText = ViewBottomEditText(context).apply {
            id = 1223
            visibility = GONE
        }
        addView(viewBottomEditText, LayoutParams(-1, (21.389f * w).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
        })

        viewColorText = ViewColor(context).apply { visibility = GONE }
        addView(viewColorText, LayoutParams(-1, (21.94f * w).toInt()).apply {
            addRule(ABOVE, viewBottomEditText.id)
        })

        viewShadowText = ViewColor(context).apply {
            visibility = GONE
            vOpacity.tvOpacity.text = resources.getString(R.string.shadow)
        }
        addView(viewShadowText, LayoutParams(-1, (21.94f * w).toInt()).apply {
            addRule(ABOVE, viewBottomEditText.id)
        })

        //filter
        viewEditFilter = ViewRecycle(context).apply { visibility = GONE }
        addView(viewEditFilter, LayoutParams(-1, (15.556f * w).toInt()).apply {
            addRule(ABOVE, viewBottomEdit.id)
        })

        //frame
        viewAddFrame = ViewRecycle(context).apply { visibility = GONE }
        addView(viewAddFrame, LayoutParams(-1, (15.556f * w).toInt()).apply {
            addRule(ABOVE, viewBottomEdit.id)
        })

        //adjust
        viewBottomEditAdjust = ViewBottomEditBackground(context).apply {
            id = 1224
            visibility = GONE
        }
        addView(viewBottomEditAdjust, LayoutParams(-1, (21.389f * w).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
        })

        viewEditAdjust = ViewAdjust(context).apply { visibility = GONE }
        addView(viewEditAdjust, LayoutParams(-1, (15.556f * w).toInt()).apply {
            addRule(ABOVE, viewBottomEditAdjust.id)
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

    fun swipeOptionBottom(option: String) {
        when(option) {
            "main" -> {
                if (viewBottomEditText.isVisible) {
                    hideViewText()
                    viewBottomEditText.animation =
                        AnimationUtils.loadAnimation(context, R.anim.slide_down_out)
                    viewBottomEditText.visibility = GONE
                }
                if (viewBottomEditAdjust.isVisible) {
                    viewBottomEditAdjust.animation =
                        AnimationUtils.loadAnimation(context, R.anim.slide_down_out)
                    viewBottomEditAdjust.visibility = GONE
                }
                hideViewAdjust()

                if (!viewBottomEdit.isVisible) {
                    viewBottomEdit.animation =
                        AnimationUtils.loadAnimation(context, R.anim.slide_up_in)
                    viewBottomEdit.visibility = VISIBLE
                }
            }
            "text" -> {
                if (viewBottomEdit.isVisible) {
                    viewBottomEdit.animation =
                        AnimationUtils.loadAnimation(context, R.anim.slide_down_out)
                    viewBottomEdit.visibility = GONE
                }

                viewBottomEditText.chooseOption("color_text")
                if (!viewBottomEditText.isVisible) {
                    viewBottomEditText.animation =
                        AnimationUtils.loadAnimation(context, R.anim.slide_up_in)
                    viewBottomEditText.visibility = VISIBLE
                }
            }
            "adjust" -> {
                if (viewBottomEditText.isVisible) {
                    hideViewText()
                    viewBottomEditText.animation =
                        AnimationUtils.loadAnimation(context, R.anim.slide_down_out)
                    viewBottomEditText.visibility = GONE
                }
                if (viewBottomEdit.isVisible) {
                    viewBottomEdit.animation =
                        AnimationUtils.loadAnimation(context, R.anim.slide_down_out)
                    viewBottomEdit.visibility = GONE
                }

                if (!viewBottomEditAdjust.isVisible) {
                    viewBottomEditAdjust.animation =
                        AnimationUtils.loadAnimation(context, R.anim.slide_up_in)
                    viewBottomEditAdjust.visibility = VISIBLE
                }
                if (!viewEditAdjust.isVisible) {
                    viewEditAdjust.animation =
                        AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
                    viewEditAdjust.visibility = VISIBLE
                    viewBottomEditAdjust.chooseOption("brightness")
                }
            }
        }
    }

    fun hideViewAdjust() {
        if (viewEditAdjust.isVisible) {
            viewEditAdjust.animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
            viewEditAdjust.visibility = GONE
        }
    }

    fun hideViewFrame() {
        if (viewAddFrame.isVisible) {
            viewAddFrame.animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
            viewAddFrame.visibility = GONE
        }
    }

    fun hideViewFilter() {
        if (viewEditFilter.isVisible) {
            viewEditFilter.animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
            viewEditFilter.visibility = GONE
        }
    }

    fun hideViewText() {
        if (viewColorText.isVisible) {
            viewColorText.animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
            viewColorText.visibility = GONE
        }

        if (viewShadowText.isVisible) {
            viewShadowText.animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
            viewShadowText.visibility = GONE
        }
    }

    fun hideViewTattooPremium() {
        if(viewChildTattoo.rcvPremium.isVisible) viewChildTattoo.clickPremium("free")

        if (viewColorTattooPremium.isVisible) {
            viewColorTattooPremium.animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
            viewColorTattooPremium.visibility = GONE
        }
    }

    fun hideViewTattoo() {
        if (viewColorTattoo.isVisible) {
            viewColorTattoo.animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
            viewColorTattoo.visibility = GONE
        }
    }

    inner class ViewBottomEditBackground(context: Context): HorizontalScrollView(context) {

        val viewBrightness: ViewItemBottomEdit
        val viewContrast: ViewItemBottomEdit
        val viewExposure: ViewItemBottomEdit
        val viewSaturation: ViewItemBottomEdit
        val viewSharpen: ViewItemBottomEdit
        val viewVignette: ViewItemBottomEdit

        init {
            setBackgroundColor(ContextCompat.getColor(context, R.color.black_bottom))
            isHorizontalScrollBarEnabled = false

            val ll = LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL }
            viewBrightness = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_brightness)
                tv.text = resources.getString(R.string.brightness)
            }
            ll.addView(viewBrightness, LinearLayout.LayoutParams((18.056f * w).toInt(), -2).apply {
                gravity = Gravity.CENTER
                leftMargin = (4.44f * w).toInt()
            })

            viewContrast = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_contrast)
                tv.text = resources.getString(R.string.contrast)
            }
            ll.addView(viewContrast, LinearLayout.LayoutParams((18.056f * w).toInt(), -2).apply {
                gravity = Gravity.CENTER
                leftMargin = (2.22f * w).toInt()
                rightMargin = (2.22f * w).toInt()
            })

            viewSaturation = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_saturation)
                tv.text = resources.getString(R.string.saturation)
            }
            ll.addView(viewSaturation, LinearLayout.LayoutParams((18.056f * w).toInt(), -2).apply {
                gravity = Gravity.CENTER
                leftMargin = (2.22f * w).toInt()
                rightMargin = (2.22f * w).toInt()
            })

            viewExposure = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_exposure)
                tv.text = resources.getString(R.string.exposure)
            }
            ll.addView(viewExposure, LinearLayout.LayoutParams((18.056f * w).toInt(), -2).apply {
                gravity = Gravity.CENTER
                leftMargin = (2.22f * w).toInt()
                rightMargin = (2.22f * w).toInt()
            })

            viewSharpen = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_sharpen)
                tv.text = resources.getString(R.string.sharpen)
            }
            ll.addView(viewSharpen, LinearLayout.LayoutParams((18.056f * w).toInt(), -2).apply {
                gravity = Gravity.CENTER
                leftMargin = (2.22f * w).toInt()
                rightMargin = (2.22f * w).toInt()
            })

            viewVignette = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_vignette)
                tv.text = resources.getString(R.string.vignette)
            }
            ll.addView(viewVignette, LinearLayout.LayoutParams((18.056f * w).toInt(), -2).apply {
                gravity = Gravity.CENTER
                leftMargin = (2.22f * w).toInt()
                rightMargin = (4.44f * w).toInt()
            })

            addView(ll, -2, -1)
        }

        fun chooseOption(option: String) {
            when(option) {
                "brightness" -> {
                    viewEditAdjust.tvTitle.text = resources.getString(R.string.brightness)
                    viewBrightness.apply {
                        tv.setTextColor(Color.WHITE)
                        iv.setImageResource(R.drawable.ic_brightness)
                    }

                    viewContrast.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "contrast", 1, R.drawable.ic_contrast, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewExposure.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "exposure", 3, R.drawable.ic_exposure, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewSaturation.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "saturation", 2, R.drawable.ic_saturation, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewSharpen.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "sharpen", 1, R.drawable.ic_sharpen, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewVignette.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "vignette", 2, R.drawable.ic_vignette, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                }

                "contrast" -> {
                    viewEditAdjust.tvTitle.text = resources.getString(R.string.contrast)
                    viewContrast.apply {
                        tv.setTextColor(Color.WHITE)
                        iv.setImageResource(R.drawable.ic_contrast)
                    }

                    viewBrightness.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "brightness", 9, R.drawable.ic_brightness, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewExposure.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "exposure", 3, R.drawable.ic_exposure, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewSaturation.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "saturation", 2, R.drawable.ic_saturation, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewSharpen.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "sharpen", 1, R.drawable.ic_sharpen, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewVignette.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "vignette", 2, R.drawable.ic_vignette, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                }

                "exposure" -> {
                    viewEditAdjust.tvTitle.text = resources.getString(R.string.exposure)
                    viewExposure.apply {
                        tv.setTextColor(Color.WHITE)
                        iv.setImageResource(R.drawable.ic_exposure)
                    }

                    viewBrightness.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "brightness", 9, R.drawable.ic_brightness, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewSaturation.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "saturation", 2, R.drawable.ic_saturation, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewContrast.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "contrast", 1, R.drawable.ic_contrast, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewSharpen.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "sharpen", 1, R.drawable.ic_sharpen, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewVignette.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "vignette", 2, R.drawable.ic_vignette, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                }

                "saturation" -> {
                    viewEditAdjust.tvTitle.text = resources.getString(R.string.saturation)
                    viewSaturation.apply {
                        tv.setTextColor(Color.WHITE)
                        iv.setImageResource(R.drawable.ic_saturation)
                    }

                    viewBrightness.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "brightness", 9, R.drawable.ic_brightness, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewContrast.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "contrast", 1, R.drawable.ic_contrast, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewExposure.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "exposure", 3, R.drawable.ic_exposure, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewSharpen.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "sharpen", 1, R.drawable.ic_sharpen, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewVignette.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "vignette", 2, R.drawable.ic_vignette, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                }

                "sharpen" -> {
                    viewEditAdjust.tvTitle.text = resources.getString(R.string.sharpen)
                    viewSharpen.apply {
                        tv.setTextColor(Color.WHITE)
                        iv.setImageResource(R.drawable.ic_sharpen)
                    }

                    viewBrightness.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "brightness", 9, R.drawable.ic_brightness, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewContrast.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "contrast", 1, R.drawable.ic_contrast, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewExposure.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "exposure", 3, R.drawable.ic_exposure, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewSaturation.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "saturation", 2, R.drawable.ic_saturation, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewVignette.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "vignette", 2, R.drawable.ic_vignette, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                }

                "vignette" -> {
                    viewEditAdjust.tvTitle.text = resources.getString(R.string.vignette)
                    viewVignette.apply {
                        tv.setTextColor(Color.WHITE)
                        iv.setImageResource(R.drawable.ic_vignette)
                    }

                    viewBrightness.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "brightness", 9, R.drawable.ic_brightness, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewContrast.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "contrast", 1, R.drawable.ic_contrast, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewExposure.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "exposure", 3, R.drawable.ic_exposure, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewSaturation.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "saturation", 2, R.drawable.ic_saturation, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewSharpen.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "sharpen", 1, R.drawable.ic_sharpen, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                }
            }
        }
    }

    inner class ViewBottomEditText(context: Context): LinearLayout(context) {

        val vTextColor: ViewItemBottomEdit
        val vShadowText: ViewItemBottomEdit
        val vEditText: ViewItemBottomEdit

        init {
            orientation = HORIZONTAL
            setBackgroundColor(ContextCompat.getColor(context, R.color.black_bottom))

            vTextColor = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_color_text)
                tv.text = resources.getText(R.string.color_text)
            }
            addView(vTextColor, LayoutParams(-1, -2, 1F).apply {
                gravity = Gravity.CENTER
            })

            vShadowText = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_shadow_text)
                tv.text = resources.getText(R.string.shadow_text)
            }
            addView(vShadowText, LayoutParams(-1, -2, 1F).apply {
                gravity = Gravity.CENTER
            })

            vEditText = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_edit_text)
                tv.text = resources.getText(R.string.edit_text)
            }
            addView(vEditText, LayoutParams(-1, -2, 1F).apply {
                gravity = Gravity.CENTER
            })
        }

        fun chooseOption(option: String) {
            when(option) {
                "color_text" -> {
                    vTextColor.apply {
                        tv.setTextColor(Color.WHITE)
                        iv.setImageResource(R.drawable.ic_color_text)
                    }
                    if (!viewColorText.isVisible) {
                        viewColorText.animation =
                            AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
                        viewColorText.visibility = VISIBLE
                    }

                    vShadowText.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "shadow_text", 1, R.drawable.ic_shadow_text, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                    if (viewShadowText.isVisible) {
                        viewShadowText.animation =
                            AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
                        viewShadowText.visibility = GONE
                    }

                    vEditText.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "edit_text", 10, R.drawable.ic_edit_text, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                }

                "shadow_text" -> {
                    vShadowText.apply {
                        tv.setTextColor(Color.WHITE)
                        iv.setImageResource(R.drawable.ic_shadow_text)
                    }
                    if (!viewShadowText.isVisible) {
                        viewShadowText.animation =
                            AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
                        viewShadowText.visibility = VISIBLE
                    }

                    vTextColor.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "color_text", 1, R.drawable.ic_color_text, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                    if (viewColorText.isVisible) {
                        viewColorText.animation =
                            AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
                        viewColorText.visibility = GONE
                    }

                    vEditText.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "edit_text", 10, R.drawable.ic_edit_text, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                }

                "edit_text" -> {
                    vEditText.apply {
                        tv.setTextColor(Color.WHITE)
                        iv.setImageResource(R.drawable.ic_edit_text)
                    }

                    vTextColor.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "color_text", 1, R.drawable.ic_color_text, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    vShadowText.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "shadow_text", 1, R.drawable.ic_shadow_text, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                }
            }
        }
    }

    inner class ViewBottomEdit(context: Context): LinearLayout(context) {

        val viewTattoos: ViewItemBottomEdit
        val viewText: ViewItemBottomEdit
        val viewFilter: ViewItemBottomEdit
        val viewFrame: ViewItemBottomEdit
        val viewAdjust: ViewItemBottomEdit

        init {
            orientation = HORIZONTAL
            setBackgroundColor(ContextCompat.getColor(context, R.color.black_bottom))

            viewTattoos = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_tattoos_edit)
                tv.text = resources.getText(R.string.tattoos)
            }
            addView(viewTattoos, LayoutParams(-1, -2, 1F).apply {
                gravity = Gravity.CENTER
            })

            viewText = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_text_edit)
                tv.text = resources.getText(R.string.text)
            }
            addView(viewText, LayoutParams(-1, -2, 1F).apply {
                gravity = Gravity.CENTER
            })

            viewFilter = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_filter_edit)
                tv.text = resources.getText(R.string.filter)
            }
            addView(viewFilter, LayoutParams(-1, -2, 1F).apply {
                gravity = Gravity.CENTER
            })

            viewFrame = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_frame_edit)
                tv.text = resources.getText(R.string.frame)
            }
            addView(viewFrame, LayoutParams(-1, -2, 1F).apply {
                gravity = Gravity.CENTER
            })

            viewAdjust = ViewItemBottomEdit(context).apply {
                iv.setImageResource(R.drawable.ic_adjust_edit)
                tv.text = resources.getText(R.string.adjust)
            }
            addView(viewAdjust, LayoutParams(-1, -2, 1F).apply {
                gravity = Gravity.CENTER
            })
        }

        fun chooseOption(option: String) {
            when(option) {
                "tattoos" -> {
                    viewTattoos.apply {
                        tv.setTextColor(Color.WHITE)
                        iv.setImageResource(R.drawable.ic_tattoos_edit)
                    }
                    hideViewTattoo()
                    hideViewTattooPremium()
                    if (!viewChildTattoo.isVisible) {
                        viewChildTattoo.animation =
                            AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
                        viewChildTattoo.visibility = VISIBLE
                    }

                    viewText.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "text", 17, R.drawable.ic_text_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewFilter.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "filter", 6, R.drawable.ic_filter_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                    hideViewFilter()

                    viewFrame.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "frame", 5, R.drawable.ic_frame_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                    hideViewFrame()

                    viewAdjust.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "adjust", 3, R.drawable.ic_adjust_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                }

                "text" -> {
                    viewText.apply {
                        tv.setTextColor(Color.WHITE)
                        iv.setImageResource(R.drawable.ic_text_edit)
                    }

                    viewTattoos.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "tattoos", 12, R.drawable.ic_tattoos_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                    hideViewTattoo()
                    hideViewTattooPremium()

                    viewFilter.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "filter", 6, R.drawable.ic_filter_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                    hideViewFilter()

                    viewFrame.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "frame", 5, R.drawable.ic_frame_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                    hideViewFrame()

                    viewAdjust.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "adjust", 3, R.drawable.ic_adjust_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                }

                "filter" -> {
                    viewFilter.apply {
                        tv.setTextColor(Color.WHITE)
                        iv.setImageResource(R.drawable.ic_filter_edit)
                    }
                    if (!viewEditFilter.isVisible) {
                        viewEditFilter.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
                        viewEditFilter.visibility = VISIBLE
                    }

                    viewTattoos.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "tattoos", 12, R.drawable.ic_tattoos_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                    if (viewChildTattoo.isVisible) {
                        viewChildTattoo.animation =
                            AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
                        viewChildTattoo.visibility = GONE
                    }
                    hideViewTattoo()
                    hideViewTattooPremium()

                    viewText.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "text", 17, R.drawable.ic_text_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewFrame.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "frame", 5, R.drawable.ic_frame_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                    hideViewFrame()

                    viewAdjust.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "adjust", 3, R.drawable.ic_adjust_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                }

                "frame" -> {
                    viewFrame.apply {
                        tv.setTextColor(Color.WHITE)
                        iv.setImageResource(R.drawable.ic_frame_edit)
                    }
                    if (!viewAddFrame.isVisible) {
                        viewAddFrame.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
                        viewAddFrame.visibility = VISIBLE
                    }

                    viewTattoos.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "tattoos", 12, R.drawable.ic_tattoos_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                    if (viewChildTattoo.isVisible) {
                        viewChildTattoo.animation =
                            AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
                        viewChildTattoo.visibility = GONE
                    }
                    hideViewTattoo()
                    hideViewTattooPremium()

                    viewText.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "text", 17, R.drawable.ic_text_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                    hideViewText()

                    viewFilter.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "filter", 6, R.drawable.ic_filter_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                    hideViewFilter()

                    viewAdjust.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "adjust", 3, R.drawable.ic_adjust_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                }

                "adjust" -> {
                    viewAdjust.apply {
                        tv.setTextColor(Color.WHITE)
                        iv.setImageResource(R.drawable.ic_adjust_edit)
                    }

                    viewTattoos.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "tattoos", 12, R.drawable.ic_tattoos_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                    if (viewChildTattoo.isVisible) {
                        viewChildTattoo.animation =
                            AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
                        viewChildTattoo.visibility = GONE
                    }
                    hideViewTattoo()
                    hideViewTattooPremium()

                    viewText.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "text", 17, R.drawable.ic_text_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }

                    viewFilter.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "filter", 6, R.drawable.ic_filter_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                    hideViewFilter()

                    viewFrame.apply {
                        tv.setTextColor(ContextCompat.getColor(context, R.color.white2))
                        UtilsDrawable.changeIcon(
                            context, "frame", 5, R.drawable.ic_frame_edit, iv,
                            ContextCompat.getColor(context, R.color.white2),
                            ContextCompat.getColor(context, R.color.white2)
                        )
                    }
                    hideViewFrame()
                }
            }
        }
    }
}