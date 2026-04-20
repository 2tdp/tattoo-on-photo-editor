package com.tattoo.tattoomaker.on.myphoto.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.databinding.ItemProjectBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.gone
import com.tattoo.tattoomaker.on.myphoto.extensions.visible
import com.tattoo.tattoomaker.on.myphoto.model.ProjectModel
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ProjectAdapter @Inject constructor(@ActivityContext private val context: Context): RecyclerView.Adapter<ProjectAdapter.ProjectHolder> () {

    var callBack: ICallBackItem? = null
    private var lstProject = mutableListOf<ProjectModel>()
    private var isMultiChoose = false
    private var w = context.resources.displayMetrics.widthPixels / 100F

    fun setData(lstProject: MutableList<ProjectModel>) {
        this.lstProject = lstProject

        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectHolder =
        ProjectHolder(ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ProjectHolder, position: Int) {
        holder.onBindProject(position)
    }

    override fun getItemCount(): Int = if (lstProject.isNotEmpty()) lstProject.size else 0

    inner class ProjectHolder(private val binding: ItemProjectBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.layoutParams =
                LinearLayout.LayoutParams((27.778f * w).toInt(), (41.667f * w).toInt()).apply {
                    leftMargin = (2.22f * w).toInt()
                    rightMargin = (2.22f * w).toInt()
                    bottomMargin = (3.889f * w).toInt()
                }
        }

        fun onBindProject(position: Int) {
            val project = lstProject[position]

            if (isMultiChoose) binding.ivTick.visible() else binding.ivTick.gone()

            Glide.with(context)
                .asBitmap()
                .load(project.uriSaved)
                .placeholder(R.drawable.im_place_holder)
                .into(binding.iv)

            itemView.setOnClickListener {
                if (isMultiChoose) {
                    if (!project.isSelect) {
                        binding.ivTick.setImageResource(R.drawable.ic_item_tick)
                        project.isSelect = true
                    } else {
                        binding.ivTick.setImageResource(R.drawable.ic_item_un_tick)
                        project.isSelect = false
                    }
                } else callBack?.callBack(project, position)
            }

            itemView.setOnLongClickListener {
                callBack?.callBack( project, -1)
                true
            }
        }
    }

    fun getSelected(): MutableList<ProjectModel> {
        val lstProSelected = mutableListOf<ProjectModel>()
        for (i in lstProject.indices)
            if (lstProject[i].isSelect) lstProSelected.add(lstProject[i])
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