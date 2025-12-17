package com.tattoo.tattoomaker.on.myphoto.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tattoo.tattoomaker.on.myphoto.MyApp
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.databinding.ItemColorBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.model.ColorModel
import com.tattoo.tattoomaker.on.myphoto.utils.Utils

class ColorAdapter(private val context: Context) : RecyclerView.Adapter<ColorAdapter.ColorHolder>() {

    var callBack: ICallBackItem? = null
    private var lstColor = mutableListOf<ColorModel>()

    fun setData(lstColor: MutableList<ColorModel>) {
        this.lstColor = lstColor

        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder =
        ColorHolder(ItemColorBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = if (lstColor.isNotEmpty()) lstColor.size else 0

    inner class ColorHolder(private val binding: ItemColorBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            val color = lstColor[position]

            if (color.colorStart == null && color.colorEnd == null)
                binding.root.setImageResource(R.drawable.ic_pick_color)
            else {
                if (color.isCheck) binding.root.setImageResource(R.drawable.ic_select_color)
                else binding.root.setImageResource(R.drawable.ic_border_color)

                binding.root.background = Utils.createBackground(
                    intArrayOf(color.colorStart!!, color.colorEnd!!),
                    (2f * MyApp.w).toInt(),
                    -1, -1
                )
            }

            binding.root.setOnUnDoubleClickListener {
                callBack?.callBack(color, position)
                setCurrent(color)
            }
        }
    }

    fun setCurrent(colorModel: ColorModel) {
        for (color in lstColor)
            color.isCheck =
                (color.colorStart == colorModel.colorStart && color.colorEnd == colorModel.colorEnd)

        notifyChange()
    }

    fun getPosition(colorModel: ColorModel): Int {
        val tmp = lstColor.filter { color ->
            color.colorStart == colorModel.colorStart
                    && color.colorEnd == colorModel.colorEnd
                    && color.direc == colorModel.direc
        }
        return if (tmp.isNotEmpty()) lstColor.indexOf(tmp[0]) else -1
    }

    fun setCurrent(color: Int) {
        for (c in lstColor)
            c.isCheck = (c.colorStart == color && c.colorEnd == color)

        notifyChange()
    }

    fun getPosition(color: Int): Int {
        val tmp = lstColor.filter { c -> c.colorStart == color && c.colorEnd == color }
        return if (tmp.isNotEmpty()) lstColor.indexOf(tmp[0]) else -1
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange() {
        notifyDataSetChanged()
    }
}