package com.tattoo.tattoomaker.on.myphoto.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.model.FilterModel
import com.tattoo.tattoomaker.on.myphoto.model.FrameModel
import com.tattoo.tattoomaker.on.myphoto.model.TattooPremiumModel
import com.tattoo.tattoomaker.on.myphoto.utils.Constant
import com.tattoo.tattoomaker.on.myphoto.utils.Utils

class ImageAdapter(context: Context, type: String, callBack: ICallBackItem): RecyclerView.Adapter<ImageAdapter.TattooPremiumHolder>() {

    private val context: Context
    private val type: String
    private val callBack: ICallBackItem
    private var lstTattooPremium: ArrayList<TattooPremiumModel>
    private var lstFilter: ArrayList<FilterModel>
    private var lstFrame: ArrayList<FrameModel>
    private var w = 0F

    init {
        this.context = context
        this.type = type
        this.callBack = callBack

        lstTattooPremium = ArrayList()
        lstFilter = ArrayList()
        lstFrame = ArrayList()
        w = context.resources.displayMetrics.widthPixels / 100F
    }

    fun setDataTattooPremium(lstTattooPremium: ArrayList<TattooPremiumModel>) {
        this.lstTattooPremium = lstTattooPremium

        notifyChange()
    }

    fun setDataFilter(lstFilter: ArrayList<FilterModel>) {
        this.lstFilter = lstFilter

        notifyChange()
    }

    fun setDataFrame(lstFrame: ArrayList<FrameModel>) {
        this.lstFrame = lstFrame

        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TattooPremiumHolder {
        return TattooPremiumHolder(ImageView(context), RoundedImageView(context))
    }

    override fun onBindViewHolder(holder: TattooPremiumHolder, position: Int) {
        if (type == Constant.TATTOO_PREMIUM) holder.onBindTattooPremium(position)
        if (type == Constant.FRAME) holder.onBindFrame(position)
        if (type == Constant.BACKGROUND_FILTER) holder.onBindFilter(position)
    }

    override fun getItemCount(): Int {
        if (type == Constant.TATTOO_PREMIUM && lstTattooPremium.isNotEmpty())
            return lstTattooPremium.size

        if (type == Constant.FRAME && lstFrame.isNotEmpty())
            return lstFrame.size

        if (type == Constant.BACKGROUND_FILTER && lstFilter.isNotEmpty())
            return lstFilter.size
        return 0
    }

    inner class TattooPremiumHolder(viewItem: ImageView, ivFilter: RoundedImageView): RecyclerView.ViewHolder(
        if (type == Constant.TATTOO_PREMIUM || type == Constant.FRAME) viewItem
        else ivFilter
    ) {

        private val iv: ImageView
        private val ivFilter: RoundedImageView

        init {
            this.iv = viewItem
            this.ivFilter = ivFilter
            when (type) {
                Constant.TATTOO_PREMIUM -> iv.apply {
                    background = Utils.createBackground(
                        intArrayOf(Color.TRANSPARENT), (2f * w).toInt(), (0.55f * w).toInt(), Color.WHITE)
                    setPadding(w.toInt())
                }
                Constant.FRAME -> iv.apply {
                    background =
                        Utils.createBackground(intArrayOf(Color.WHITE), (2f * w).toInt(), -1, -1)
                    setPadding(w.toInt())
                }
                Constant.BACKGROUND_FILTER -> ivFilter.apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    cornerRadius = 1.5f * w
                    background = Utils.createBackground(
                        intArrayOf(Color.TRANSPARENT), (2f * w).toInt(), (0.55f * w).toInt(), Color.WHITE)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }
        }

        fun onBindTattooPremium(position: Int) {
            val tattooPremium = lstTattooPremium[position]

            when (position) {
                0 -> iv.layoutParams =
                    RecyclerView.LayoutParams((7.778f * w).toInt(), (10.556f * w).toInt()).apply {
                        leftMargin = (4.44f * w).toInt()
                        rightMargin = (2.22f * w).toInt()
                    }
                lstTattooPremium.size - 1 -> iv.layoutParams =
                    RecyclerView.LayoutParams((7.778f * w).toInt(), (10.556f * w).toInt()).apply {
                        leftMargin = (2.22f * w).toInt()
                        rightMargin = (4.44f * w).toInt()
                    }
                else -> iv.layoutParams =
                    RecyclerView.LayoutParams((7.778f * w).toInt(), (10.556f * w).toInt()).apply {
                        leftMargin = (2.22f * w).toInt()
                        rightMargin = (2.22f * w).toInt()
                    }
            }

            if (tattooPremium.isSelected)
                iv.background =
                        Utils.createBackground(intArrayOf(Color.TRANSPARENT),
                            (2f * w).toInt(),
                            (0.55f * w).toInt(),
                            ContextCompat.getColor(context, R.color.color_main))

            Glide.with(context)
                .load(Uri.parse("file:///android_asset/${tattooPremium.folder}/${tattooPremium.nameTattoo}"))
                .placeholder(R.drawable.im_place_holder)
                .into(iv)

            itemView.setOnClickListener { callBack.callBack(tattooPremium, position) }
        }

        fun onBindFrame(position: Int) {
            val frame = lstFrame[position]

            when (position) {
                0 -> iv.layoutParams =
                    RecyclerView.LayoutParams((7.778f * w).toInt(), (10.556f * w).toInt()).apply {
                        leftMargin = (4.44f * w).toInt()
                        rightMargin = (2.22f * w).toInt()
                    }
                lstTattooPremium.size - 1 -> iv.layoutParams =
                    RecyclerView.LayoutParams((7.778f * w).toInt(), (10.556f * w).toInt()).apply {
                        leftMargin = (2.22f * w).toInt()
                        rightMargin = (4.44f * w).toInt()
                    }
                else -> iv.layoutParams =
                    RecyclerView.LayoutParams((7.778f * w).toInt(), (10.556f * w).toInt()).apply {
                        leftMargin = (2.22f * w).toInt()
                        rightMargin = (2.22f * w).toInt()
                    }
            }

            Glide.with(context)
                .load(Uri.parse("file:///android_asset/${frame.folder}/${frame.name}"))
                .placeholder(R.drawable.im_place_holder)
                .into(iv)

            itemView.setOnClickListener { callBack.callBack(frame, position) }
        }

        fun onBindFilter(position: Int) {
            val filter = lstFilter[position]

            when (position) {
                0 -> ivFilter.layoutParams =
                    RecyclerView.LayoutParams((7.778f * w).toInt(), (10.556f * w).toInt()).apply {
                        leftMargin = (4.44f * w).toInt()
                        rightMargin = (2.22f * w).toInt()
                    }
                lstTattooPremium.size - 1 -> ivFilter.layoutParams =
                    RecyclerView.LayoutParams((7.778f * w).toInt(), (10.556f * w).toInt()).apply {
                        leftMargin = (2.22f * w).toInt()
                        rightMargin = (4.44f * w).toInt()
                    }
                else -> ivFilter.layoutParams =
                    RecyclerView.LayoutParams((7.778f * w).toInt(), (10.556f * w).toInt()).apply {
                        leftMargin = (2.22f * w).toInt()
                        rightMargin = (2.22f * w).toInt()
                    }
            }

            if (!filter.isCheck)
                ivFilter.apply {
                    borderColor = ContextCompat.getColor(context, R.color.white)
                    borderWidth = 0.34f * w
                }
            else ivFilter.apply {
                borderColor = ContextCompat.getColor(context, R.color.color_main)
                borderWidth = 0.34f * w
            }

            ivFilter.setImageBitmap(filter.bitmap)

            itemView.setOnClickListener {
                callBack.callBack(filter, position)
                setCurrent(position)
            }
        }
    }

    fun setCurrent(pos: Int) {
        for (i in lstFilter.indices) lstFilter[i].isCheck = i == pos

        notifyChange()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange() {
        notifyDataSetChanged()
    }
}