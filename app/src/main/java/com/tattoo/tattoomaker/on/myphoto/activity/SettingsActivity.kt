package com.tattoo.tattoomaker.on.myphoto.activity

import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseActivity
import com.tattoo.tattoomaker.on.myphoto.databinding.ActivitySettingsBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.utils.Utils

class SettingsActivity: BaseActivity<ActivitySettingsBinding>(ActivitySettingsBinding::inflate) {

    override fun handleKeyboardUi(isVisible: Boolean, imeHeight: Int) {

    }

    override fun setUp() {
        binding.ivBack.setOnClickListener { finish() }

        binding.llShareApp.setOnUnDoubleClickListener { Utils.shareApp(this@SettingsActivity) }
        binding.llFeedback.setOnUnDoubleClickListener { Utils.sendFeedback(this@SettingsActivity) }
        binding.llPP.setOnUnDoubleClickListener { Utils.privacyApp(this@SettingsActivity) }
    }
}