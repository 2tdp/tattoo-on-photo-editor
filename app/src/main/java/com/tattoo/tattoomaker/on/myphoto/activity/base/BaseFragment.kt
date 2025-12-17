package com.tattoo.tattoomaker.on.myphoto.activity.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import java.lang.ref.WeakReference

abstract class BaseFragment<B: ViewBinding>(val bindingFactory: (LayoutInflater) -> B): Fragment(),
    BaseView {

    val binding: B by lazy { bindingFactory(layoutInflater) }

    private var baseActivity: WeakReference<BaseActivity<*>>? = null
    var w = 0f

    protected abstract fun setUp()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isClickable = true
        w = requireContext().resources.displayMetrics.widthPixels / 100f
        setUp()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) baseActivity = WeakReference(context)
    }

    override fun startIntent(nameActivity: String, isFinish: Boolean) {
        baseActivity?.get()?.startIntent(nameActivity, isFinish)
    }

    override fun startIntent(intent: Intent, isFinish: Boolean) {
        baseActivity?.get()?.startIntent(intent, isFinish)
    }

    override fun startIntentForResult(
        startForResult: ActivityResultLauncher<Intent>,
        nameActivity: String,
        isFinish: Boolean
    ) {
        baseActivity?.get()?.startIntentForResult(startForResult, nameActivity, isFinish)
    }

    override fun startIntentForResult(
        startForResult: ActivityResultLauncher<Intent>,
        intent: Intent,
        isFinish: Boolean
    ) {
        baseActivity?.get()?.startIntentForResult(startForResult, intent, isFinish)
    }

    override fun showNativeFull(
        isShowNativeFull: Boolean,
        strId: List<String>,
        isShowCd: Boolean,
        callback: ICallBackItem?
    ) {
        baseActivity?.get()?.showNativeFull(isShowNativeFull, strId, isShowCd, callback)
    }

    override fun reCdNativeFull() {
        baseActivity?.get()?.reCdNativeFull()
    }

    override fun showLoading() {
        baseActivity?.get()?.showLoading()
    }

    override fun showLoading(cancelable: Boolean) {
        baseActivity?.get()?.showLoading(cancelable)
    }

    override fun hideLoading() {
        baseActivity?.get()?.hideLoading()
    }

    companion object {
        //animation
        const val res = android.R.id.content
    }
}