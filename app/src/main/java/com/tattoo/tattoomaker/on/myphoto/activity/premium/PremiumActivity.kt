package com.tattoo.tattoomaker.on.myphoto.activity.premium

import android.os.Bundle
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseActivity
import com.tattoo.tattoomaker.on.myphoto.addview.viewpremium.ViewPremium

class PremiumActivity: BaseActivity() {

    lateinit var viewPremium: ViewPremium

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewPremium = ViewPremium(this@PremiumActivity)
        setContentView(viewPremium)


        evenClick()
    }

    private fun evenClick(){
        viewPremium.tvContinue.setOnClickListener {
            setIntent(PremiumGoActivity::class.java.name, false)
        }
        viewPremium.ivExit.setOnClickListener { finish() }
    }
}