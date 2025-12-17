package com.tattoo.tattoomaker.on.myphoto.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.model.TattooModel
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.viewcustom.CustomDrawPathData
import androidx.core.net.toUri
import com.tattoo.tattoomaker.on.myphoto.MyApp
import com.tattoo.tattoomaker.on.myphoto.databinding.ItemTattooBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.helper.Constant.URI_ASSETS
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class TattooAdapter(private val context: Context, private val callBack: ICallBackItem): RecyclerView.Adapter<TattooAdapter.TattooHolder>() {

    private var lstTattoo: MutableList<TattooModel> = mutableListOf()

    fun setData(lstTattoo: MutableList<TattooModel>) {
        this.lstTattoo = lstTattoo

        this.lstTattoo.reverse()
        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TattooHolder =
        TattooHolder(ItemTattooBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(holder: TattooHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = if (lstTattoo.isNotEmpty())  lstTattoo.size else 0

    inner class TattooHolder(private val binding: ItemTattooBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            val tattoo = lstTattoo[position]

            if (!tattoo.isPremium) {
                binding.vPath.setDataPath(tattoo.lstPathData)
            } else {
                Glide.with(context)
                    .load("$URI_ASSETS${tattoo.nameFolder}/${tattoo.name}".toUri())
                    .placeholder(R.drawable.im_place_holder)
                    .into(binding.iv)
            }

            binding.root.setOnUnDoubleClickListener { callBack.callBack(tattoo, position) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange() {
        notifyDataSetChanged()
    }
}