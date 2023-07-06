package com.tattoo.tattoomaker.on.myphoto.utils

import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import kotlin.math.cos
import kotlin.math.sin

object ColorFilterGenerator {
    /**
     * Creates a HUE ajustment ColorFilter
     *
     * @param value degrees to shift the hue.
     * @return
     * @see http://groups.google.com/group/android-developers/browse_thread/thread/9e215c83c3819953
     *
     * @see http://gskinner.com/blog/archives/2007/12/colormatrix_cla.html
     */
    fun adjustHue(value: Float): ColorFilter {
        val cm = ColorMatrix()
        adjustHue(cm, value)
        return ColorMatrixColorFilter(cm)
    }

    /**
     * @param cm
     * @param value
     * @see http://groups.google.com/group/android-developers/browse_thread/thread/9e215c83c3819953
     *
     * @see http://gskinner.com/blog/archives/2007/12/colormatrix_cla.html
     */
    private fun adjustHue(cm: ColorMatrix, value: Float) {
        var value = value
        value = cleanValue(value, 360f) / 360f * Math.PI.toFloat()
        if (value == 0f) return

        val cosVal = cos(value.toDouble()).toFloat()
        val sinVal = sin(value.toDouble()).toFloat()

        val lumR = 0.213f
        val lumG = 0.715f
        val lumB = 0.072f

        val mat = floatArrayOf(
            lumR + cosVal * (1 - lumR) + sinVal * -lumR, lumG + cosVal * -lumG + sinVal * -lumG, lumB + cosVal * -lumB + sinVal * (1 - lumB), 0f, 0f,
            lumR + cosVal * -lumR + sinVal * 0.143f, lumG + cosVal * (1 - lumG) + sinVal * 0.140f, lumB + cosVal * -lumB + sinVal * -0.283f, 0f, 0f,
            lumR + cosVal * -lumR + sinVal * -(1 - lumR), lumG + cosVal * -lumG + sinVal * lumG, lumB + cosVal * (1 - lumB) + sinVal * lumB, 0f, 0f,
            0f, 0f, 0f, 1f, 0f,
            0f, 0f, 0f, 0f, 1f
        )
        cm.postConcat(ColorMatrix(mat))
    }

    private fun cleanValue(p_val: Float, p_limit: Float): Float {
        return p_limit.coerceAtMost((-p_limit).coerceAtLeast(p_val))
    }
}