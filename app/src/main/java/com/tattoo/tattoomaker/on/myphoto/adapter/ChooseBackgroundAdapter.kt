package com.tattoo.tattoomaker.on.myphoto.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.model.picture.PicModel
import com.tattoo.tattoomaker.on.myphoto.utils.Constant

class ChooseBackgroundAdapter(context: Context, callBack: ICallBackItem): RecyclerView.Adapter<ChooseBackgroundAdapter.ChooseBackgroundHolder>() {

    private val context: Context
    private val callBack: ICallBackItem
    private var lstPic: ArrayList<PicModel>

    private var w = 0F

    init {
        this.context = context
        this.callBack = callBack

        w = context.resources.displayMetrics.widthPixels / 100f
        lstPic = ArrayList()
    }

    fun setData(lstPic : ArrayList<PicModel>) {
        this.lstPic = lstPic

        this.lstPic.reverse()
        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseBackgroundHolder {
        return ChooseBackgroundHolder(RoundedImageView(context))
    }

    override fun onBindViewHolder(holder: ChooseBackgroundHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        if (lstPic.isNotEmpty()) return lstPic.size
        return 0
    }

    inner class ChooseBackgroundHolder(itemView: RoundedImageView): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                cornerRadius = 2.5f * w
            }
            itemView.layoutParams =
                RecyclerView.LayoutParams((27.778f * w).toInt(), (41.667f * w).toInt()).apply {
                    leftMargin = (1.94f * w).toInt()
                    rightMargin = (1.94f * w).toInt()
                    bottomMargin = (4.44f * w).toInt()
                }
        }

        fun onBind(position: Int) {
            val picture = lstPic[position]

            if (!picture.bucket.equals("tattoo_background"))
                Glide.with(context)
                    .asBitmap()
                    .load(picture.uri)
                    .placeholder(R.drawable.im_place_holder_rec)
                    .into(itemView as ImageView)
            else
                Glide.with(context)
                    .asBitmap()
                    .load("file:///android_asset/tattoo/background/${picture.uri}")
                    .placeholder(R.drawable.im_place_holder_rec)
                    .into(itemView as ImageView)

            itemView.setOnClickListener { callBack.callBack(picture, position) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange() {
        notifyDataSetChanged()
    }
}