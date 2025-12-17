package com.tattoo.tattoomaker.on.myphoto.activity.base

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem

interface BaseView {
    fun startIntent(nameActivity: String, isFinish: Boolean)
    fun startIntent(intent: Intent, isFinish: Boolean)
    fun startIntentForResult(startForResult: ActivityResultLauncher<Intent>, nameActivity: String, isFinish: Boolean)
    fun startIntentForResult(startForResult: ActivityResultLauncher<Intent>, intent: Intent, isFinish: Boolean)
    fun showLoading()
    fun showLoading(cancelable: Boolean)
    fun hideLoading()
    fun showNativeFull(isShowNativeFull: Boolean, strId: List<String> = listOf(), isShowCd: Boolean = true, callback: ICallBackItem? = null)
    fun reCdNativeFull()
}