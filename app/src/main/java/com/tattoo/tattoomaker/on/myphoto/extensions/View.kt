package com.tattoo.tattoomaker.on.myphoto.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.tattoo.tattoomaker.on.myphoto.utils.UnDoubleClick
import kotlin.apply
import androidx.core.graphics.createBitmap
import androidx.core.view.isVisible
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


fun View.setOnUnDoubleClickListener(onUnDoubleClick: (View) -> Unit) {
    setOnClickListener(UnDoubleClick {
//        applyClickEffect(it)
        onUnDoubleClick(it)
    })
}

fun View.setOnClickEffectListener(onClick: (View) -> Unit) {
    setOnClickListener {
        applyClickEffect(it)
        onClick(it)
    }
}

fun applyClickEffect(view: View) {
    val animation = AlphaAnimation(1.0f, 0.5f) .apply {
        duration = 134
        repeatMode = AlphaAnimation.REVERSE
        repeatCount = 1

    }
    view.startAnimation(animation)
}

fun View.loadBitmapFromView(): Bitmap {
    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)
    layout(left, top, right, bottom)
    draw(canvas)
    return bitmap
}

fun View.createBackground(colorArr: IntArray, border: Float, stroke: Int, colorStroke: Int) {
    background = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = border
        if (stroke != -1) setStroke(stroke, colorStroke)

        if (colorArr.size >= 2) {
            colors = colorArr
            gradientType = GradientDrawable.LINEAR_GRADIENT
        } else setColor(colorArr[0])
    }
}

fun View.createBackground(colorArr: IntArray, border: FloatArray, stroke: Int, colorStroke: Int) {
    background = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadii = border
        if (stroke != -1) setStroke(stroke, colorStroke)

        if (colorArr.size >= 2) {
            colors = colorArr
            gradientType = GradientDrawable.LINEAR_GRADIENT
        } else setColor(colorArr[0])
    }
}

fun View.effectPressRectangle(): View {
    val value = TypedValue()
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, value, true)
    setBackgroundResource(value.resourceId)
    isFocusable = true // Required for some view types
    return this
}

fun View.effectPressOval(): View {
    val outValue = TypedValue()
    context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true)
    setBackgroundResource(outValue.resourceId)
    isFocusable = true // Required for some view types
    return this
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}


fun View.getVectorBitmap(@DrawableRes id: Int, width: Int, height: Int): Bitmap? {
    val drawable = AppCompatResources.getDrawable(context, id) ?: return null
    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun View.slideInUp(duration: Long = 300) {
    if (isVisible) return
    visible()
    YoYo.with(Techniques.SlideInUp)
        .duration(duration)
        .playOn(this)
}

fun View.slideOutUp(duration: Long = 300) {
    YoYo.with(Techniques.SlideOutUp)
        .duration(duration)
        .onEnd { gone() }
        .playOn(this)
}

fun View.slideInDown(duration: Long = 300) {
    visible()
    YoYo.with(Techniques.SlideInDown)
        .duration(duration)
        .playOn(this)
}

fun View.slideOutDown(duration: Long = 300) {
    if (!isVisible) return
    YoYo.with(Techniques.SlideOutDown)
        .duration(duration)
        .onEnd { gone() }
        .playOn(this)
}

fun View.slideOutRight(duration: Long = 300) {
    YoYo.with(Techniques.SlideOutRight)
        .duration(duration)
        .onEnd { gone() }
        .playOn(this)
}

fun View.slideInRight(duration: Long = 300) {
    visible()
    YoYo.with(Techniques.SlideInRight)
        .duration(duration)
        .playOn(this)
}