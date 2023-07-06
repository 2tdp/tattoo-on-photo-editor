package com.tattoo.tattoomaker.on.myphoto.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tattoo.tattoomaker.on.myphoto.addview.viewedit.ViewTattoos
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.model.TattooModel
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.viewcustom.CustomDrawPathData

class TattooAdapter(context: Context, callBack: ICallBackItem): RecyclerView.Adapter<TattooAdapter.TattooHolder>() {

    private val context: Context
    private val callBack: ICallBackItem
    private var lstTattoo: ArrayList<TattooModel>

    private var w = 0F

    init {
        this.context = context
        this.callBack = callBack

        lstTattoo = ArrayList()
        w = context.resources.displayMetrics.widthPixels / 100F
    }

    fun setData(lstTattoo: ArrayList<TattooModel>) {
        this.lstTattoo = lstTattoo

        this.lstTattoo.reverse()
        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TattooHolder {
        return TattooHolder(CustomDrawPathData(context))
    }

    override fun onBindViewHolder(holder: TattooHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        if (lstTattoo.isNotEmpty()) return lstTattoo.size
        return 0
    }

    inner class TattooHolder(itemView: CustomDrawPathData): RecyclerView.ViewHolder(itemView) {

        private val vPath: CustomDrawPathData

        init {
            this.vPath = itemView
            vPath.apply {
                background =
                    Utils.createBackground(intArrayOf(Color.TRANSPARENT), (2f * w).toInt(), (0.55f * w).toInt(), Color.WHITE)
            }
        }

        fun onBind(position: Int) {
            val tattoo = lstTattoo[position]

            when (position) {
                0 -> vPath.layoutParams =
                    RecyclerView.LayoutParams((7.778f * w).toInt(), (10.556f * w).toInt()).apply {
                        leftMargin = (4.44f * w).toInt()
                        rightMargin = (2.22f * w).toInt()
                    }
                lstTattoo.size - 1 -> vPath.layoutParams =
                    RecyclerView.LayoutParams((7.778f * w).toInt(), (10.556f * w).toInt()).apply {
                        leftMargin = (2.22f * w).toInt()
                        rightMargin = (4.44f * w).toInt()
                    }
                else -> vPath.layoutParams =
                    RecyclerView.LayoutParams((7.778f * w).toInt(), (10.556f * w).toInt()).apply {
                        leftMargin = (2.22f * w).toInt()
                        rightMargin = (2.22f * w).toInt()
                    }
            }

            vPath.setDataPath(tattoo.lstPathData)

            vPath.setOnClickListener { callBack.callBack(tattoo, position) }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange() {
        notifyDataSetChanged()
    }
}