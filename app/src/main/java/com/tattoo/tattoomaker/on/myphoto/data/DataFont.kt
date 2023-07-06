package com.tattoo.tattoomaker.on.myphoto.data

import android.content.Context
import com.tattoo.tattoomaker.on.myphoto.model.text.FontModel
import java.io.IOException
import java.lang.StringBuilder
import java.util.*

object DataFont {

    fun getDataFont(context: Context): ArrayList<FontModel> {
        val lstFont = ArrayList<FontModel>()
        try {
            val f = context.assets.list("font_text/")
            for (s in f!!) {
                if (s != "solway_medium.ttf") lstFont.add(FontModel(s, false))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        lstFont.add(0, FontModel("solway_medium.ttf", true))
        return lstFont
    }

    fun getDataTypeFont(context: Context, nameFont: String): String {
        var font = ""
        try {
            val f = context.assets.list("font_text/")
            for (s in f!!) {
                val arr = s.split("_").toTypedArray()
                var style = StringBuilder()
                for (i in 1 until arr.size) {
                    if (i == arr.size - 1)
                        style.append(" ").append(arr[i].replace(".ttf", " "))
                    else style.append(" ").append(arr[i])
                    style = StringBuilder(
                        style.toString().substring(0, 2)
                            .uppercase(Locale.getDefault()) + style.toString().substring(2)
                            .lowercase(Locale.getDefault())
                    )
                }
//                lstStyle.add(TypeFontModel(style.toString().trim { it <= ' ' }, nameFont, false))
                font = style.toString().trim { it <= ' ' }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return font
    }
}