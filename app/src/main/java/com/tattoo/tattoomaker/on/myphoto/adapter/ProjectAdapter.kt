package com.tattoo.tattoomaker.on.myphoto.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.addview.viewitem.ViewItemProject
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.model.ProjectModel

class ProjectAdapter(context: Context, callBack: ICallBackItem): RecyclerView.Adapter<ProjectAdapter.ProjectHolder> () {

    private val context: Context
    private val callBack: ICallBackItem
    private var lstProject: ArrayList<ProjectModel>
    private var isMultiChoose = false
    private var w = 0F

    init {
        this.context = context
        this.callBack = callBack

        lstProject = ArrayList()
        w = context.resources.displayMetrics.widthPixels / 100F
    }

    fun setDataProject(lstProject: ArrayList<ProjectModel>) {
        this.lstProject = lstProject

        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectHolder {
        return ProjectHolder(ViewItemProject(context))
    }

    override fun onBindViewHolder(holder: ProjectHolder, position: Int) {
        holder.onBindProject(position)
    }

    override fun getItemCount(): Int {
        return if (lstProject.isNotEmpty()) lstProject.size else 0
    }

    inner class ProjectHolder(itemView: ViewItemProject): RecyclerView.ViewHolder(itemView) {

        private val ivProject: ViewItemProject

        init {
            this.ivProject = itemView
            ivProject.layoutParams =
                LinearLayout.LayoutParams((27.778f * w).toInt(), (41.667f * w).toInt()).apply {
                    leftMargin = (2.22f * w).toInt()
                    rightMargin = (2.22f * w).toInt()
                    bottomMargin = (3.889f * w).toInt()
                }
        }

        fun onBindProject(position: Int) {
            val project = lstProject[position]

            if (isMultiChoose) ivProject.ivTick.visibility = View.VISIBLE
            else ivProject.ivTick.visibility = View.GONE

            Glide.with(context)
                .asBitmap()
                .load(project.uriSaved)
                .placeholder(R.drawable.im_place_holder)
                .into(ivProject.iv)

            itemView.setOnClickListener {
                if (isMultiChoose) {
                    if (!project.isSelect) {
                        ivProject.ivTick.setImageResource(R.drawable.ic_item_tick)
                        project.isSelect = true
                    } else {
                        ivProject.ivTick.setImageResource(R.drawable.ic_item_un_tick)
                        project.isSelect = false
                    }
                } else callBack.callBack(project, position)
            }

            itemView.setOnLongClickListener {
                callBack.callBack( project, -1)
                true
            }
        }
    }

    fun getSelected(): ArrayList<ProjectModel> {
        val lstProSelected = ArrayList<ProjectModel>()
        for (i in lstProject.indices) {
            if (lstProject[i].isSelect) lstProSelected.add(lstProject[i])
        }
        return lstProSelected
    }

    fun isMultiChoose(isMultiChoose: Boolean) {
        this.isMultiChoose = isMultiChoose

        notifyChange()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange(){
        notifyDataSetChanged()
    }
}