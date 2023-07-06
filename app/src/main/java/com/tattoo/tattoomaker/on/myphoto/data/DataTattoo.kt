package com.tattoo.tattoomaker.on.myphoto.data

import android.content.Context
import android.graphics.Color
import android.graphics.Path
import androidx.core.graphics.PathParser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tattoo.tattoomaker.on.myphoto.model.ColorModel
import com.tattoo.tattoomaker.on.myphoto.model.ShadowModel
import com.tattoo.tattoomaker.on.myphoto.model.TattooModel
import com.tattoo.tattoomaker.on.myphoto.model.TattooPremiumModel
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager
import java.io.IOException

object DataTattoo {

    fun getDataTattooPremium(context: Context, name: String): ArrayList<TattooPremiumModel> {
        val lstTattooPremium = ArrayList<TattooPremiumModel>()
        try {
            val f = context.assets.list(name)
            for (s in f!!) {
                lstTattooPremium.add(
                    TattooPremiumModel(0, s!!, name, 0f, 255, false, false, false, null)
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return lstTattooPremium
    }

    fun getDataTattoo(context: Context, name: String): ArrayList<TattooModel> {
        val lstTattoo = ArrayList<TattooModel>()
        try {
            val f = context.assets.list(name)
            for (s in f!!) {
                lstTattoo.add(
                    TattooModel(
                        0, s, name, getPathDataTattoo(context, s, name),
                        ColorModel(Color.BLACK, Color.BLACK, 0, false),
                        ShadowModel(0F, 0F, 0F, 0), 255, false,
                        false, false, null
                    )
                )
            }

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