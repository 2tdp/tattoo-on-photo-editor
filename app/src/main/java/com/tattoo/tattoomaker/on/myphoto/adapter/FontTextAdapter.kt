package com.tattoo.tattoomaker.on.myphoto.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.addview.viewedit.ViewAddText
import com.tattoo.tattoomaker.on.myphoto.addview.viewhome.ViewChooseBackground
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.model.text.FontModel
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsView.textCustom

class FontTextAdapter(context: Context, callBack: ICallBackItem): RecyclerView.Adapter<FontTextAdapter.FontTextHolder>() {

    private val context: Context
    private val callBack: ICallBackItem
    private var lstFont: ArrayList<FontModel>
    private var w = 0F

    init {
        this.context = context
        this.callBack = callBack

        lstFont = ArrayList()
        w = context.resources.displayMetrics.widthPixels / 100F
    }

    fun setData(lstFont: ArrayList<FontModel>) {
        this.lstFont = lstFont

        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontTextHolder {
        return FontTextHolder(TextView(context))
    }

    override fun onBindViewHolder(holder: FontTextHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        if (lstFont.isNotEmpty()) return lstFont.size
        return 0
    }

    inner class FontTextHolder(itemView: TextView): RecyclerView.ViewHolder(itemView) {

        private val tvFont: TextView

        init {
            this.tvFont = itemView
            tvFont.layoutParams =
                RecyclerView.LayoutParams((27.778f * w).toInt(), (12.22f * w).toInt()).apply {
                    leftMargin = (2.22f * w).toInt()
                    rightMargin = (2.22f * w).toInt()
                    bottomMargin = (4.44f * w).toInt()
            }
        }

        fun onBind(position: Int) {
            val font = lstFont[position]

            if (font.isSelected)
                tvFont.apply {
                    textCustom(
                        resources.getString(R.string.tattoo),
                        ContextCompat.getColor(context, R.color.white),
                        5.556f * w, "solway_medium", context
                    )
                    gravity = Gravity.CENTER
                    background = Utils.createBackground(
                        intArrayOf(
                            ContextCompat.getColor(context, R.color.main_color_1),
                            ContextCompat.getColor(context, R.color.main_color_2)
                        ), (1.5f * w).toInt(), -1, -1
                    )
                }
            else
                tvFont.apply {
                    textCustom(
                        resources.getString(R.string.tattoo),
                        ContextCompat.getColor(context, R.color.black_text),
                        5.556f * w, "solway_medium", context
                    )
                    gravity = Gravity.CENTER
                    background = Utils.createBackground(
                        intArrayOf(Color.TRANSPARENT), (1.5f * w).toInt(),
                        (0.34f * w).toInt(), ContextCompat.getColor(context, R.color.black_text)
                    )
                }

            tvFont.typeface = Typeface.createFromAsset(context.assets, "font_text/${font.nameFont}")

            tvFont.setOnClickListener {
                callBack.callBack(font, position)
                setCurrent(font.nameFont)
            }
        }
    }

    fun setCurrent(nameFont: String) {
        for (font in lstFont) font.isSelected = font.nameFont == nameFont
        notifyChange()
    }

    fun getPosition(nameFont: String): Int {
        for (font in lstFont)
            if (font.nameFont == nameFont) {
                val index = lstFont.indexOf(font)
                return if (index != 0 || index != lstFont.size - 1) (index - 1)
                else index
            }
        return -1
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange() {
        notifyDataSetChanged()
    }
}