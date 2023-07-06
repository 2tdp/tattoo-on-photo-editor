package com.tattoo.tattoomaker.on.myphoto.utils

import android.graphics.*
import com.tattoo.tattoomaker.on.myphoto.model.ColorModel
import com.tattoo.tattoomaker.on.myphoto.model.background.AdjustModel
import org.wysaid.nativePort.CGENativeLibrary

object UtilsAdjust {

    fun setColor(color: ColorModel, paint: Paint, w: Float, h: Float) {
        if (color.colorStart == color.colorEnd) {
            paint.shader = null
            paint.color = color.colorStart
        } else {
            if (color.direc == 4) {
                val c = color.colorStart
                color.apply {
                    colorStart = color.colorEnd
                    colorEnd = c
                    direc = 0
                }
            } else if (color.direc == 5) {
                val c = color.colorStart
                color.apply {
                    colorStart = color.colorEnd
                    colorEnd = c
                    direc = 2
                }
            }
            val shader = LinearGradient(
                setDirection(color.direc, w, h)[0].toFloat(),
                setDirection(color.direc, w, h)[1].toFloat(),
                setDirection(color.direc, w, h)[2].toFloat(),
                setDirection(color.direc, w, h)[3].toFloat(),
                intArrayOf(
                    Color.parseColor(UtilsBitmap.toRGBString(color.colorStart)),
                    Color.parseColor(UtilsBitmap.toRGBString(color.colorEnd))
                ),
                floatArrayOf(0f, 1f),
                Shader.TileMode.MIRROR
            )
            paint.shader = shader
        }
    }

    private fun setDirection(direction: Int, w: Float, h: Float): IntArray {
        when (direction) {
            0 -> return intArrayOf(w.toInt() / 2, 0, w.toInt() / 2, h.toInt())
            1 -> return intArrayOf(0, 0, w.toInt(), h.toInt())
            2 -> return intArrayOf(0, h.toInt() / 2, w.toInt(), h.toInt() / 2)
            3 -> return intArrayOf(0, h.toInt(), w.toInt(), 0)
        }
        return intArrayOf()
    }

    fun adjust(bitmap: Bitmap, adjust: AdjustModel): Bitmap {
        return if (adjust.brightness != 0f) adjustBrightness(bitmap, adjust)
        else if (adjust.contrast != 0f) adjustContrast(bitmap, adjust)
        else if (adjust.saturation != 0f) adjustSaturation(bitmap, adjust)
        else if (adjust.exposure != 0f) adjustExposure(bitmap, adjust)
        else if (adjust.sharpen != 0f) adjustSharpen(bitmap, adjust)
        else if (adjust.vignette != 0f) adjustVignette(bitmap, adjust)
        else bitmap
    }

    private fun adjustBrightness(bitmap: Bitmap?, adjust: AdjustModel): Bitmap {
        val bm = CGENativeLibrary.filterImage_MultipleEffects(
            bitmap, "@adjust brightness 0.5", adjust.brightness / 100f
        )
        return if (adjust.contrast != 0f) adjustContrast(bm, adjust)
        else if (adjust.saturation != 0f) adjustSaturation(bm, adjust)
        else if (adjust.exposure != 0f) adjustExposure(bm, adjust)
        else if (adjust.sharpen != 0f) adjustSharpen(bm, adjust)
        else if (adjust.vignette != 0f) adjustVignette(bm, adjust)
        else bm
    }

    private fun adjustContrast(bitmap: Bitmap?, adjust: AdjustModel): Bitmap {
        val bm = CGENativeLibrary.filterImage_MultipleEffects(
            bitmap, "@adjust contrast 2", adjust.contrast / 100f
        )
        return if (adjust.saturation != 0f) adjustSaturation(bm, adjust)
        else  if (adjust.exposure != 0f) adjustExposure(bm, adjust)
        else if (adjust.sharpen != 0f) adjustSharpen(bm, adjust)
        else if (adjust.vignette != 0f) adjustVignette(bm, adjust)
        else bm
    }

    private fun adjustSaturation(bitmap: Bitmap?, adjust: AdjustModel): Bitmap {
        val bm = CGENativeLibrary.filterImage_MultipleEffects(
            bitmap, "@adjust saturation 2", adjust.saturation / 100f
        )
        return if (adjust.exposure != 0f) adjustExposure(bm, adjust)
        else if (adjust.sharpen != 0f) adjustSharpen(bm, adjust)
        else if (adjust.vignette != 0f) adjustVignette(bm, adjust)
        else bm
    }

    private fun adjustExposure(bitmap: Bitmap?, adjust: AdjustModel): Bitmap {
        val bm = CGENativeLibrary.filterImage_MultipleEffects(
            bitmap, "@adjust exposure 0.62", adjust.exposure / 100f
        )
        return  if (adjust.sharpen != 0f) adjustSharpen(bm, adjust)
        else if (adjust.vignette != 0f) adjustVignette(bm, adjust)
        else bm
    }

    private fun adjustSharpen(bitmap: Bitmap?, adjust: AdjustModel): Bitmap {
        val bm = CGENativeLibrary.filterImage_MultipleEffects(
            bitmap, "@adjust sharpen 4.33 2 ", adjust.sharpen / 100f
        )
        return if (adjust.vignette != 0f) adjustVignette(bm, adjust)
        else bm
    }

    private fun adjustVignette(bitmap: Bitmap?, adjust: AdjustModel): Bitmap {
        return CGENativeLibrary.filterImage_MultipleEffects(
            bitmap, "@vignette 0.1 0.9", -adjust.vignette / 100f
        )
    }
}