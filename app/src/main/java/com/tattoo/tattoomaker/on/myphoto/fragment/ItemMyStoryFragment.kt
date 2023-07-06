package com.tattoo.tattoomaker.on.myphoto.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.activity.EditActivity
import com.tattoo.tattoomaker.on.myphoto.adapter.ProjectAdapter
import com.tattoo.tattoomaker.on.myphoto.addview.viewitem.ViewItemMyStory
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.model.ProjectModel
import com.tattoo.tattoomaker.on.myphoto.model.picture.PicModel
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager
import com.tattoo.tattoomaker.on.myphoto.utils.Constant
import com.tattoo.tattoomaker.on.myphoto.utils.Utils

class ItemMyStoryFragment(lstPro: ArrayList<ProjectModel>, callBack: ICallBackItem): Fragment() {

    private lateinit var viewItemMyStory: ViewItemMyStory

    private val callBack: ICallBackItem
    private var lstPro = ArrayList<ProjectModel>()
    private var position = -1

    private var projectAdapter: ProjectAdapter? = null

    companion object {
        const val VIEW_POSITION = "viewPosition"
        fun newInstance(lstPro: ArrayList<ProjectModel>, position: Int, callBack: ICallBackItem): ItemMyStoryFragment {
            val args = Bundle()

            val fragment = ItemMyStoryFragment(lstPro, callBack)
            args.putInt(VIEW_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

    init {
        this.lstPro = lstPro
        this.callBack = callBack
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewItemMyStory = ViewItemMyStory(requireContext())

        position = requireArguments().getInt(VIEW_POSITION)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return viewItemMyStory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (position == 0) setDataComplete()
        else if (position == 1) setDataDraft()

        viewItemMyStory.tvMove.setOnClickListener { goToEdit() }
    }

    private fun setDataComplete() {
        if (lstPro.isEmpty()) {
            viewItemMyStory.rlNoData.visibility = View.VISIBLE
            viewItemMyStory.rcv.visibility = View.GONE
        } else {
            viewItemMyStory.rlNoData.visibility = View.GONE
            viewItemMyStory.rcv.visibility = View.VISIBLE

            projectAdapter = ProjectAdapter(requireContext(), object : ICallBackItem {
                override fun callBack(ob: Any, position: Int) {
                    if (viewItemMyStory.viewLoading.isVisible) return
                    if (position > -1) callBack.callBack(ob, position)
                    else {
                        projectAdapter!!.isMultiChoose(true)
                        callBack.callBack(ob, position)
                    }
                }
            })

            projectAdapter!!.setDataProject(lstPro)
            viewItemMyStory.rcv.layoutManager = GridLayoutManager(requireContext(), 3)
            viewItemMyStory.rcv.adapter = projectAdapter
        }
    }

    private fun setDataDraft() {
        if (lstPro.isEmpty()) {
            viewItemMyStory.rlNoData.visibility = View.VISIBLE
            viewItemMyStory.rcv.visibility = View.GONE
        } else {
            viewItemMyStory.rlNoData.visibility = View.GONE
            viewItemMyStory.rcv.visibility = View.VISIBLE

            projectAdapter = ProjectAdapter(requireContext(), object : ICallBackItem {
                override fun callBack(ob: Any, position: Int) {
                    if (viewItemMyStory.viewLoading.isVisible) return
                    if (position > -1) callBack.callBack(ob, position)
                    else {
                        projectAdapter!!.isMultiChoose(true)
                        callBack.callBack(ob, position)
                    }
                }
            })

            projectAdapter!!.setDataProject(lstPro)
            viewItemMyStory.rcv.layoutManager = GridLayoutManager(requireContext(), 3)
            viewItemMyStory.rcv.adapter = projectAdapter
        }
    }

    private fun goToEdit() {
        val chooseBackgroundFragment = ChooseBackgroundFragment.newInstance(object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                val pic = ob as PicModel
                DataLocalManager.setPicture(pic, Constant.BACKGROUND_PICTURE)
                Utils.setIntent(requireContext(), EditActivity::class.java.name)
            }
        })
        Utils.replaceFragment(parentFragmentManager, chooseBackgroundFragment, true, true, true)
    }

    fun setMultiChoose(): Boolean {
        projectAdapter?.isMultiChoose(true)
        return lstPro.isNotEmpty()
    }

    fun getLstDel(): ArrayList<ProjectModel> {
        return projectAdapter!!.getSelected()
    }
}