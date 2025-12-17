package com.tattoo.tattoomaker.on.myphoto.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseActivity
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.databinding.ActivitySplashBinding
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager
import kotlin.jvm.java

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun handleKeyboardUi(isVisible: Boolean, imeHeight: Int) {

    }

    override fun setUp() {
        binding.vLoading.onProgress = object : ICallBackItem {
            override fun callBack(ob: Any?, position: Int) {
                binding.tvProgress.text = "${getString(R.string.loading)}($position%)..."
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({ startActivity(false) }, 1500)
    }

    private fun startActivity(isShowNativeFull: Boolean) {
        startIntent(Intent(this@SplashActivity, OnBoardingActivity::class.java), true)
    }
}