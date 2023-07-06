package com.tattoo.tattoomaker.on.myphoto.addview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsDrawable

@SuppressLint("ResourceType")
class ViewPickPicture(context: Context) : RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val viewToolbar: ViewToolbar
    val rcvBucket: RecyclerView
    val rcvPicture: RecyclerView
    val viewLoading: LottieAnimationView

    init {
        w = resources.displayMetrics.widthPixels / 100F
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
            tvTitle.text = resources.getString(R.string.gallery)
            ivRight.visibility = GONE
        }
        addView(viewToolbar, LayoutParams(-1, (8.33f * w).toInt()).apply {
            topMargin = (13.61f * w).toInt()
        })

        val paramsRcv = LayoutParams(-1, -1).apply {
            setMargins(
                (5.56f * w).toInt(),
                (5.56f * w).toInt(),
                (5.56f * w).toInt(),
                (4.167f * w).toInt()
            )
            addRule(BELOW, viewToolbar.id)
        }

        rcvBucket = RecyclerView(context)
        addView(rcvBucket, paramsRcv)

        rcvPicture = RecyclerView(context)
        addView(rcvPicture, paramsRcv)

        viewLoading = LottieAnimationView(context).apply {
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
}