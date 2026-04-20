package com.tattoo.tattoomaker.on.myphoto.activity.my_tattoo

import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseActivity
import com.tattoo.tattoomaker.on.myphoto.activity.my_tattoo.fragment.CompletedFragment
import com.tattoo.tattoomaker.on.myphoto.activity.my_tattoo.fragment.DraftsFragment
import com.tattoo.tattoomaker.on.myphoto.adapter.ViewPagerAddFragmentsAdapter
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackCheck
import com.tattoo.tattoomaker.on.myphoto.databinding.ActivityMyTattooBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.gone
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTattooActivity: BaseActivity<ActivityMyTattooBinding>(ActivityMyTattooBinding::inflate) {

    override fun handleKeyboardUi(isVisible: Boolean, imeHeight: Int) {

    }

    private val completeItemFragment: CompletedFragment by lazy { CompletedFragment.newInstance() }
    private val draftItemFragment: DraftsFragment by lazy { DraftsFragment.newInstance() }

    override fun setUp() {
        binding.viewPager.apply {
            adapter = ViewPagerAddFragmentsAdapter(supportFragmentManager, lifecycle).apply {
                addFrag(completeItemFragment)
                addFrag(draftItemFragment)
            }
            isUserInputEnabled = false
        }

        completeItemFragment.isDel = object : ICallBackCheck {
            override fun check(isCheck: Boolean) {
                if (isCheck) {
                    binding.ivTick.gone()
                    binding.ivDel.visible()
                } else {
                    binding.ivTick.visible()
                    binding.ivDel.gone()
                }
            }
        }
        draftItemFragment.isDel = object : ICallBackCheck {
            override fun check(isCheck: Boolean) {
                if (isCheck) {
                    binding.ivTick.gone()
                    binding.ivDel.visible()
                } else {
                    binding.ivTick.visible()
                    binding.ivDel.gone()
                }
            }
        }

        evenClick()
    }

    private fun evenClick() {
        binding.tvCompleted.setOnUnDoubleClickListener { actionTab(0) }
        binding.tvDrafts.setOnUnDoubleClickListener { actionTab(1) }

        binding.ivDel.setOnUnDoubleClickListener {
            if (binding.viewPager.currentItem == 0) completeItemFragment.actionDel()
            else draftItemFragment.actionDel()
        }
        binding.ivTick.setOnUnDoubleClickListener {
            if (binding.viewPager.currentItem == 0) completeItemFragment.setUpDel()
            else draftItemFragment.setUpDel()
        }

        binding.ivBack.setOnClickListener { finish() }
    }

    private fun actionTab(pos: Int) {
        binding.tvCompleted.setBackgroundResource(R.drawable.bg_btn_disable_2)
        binding.tvDrafts.setBackgroundResource(R.drawable.bg_btn_disable_2)

        when(pos) {
            0 -> binding.tvCompleted.setBackgroundResource(R.drawable.bg_btn_enable_2)
            1 -> binding.tvDrafts.setBackgroundResource(R.drawable.bg_btn_enable_2)
        }
        binding.viewPager.setCurrentItem(pos, true)
    }
}