package com.tattoo.tattoomaker.on.myphoto.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.addview.viewitem.ViewItemBucket
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.model.picture.BucketPicModel
import java.util.ArrayList

class BucketAdapter(context: Context, callBack: ICallBackItem) :
    RecyclerView.Adapter<BucketAdapter.BucketHolder>() {

    private var context: Context
    private var lstBucket: ArrayList<BucketPicModel>
    private val callBack: ICallBackItem
    private var w = 0F

    init {
        this.context = context
        this.callBack = callBack

        w = context.resources.displayMetrics.widthPixels / 100f
        lstBucket = ArrayList()
    }

    fun setData(lstBucket: ArrayList<BucketPicModel>) {
        this.lstBucket = lstBucket

        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BucketHolder {
        return BucketHolder(ViewItemBucket(context))
    }

    override fun onBindViewHolder(holder: BucketHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        if (lstBucket.isNotEmpty()) return lstBucket.size
        return 0
    }

    inner class BucketHolder(itemView: ViewItemBucket) : RecyclerView.ViewHolder(itemView) {
        var viewItemBucket: ViewItemBucket

        init {
            this.viewItemBucket = itemView

            viewItemBucket.layoutParams =
                RelativeLayout.LayoutParams(-1, (20.556f * w).toInt()).apply {
                    bottomMargin = (4.44f * w).toInt()
                }
        }

        fun onBind(position: Int) {
            val bucket: BucketPicModel = lstBucket[position]
            viewItemBucket.setOnClickListener { callBack.callBack(bucket, position) }

            val index = 0
            Glide.with(context)
                .load(Uri.parse(bucket.lstPic[index].uri))
                .placeholder(R.drawable.im_place_holder)
                .into(viewItemBucket.ivThumb)

            viewItemBucket.viewNamePicture.tvName.text = bucket.bucket
            viewItemBucket.viewNamePicture.tvQuantity.text = bucket.lstPic.size.toString()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange() {
        notifyDataSetChanged()
    }
}