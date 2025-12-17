package com.tattoo.tattoomaker.on.myphoto.data

import android.content.Context
import com.tattoo.tattoomaker.on.myphoto.model.FrameModel
import java.io.IOException

object DataFrame {

    fun getDataFrame(context: Context, name: String): MutableList<Any> {
        val lstFrame = mutableListOf<FrameModel>()
        try {
            context.assets.list(name)?.let { folder ->
                for (s in folder) lstFrame.add(FrameModel(s.toString(), name, false))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return mutableListOf<Any>().apply { addAll(lstFrame) }
    }
}