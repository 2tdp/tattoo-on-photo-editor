package com.tattoo.tattoomaker.on.myphoto.activity

import android.app.AlertDialog
import android.os.Bundle
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseActivity
import com.tattoo.tattoomaker.on.myphoto.activity.premium.PremiumActivity
import com.tattoo.tattoomaker.on.myphoto.addview.ViewSettings
import com.tattoo.tattoomaker.on.myphoto.addview.viewdialog.ViewDialogRattingApp
import com.tattoo.tattoomaker.on.myphoto.utils.Utils

class SettingsActivity: BaseActivity() {

    private lateinit var viewSettings: ViewSettings
    private var w = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewSettings = ViewSettings(this@SettingsActivity)
        setContentView(viewSettings)
        w = resources.displayMetrics.widthPixels / 100

        evenClick()
    }

    private fun evenClick() {
        viewSettings.viewToolbar.ivBack.setOnClickListener { finish() }
        viewSettings.tvPremium.setOnClickListener {
            setIntent(PremiumActivity::class.java.name, false)
        }

        viewSettings.viewMoreApp.setOnClickListener{ Utils.moreApps(this@SettingsActivity)}
        viewSettings.viewRateUs.setOnClickListener {
            val viewDialogRattingApp = ViewDialogRattingApp(this@SettingsActivity)
            viewDialogRattingApp.tvRateUs.text = resources.getString(R.string.rate_us)
            viewDialogRattingApp.tvCancel.text = resources.getString(R.string.cancel)

            val dialog = AlertDialog.Builder(this@SettingsActivity, R.style.SheetDialog).create()
            dialog.setCancelable(false)
            dialog.setView(viewDialogRattingApp)
            dialog.show()

            viewDialogRattingApp.layoutParams.height = (46.94f * w).toInt()
            viewDialogRattingApp.layoutParams.width = (75f * w).toInt()

            //rate = 5 - rating
            viewDialogRattingApp.tvRateUs.setOnClickListener {
                val rate = 5 - viewDialogRattingApp.ratingBar.rating
                if (rate > 3.5) Utils.rateApp(this@SettingsActivity)
                else Utils.sendFeedback(this@SettingsActivity)
            }

            viewDialogRattingApp.tvCancel.setOnClickListener { dialog.cancel() }
        }
        viewSettings.viewShareApp.setOnClickListener { Utils.shareApp(this@SettingsActivity) }
        viewSettings.viewFeedBack.setOnClickListener { Utils.sendFeedback(this@SettingsActivity) }
        viewSettings.viewPP.setOnClickListener { Utils.privacyApp(this@SettingsActivity) }
    }
}