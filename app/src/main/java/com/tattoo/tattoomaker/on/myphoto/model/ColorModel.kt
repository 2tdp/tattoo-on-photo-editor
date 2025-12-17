package com.tattoo.tattoomaker.on.myphoto.model

import android.graphics.Color
import java.io.Serializable

data class ColorModel(
    var colorStart: Int? = Color.BLACK,
    var colorEnd: Int? = Color.BLACK,
    var direc: Int = 0,
    var isCheck: Boolean = false
) : Serializable