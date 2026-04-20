package com.tattoo.tattoomaker.on.myphoto.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.scale
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.model.TattooModel
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.viewcustom.CustomDrawPathData
import androidx.core.net.toUri
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tattoo.tattoomaker.on.myphoto.MyApp
import com.tattoo.tattoomaker.on.myphoto.databinding.ItemTattooBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.gone
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.extensions.visible
import com.tattoo.tattoomaker.on.myphoto.helper.Constant.URI_ASSETS
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class TattooAdapter @Inject constructor (@ActivityContext private val context: Context): RecyclerView.Adapter<TattooAdapter.TattooHolder>() {

    private var lstTattoo: MutableList<TattooModel> = mutableListOf()
    var callBack: ICallBackItem? = null

    fun setData(lstData: MutableList<TattooModel>) {
        lstTattoo = lstData

        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TattooHolder =
        TattooHolder(ItemTattooBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(holder: TattooHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = if (lstTattoo.isNotEmpty()) lstTattoo.size else 0

    inner class TattooHolder(private val binding: ItemTattooBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            val tattoo = lstTattoo[position]

            binding.vPath.gone()
            binding.iv.gone()

            if (!tattoo.isPremium) {
                binding.vPath.visible()
                binding.vPath.setDataPath(tattoo.lstPathData)
            } else {
                binding.iv.visible()
                Glide.with(context)
                    .asBitmap()
                    .load("$URI_ASSETS${tattoo.nameFolder}/${tattoo.name}".toUri())
                    .placeholder(R.drawable.im_place_holder)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            val bm = resource.scale(128, 128 * resource.height / resource.width, true)
                            binding.iv.setImageBitmap(bm)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }

            binding.root.setOnUnDoubleClickListener { callBack?.callBack(tattoo, position) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange() {
        notifyDataSetChanged()
    }
}