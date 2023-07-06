package com.tattoo.tattoomaker.on.myphoto.model

import android.graphics.Bitmap
import java.io.Serializable

class FilterModel(
    var bitmap: Bitmap?,
    var nameFilter: String,
    var parameterFilter: String,
    var isCheck: Boolean
) : Serializable