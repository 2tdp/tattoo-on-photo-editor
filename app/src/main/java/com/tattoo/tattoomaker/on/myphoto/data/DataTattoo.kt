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
                            ColorModel(), null, 255, false, false, true, null
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
            // name = "tattoo" → list() trả về các SUBFOLDER: ["tattoo_free", "tattoo_premium", ...]
            // Phải duyệt vào từng subfolder, rồi mới list file .json bên trong
            context.assets.list(name)?.let { subFolders ->
                for (subFolder in subFolders) {
                    val subFolderPath = "$name/$subFolder"

                    // Skip tattoo_premium — được load riêng bởi getDataTattooPremium
                    if (subFolder == "tattoo_premium" || !subFolder.contains("tattoo")) continue

                    // list file bên trong subfolder (vd: tattoo/tattoo_free → free_1.json, free_2.json...)
                    context.assets.list(subFolderPath)?.let { files ->
                        for (fileName in files) {
                            lstTattoo.add(
                                TattooModel(
                                    0, fileName, subFolderPath,
                                    getPathDataTattoo(context, fileName, subFolderPath),
                                    ColorModel(), null, 255, false, false, false, null
                                )
                            )
                        }
                    }
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (tContents.isNotEmpty()) {
            val type = object : TypeToken<ArrayList<String>>() {}.type
            return Gson().fromJson(tContents, type)
        }
        return ArrayList()
    }
}