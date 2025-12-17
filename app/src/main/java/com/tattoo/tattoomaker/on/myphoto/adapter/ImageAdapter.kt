package com.tattoo.tattoomaker.on.myphoto.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tattoo.tattoomaker.on.myphoto.MyApp
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.databinding.ItemImageBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.helper.Constant.URI_ASSETS
import com.tattoo.tattoomaker.on.myphoto.model.FilterModel
import com.tattoo.tattoomaker.on.myphoto.model.FrameModel

class ImageAdapter(private val context: Context): RecyclerView.Adapter<ImageAdapter.ImageHolder>() {

    companion object {
        const val TYPE_FRAME = 0
        const val TYPE_IMAGE = 1
    }

    var callBack: ICallBackItem? = null
    private var lstData: MutableList<Any> = mutableListOf()

    fun setData(lstData: MutableList<Any>) {
        this.lstData = lstData

        notifyChange()
    }

    override fun getItemViewType(position: Int): Int = when(lstData[position]) {
        is FrameModel -> TYPE_FRAME
        else -> TYPE_IMAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder =
        ImageHolder(ItemImageBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        when(getItemViewType(position)) {
            TYPE_FRAME -> holder.onBindFrame(position)
            else -> holder.onBindFilter(position)
        }
    }

    override fun getItemCount(): Int = if (lstData.isNotEmpty()) lstData.size else 0

    inner class ImageHolder(private val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBindFrame(position: Int) {
            val frame = lstData[position] as FrameModel

            Glide.with(context)
                .load("$URI_ASSETS${frame.folder}/${frame.name}".toUri())
                .placeholder(R.drawable.im_place_holder)
                .into(binding.iv)

            binding.iv.setOnUnDoubleClickListener { callBack?.callBack(frame, position) }
        }

        fun onBindFilter(position: Int) {
            val filter = lstData[position] as FilterModel

            if (!filter.isCheck)
                binding.ivFilter.apply {
                    borderColor = ContextCompat.getColor(context, R.color.white)
                    borderWidth = 0.34f * MyApp.w
                }
            else binding.ivFilter.apply {
                borderColor = ContextCompat.getColor(context, R.color.color_main)
                borderWidth = 0.34f * MyApp.w
            }

            binding.ivFilter.setImageBitmap(filter.bitmap)

            binding.ivFilter.setOnUnDoubleClickListener {
                callBack?.callBack(filter, position)
                setCurrent(position)
            }
        }
    }

    fun setCurrent(pos: Int) {
        for (i in lstData.indices) (lstData[i] as FilterModel).isCheck = i == pos

        notifyChange()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange() {
        notifyDataSetChanged()
    }
}