package com.tattoo.tattoomaker.on.myphoto.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.databinding.ItemAddTextBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.model.text.FontModel

class FontTextAdapter(private val context: Context): RecyclerView.Adapter<FontTextAdapter.FontTextHolder>() {

    private var callBack: ICallBackItem? = null
    private var lstFont = mutableListOf<FontModel>()

    fun setData(lstFont: MutableList<FontModel>) {
        this.lstFont = lstFont

        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontTextHolder =
        FontTextHolder(ItemAddTextBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(holder: FontTextHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = if (lstFont.isNotEmpty()) lstFont.size else 0

    inner class FontTextHolder(private val binding: ItemAddTextBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            val font = lstFont[position]

            if (font.isSelected)
                binding.root.apply {
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    gravity = Gravity.CENTER
                    setBackgroundResource(R.drawable.bg_btn_enable_2)
                }
            else
                binding.root.apply {
                    setTextColor(ContextCompat.getColor(context, R.color.black_text))
                    gravity = Gravity.CENTER
                    setBackgroundResource(R.drawable.bg_btn_disable)
                }

            binding.root.typeface = Typeface.createFromAsset(context.assets, "font_text/${font.nameFont}")

            binding.root.setOnUnDoubleClickListener {
                setCurrent(font.nameFont)
                callBack?.callBack(font, position)
            }
        }
    }

    fun setCurrent(nameFont: String) {
        for (font in lstFont) font.isSelected = font.nameFont == nameFont
        notifyChange()
    }

    fun getPosition(nameFont: String): Int {
        val tmp = lstFont.filter { it.nameFont == nameFont }
        return if (tmp.isNotEmpty()) lstFont.indexOf(tmp[0]) else -1
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange() {
        notifyDataSetChanged()
    }
}