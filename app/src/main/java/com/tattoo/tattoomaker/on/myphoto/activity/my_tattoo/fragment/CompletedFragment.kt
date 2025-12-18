package com.tattoo.tattoomaker.on.myphoto.activity.my_tattoo.fragment

import android.os.Bundle
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseFragment
import com.tattoo.tattoomaker.on.myphoto.databinding.FragmentCompletedBinding
import com.tattoo.tattoomaker.on.myphoto.helper.Constant
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager

class CompletedFragment: BaseFragment<FragmentCompletedBinding>(FragmentCompletedBinding::inflate) {

    companion object {
        fun newInstance(): CompletedFragment {
            val args = Bundle()

            val fragment = CompletedFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setUp() {
        val lstComplete = DataLocalManager.Companion.getListProject(requireContext(), Constant.LIST_COMPLETE)
    }

}