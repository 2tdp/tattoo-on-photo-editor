package com.tattoo.tattoomaker.on.myphoto.data

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.addview.viewdialog.ViewDialogColor
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.model.ColorModel

object DataColor {

    fun getListColor(context: Context): ArrayList<ColorModel> {
        val lstColor = ArrayList<ColorModel>()
        val arrColor = context.resources.getIntArray(R.array.lstColor)
        var i = arrColor.size - 1
        while (i >= 0) {
            lstColor.add(ColorModel(arrColor[i], arrColor[i], 0, false))
            i--
        }
        lstColor.reverse()
        return lstColor
    }

    fun showDialogPickColor(context: Context, callBack: ICallBackItem) {
        val w = context.resources.displayMetrics.widthPixels
        val viewDialogColor = ViewDialogColor(context)

        val dialog = AlertDialog.Builder(context, R.style.SheetDialog).create()
        dialog.setView(viewDialogColor)
        dialog.setCancelable(true)
        dialog.show()

        viewDialogColor.layoutParams.width = (75f * w / 100).toInt()
        viewDialogColor.layoutParams.height = (91.389f * w / 100).toInt()

        viewDialogColor.tvCancel.setOnClickListener { dialog.cancel() }
        viewDialogColor.tvOk.setOnClickListener {
            val color = ColorModel(
                viewDialogColor.colorPicker.selectedColor,
                viewDialogColor.colorPicker.selectedColor,
                0, false
            )
            callBack.callBack(color, -1)
            dialog.cancel()
        }
    }
}