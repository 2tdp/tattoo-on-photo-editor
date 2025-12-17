package com.tattoo.tattoomaker.on.myphoto.model

import java.io.Serializable

data class ShadowModel(
    var xPos: Float = 0f,
    var yPos: Float = 0f,
    var blur: Float = 0f,
    var colorBlur: Int = -1
) : Serializable