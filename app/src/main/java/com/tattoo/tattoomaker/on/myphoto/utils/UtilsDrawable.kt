package com.tattoo.tattoomaker.on.myphoto.utils

import android.content.Context
import android.widget.ImageView
import com.tattoo.tattoomaker.on.myphoto.utils.designvector.VectorChildFinder
import com.tattoo.tattoomaker.on.myphoto.utils.designvector.VectorDrawableCompat

object UtilsDrawable {

    fun changeIcon(context: Context, namePath: String, quantityPath: Int, resPath: Int,
                   imageView: ImageView, colorBackground: Int, color: Int) {

        var vectorChild: VectorChildFinder? = null
        var path: VectorDrawableCompat.VFullPath

        for (i in 0 until quantityPath) {
            if (vectorChild == null) vectorChild = VectorChildFinder(context, resPath, imageView)
            path = vectorChild.findPathByName(namePath + (i + 1))
            if (i == 0) path.fillColor = colorBackground
            else path.fillColor = color
        }
    }
}