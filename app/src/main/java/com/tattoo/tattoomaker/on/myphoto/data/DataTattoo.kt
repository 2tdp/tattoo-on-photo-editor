package com.tattoo.tattoomaker.on.myphoto.data

import android.content.Context
import android.graphics.Path
import androidx.core.graphics.PathParser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tattoo.tattoomaker.on.myphoto.model.ColorModel
import com.tattoo.tattoomaker.on.myphoto.model.ShadowModel
import com.tattoo.tattoomaker.on.myphoto.model.TattooModel
import java.io.IOException

object DataTattoo {

    fun getDataTattooPremium(context: Context, name: String): MutableList<TattooModel> {
        val lstTattooPremium = mutableListOf<TattooModel>()
        try {
            context.assets.list(name)?.let { folder ->
                for (s in folder) {
                    lstTattooPremium.add(
                        TattooModel(
                            0, s.toString(), name, mutableListOf(),
                            ColorModel(), ShadowModel(), 255, false, false, false, null
                        )
                    )
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return lstTattooPremium
    }

    fun getDataTattoo(context: Context, name: String): MutableList<TattooModel> {
        val lstTattoo = mutableListOf<TattooModel>()
        try {
            context.assets.list(name)?.let { folder ->
                for (s in folder) {
                    lstTattoo.add(
                        TattooModel(
                            0, s, name, getPathDataTattoo(context, s, name),
                            ColorModel(), ShadowModel(), 255, false, false, false, null
                        )
                    )
                }
            }

            lstTattoo.addAll(getDataTattooPremium(context, "tattoo/tattoo_premium"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return lstTattoo
    }

    private fun createPath(lstPath: ArrayList<String>): Path {
        val path = Path()
        for (p in lstPath) {
            path.addPath(PathParser.createPathFromPathData(p))
        }

        return path
    }

    private fun getPathDataTattoo(context: Context, name: String, nameFolder: String): ArrayList<String> {
        var tContents = ""
        try {
            val stream = context.assets.open("$nameFolder/$name")
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            tContents = String(buffer)
        } catch (ignored: IOException) {
        }
        if (tContents.isNotEmpty()) {
            val type = object : TypeToken<ArrayList<String>>() {}.type
            return Gson().fromJson(tContents, type)
        }
        return ArrayList()
    }
}