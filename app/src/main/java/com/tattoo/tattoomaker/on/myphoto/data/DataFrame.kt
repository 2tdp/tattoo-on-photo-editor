package com.tattoo.tattoomaker.on.myphoto.data

import android.content.Context
import com.tattoo.tattoomaker.on.myphoto.model.FrameModel
import com.tattoo.tattoomaker.on.myphoto.model.TattooPremiumModel
import java.io.IOException

object DataFrame {

    fun getDataFrame(context: Context, name: String): ArrayList<FrameModel> {
        val lstFrame = ArrayList<FrameModel>()
        try {
            val f = context.assets.list(name)
            for (s in f!!) lstFrame.add(FrameModel(s!!, name, false))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return lstFrame
    }
}