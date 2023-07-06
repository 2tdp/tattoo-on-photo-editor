package com.tattoo.tattoomaker.on.myphoto.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout.LayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.model.ColorModel
import com.tattoo.tattoomaker.on.myphoto.utils.Utils

class ColorAdapter(context: Context, callBack: ICallBackItem) : RecyclerView.Adapter<ColorAdapter.ColorHolder>() {

    private val context: Context
    private val callBack: ICallBackItem
    private var lstColor: ArrayList<ColorModel>
    private var w = 0F

    init {
        this.context = context
        this.callBack = callBack

        lstColor = ArrayList()
        w = context.resources.displayMetrics.widthPixels / 100F
    }

    fun setData(lstColor: ArrayList<ColorModel>) {
        this.lstColor = lstColor

        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder {
        return ColorHolder(RoundedImageView(context))
    }

    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        if (lstColor.isNotEmpty()) return lstColor.size
        return 0
    }

    inner class ColorHolder(itemView: RoundedImageView) : RecyclerView.ViewHolder(itemView) {

        private val iv: RoundedImageView

        init {
            this.iv = itemView

            iv.apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                cornerRadius = 1.5f * w
            }
        }

        fun onBind(position: Int) {
            val color = lstColor[position]

            when (position) {
                0 -> iv.layoutParams =
                    LayoutParams((7.778f * w).toInt(), (7.778f * w).toInt()).apply {
                        leftMargin = (4.44f * w).toInt()
                        rightMargin = (2.22f * w).toInt()
                    }
                lstColor.size - 1 -> iv.layoutParams =
                    LayoutParams((7.778f * w).toInt(), (7.778f * w).toInt()).apply {
                        leftMargin = (2.22f * w).toInt()
                        rightMargin = (4.44f * w).toInt()
                    }
                else -> iv.layoutParams =
                    LayoutParams((7.778f * w).toInt(), (7.778f * w).toInt()).apply {
                        leftMargin = (2.22f * w).toInt()
                        rightMargin = (2.22f * w).toInt()
                    }
            }

            if (color.isCheck) iv.setImageResource(R.drawable.ic_select_color)
            else iv.setImageResource(R.drawable.ic_border_color)

            iv.background = Utils.createBackground(
                intArrayOf(color.colorStart, color.colorEnd),
                (2f * w).toInt(),
                -1, -1
            )

            iv.setOnClickListener {
                callBack.callBack(color, position)
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
        for (color in lstColor)
            if (color.colorStart == colorModel.colorStart
                && color.colorEnd == colorModel.colorEnd
                && color.direc == colorModel.direc
            ) {
                val index = lstColor.indexOf(color)
                return if (index != 0 || index != lstColor.size - 1) (index - 1)
                else index
            }
        return -1
    }

    fun setCurrent(color: Int) {
        for (c in lstColor)
            c.isCheck = (c.colorStart == color && c.colorEnd == color)

        notifyChange()
    }

    fun getPosition(color: Int): Int {
        for (c in lstColor)
            if (c.colorStart == color && c.colorEnd == color) {
                val index = lstColor.indexOf(c)
                return if (index != 0 || index != lstColor.size - 1) (index - 1)
                else index
            }
        return -1
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange() {
        notifyDataSetChanged()
    }
}