package com.tattoo.tattoomaker.on.myphoto.activity.my_tattoo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.activity.SuccessActivity
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseFragment
import com.tattoo.tattoomaker.on.myphoto.activity.edit.ChooseImageActivity
import com.tattoo.tattoomaker.on.myphoto.activity.edit.EditActivity
import com.tattoo.tattoomaker.on.myphoto.adapter.ProjectAdapter
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackCheck
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackCheckDimensional
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.databinding.DialogDeleteBinding
import com.tattoo.tattoomaker.on.myphoto.databinding.FragmentCompletedBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.gone
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.extensions.setUpDialog
import com.tattoo.tattoomaker.on.myphoto.extensions.toJson
import com.tattoo.tattoomaker.on.myphoto.extensions.visible
import com.tattoo.tattoomaker.on.myphoto.helper.Constant
import com.tattoo.tattoomaker.on.myphoto.model.ProjectModel
import com.tattoo.tattoomaker.on.myphoto.model.picture.PicModel
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.Utils.setIntent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CompletedFragment: BaseFragment<FragmentCompletedBinding>(FragmentCompletedBinding::inflate) {

    companion object {
        fun newInstance(): CompletedFragment {
            val args = Bundle()

            val fragment = CompletedFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject lateinit var projectAdapter: ProjectAdapter

    var isDel: ICallBackCheck? = null

    override fun setUp() {
        val lstComplete = DataLocalManager.getListProject(requireContext(), Constant.LIST_COMPLETE)
        if (lstComplete.isEmpty()) {
            binding.llNoData.visible()
            binding.rcv.gone()
        } else {
            binding.llNoData.gone()
            binding.rcv.visible()
        }

        projectAdapter.setData(lstComplete)
        projectAdapter.callBack = object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                if (position > -1) {
                    if (ob is ProjectModel) {
                        startIntent(Intent(requireActivity(), SuccessActivity::class.java).apply {
                            DataLocalManager.setProject(ob, Constant.PROJECT)
                            putExtra(Constant.TYPE_SUCCESS, 0)
                            putExtra(Constant.PROJECT_SUCCESS, ob.toJson())
                        }, false)
                    }
                } else {
                    projectAdapter.isMultiChoose(true)
                    isDel?.check(true)
                }
            }
        }

        binding.rcv.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = projectAdapter
        }

        binding.tvMove.setOnUnDoubleClickListener {
            startIntent(ChooseImageActivity::class.java.name, false)
        }
    }

    fun setUpDel() {
        val lstComplete = DataLocalManager.getListProject(requireContext(), Constant.LIST_COMPLETE)
        if (lstComplete.isEmpty())
            Toast.makeText(requireContext(), getString(R.string.empty), Toast.LENGTH_SHORT).show()
        else {
            projectAdapter.isMultiChoose(true)
            isDel?.check(true)
        }
    }

    fun actionDel() {
        val lstDel = projectAdapter.getSelected()
        if (lstDel.isEmpty())
            Toast.makeText(requireContext(), getString(R.string.please_choose_other_project), Toast.LENGTH_SHORT).show()
        else showDialogDel(lstDel)
    }

    private fun showDialogDel(lstDel: MutableList<ProjectModel>) {
        val bindingDialog = DialogDeleteBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext(), R.style.SheetDialog).create()
        dialog.setUpDialog(bindingDialog.root, false)

        bindingDialog.tvCancel.setOnClickListener { dialog.dismiss() }
        bindingDialog.tvDone.setOnUnDoubleClickListener {
            showLoading()
            // Migrate từ Thread+Handler sang Coroutine: lifecycle-aware, tự hủy khi Fragment destroyed
            lifecycleScope.launch(Dispatchers.IO) {
                val lstComplete = DataLocalManager.getListProject(requireContext(), Constant.LIST_COMPLETE)
                for (pro in lstDel) {
                    Utils.delFileInFolder(requireContext(), pro.nameFolder ?: "", "")
                    lstComplete.remove(pro)
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), getString(R.string.done), Toast.LENGTH_SHORT).show()
                    hideLoading()
                    isDel?.check(false)
                    DataLocalManager.setListProject(requireContext(), lstComplete, Constant.LIST_COMPLETE)
                    projectAdapter.setData(lstComplete)
                    dialog.dismiss()
                }
            }
        }
    }
}