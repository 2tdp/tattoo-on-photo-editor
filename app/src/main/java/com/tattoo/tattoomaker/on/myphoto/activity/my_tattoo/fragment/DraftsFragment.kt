package com.tattoo.tattoomaker.on.myphoto.activity.my_tattoo.fragment

import android.os.Bundle
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseFragment
import com.tattoo.tattoomaker.on.myphoto.databinding.FragmentCompletedBinding
import com.tattoo.tattoomaker.on.myphoto.databinding.FragmentDraftsBinding
import com.tattoo.tattoomaker.on.myphoto.helper.Constant
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager

class DraftsFragment: BaseFragment<FragmentDraftsBinding>(FragmentDraftsBinding::inflate) {

    companion object {
        fun newInstance(): DraftsFragment {
            val args = Bundle()

            val fragment = DraftsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setUp() {
        val lstDraft = DataLocalManager.Companion.getListProject(requireContext(), Constant.LIST_DRAFT)
    }

}