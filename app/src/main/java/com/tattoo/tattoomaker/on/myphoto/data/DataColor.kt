package com.tattoo.tattoomaker.on.myphoto.data

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.tattoo.tattoomaker.on.myphoto.MyApp
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.databinding.DialogPickerColorBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.extensions.setUpDialog
import com.tattoo.tattoomaker.on.myphoto.model.ColorModel

object DataColor {

    fun getListColor(context: Context): MutableList<ColorModel> {
        val lstColor = mutableListOf<ColorModel>()
        val arrColor = context.resources.getIntArray(R.array.lstColor)
        var i = arrColor.size - 1
        while (i >= 0) {
            lstColor.add(ColorModel(arrColor[i], arrColor[i]))
            i--
        }
        lstColor.reverse()
        lstColor.add(ColorModel(null, null))
        return lstColor
    }

    fun showDialogPickColor(context: Context, callBack: ICallBackItem) {
        val binding = DialogPickerColorBinding.inflate(LayoutInflater.from(context))

        val dialog = AlertDialog.Builder(context, R.style.SheetDialog).create()
        dialog.setUpDialog(binding.root, true)

        binding.root.layoutParams.width = (84.44f * MyApp.w).toInt()

        binding.tvCancel.setOnUnDoubleClickListener { dialog.dismiss() }
        binding.tvDone.setOnUnDoubleClickListener {
            val color = ColorModel(binding.vPicker.selectedColor, binding.vPicker.selectedColor)
            callBack.callBack(color, -1)
            dialog.dismiss()
        }
    }
}