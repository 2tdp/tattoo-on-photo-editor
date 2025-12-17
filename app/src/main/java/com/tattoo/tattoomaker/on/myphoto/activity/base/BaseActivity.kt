package com.tattoo.tattoomaker.on.myphoto.activity.base

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.databinding.DialogLoadingBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.setUpDialog

abstract class BaseActivity<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B) : AppCompatActivity(), BaseView {

    val binding: B by lazy { bindingFactory(layoutInflater) }

    private var loadingDialog: AlertDialog? = null

    var w = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.navigationBarColor = "#01ffffff".toColorInt()
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = hideSystemBars()

        setContentView(binding.root)
        w = resources.displayMetrics.widthPixels / 100F
    }

    protected abstract fun handleKeyboardUi(isVisible: Boolean, imeHeight: Int)
    protected abstract fun setUp()

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.navigationBarColor = "#01ffffff".toColorInt()
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = hideSystemBars()

        setContentView(binding.root)
        w = resources.displayMetrics.widthPixels / 100F

        setUp()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

            handleKeyboardUi(imeVisible, imeHeight)

            insets
        }
    }

    private fun hideSystemBars(): Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

    override fun startIntent(nameActivity: String, isFinish: Boolean) {
        val intent = Intent().apply {
            component = ComponentName(this@BaseActivity, nameActivity)
        }
        startActivity(intent, null)
        if (isFinish) this.finish()
    }

    override fun startIntent(intent: Intent, isFinish: Boolean) {
        startActivity(intent, null)
        if (isFinish) this.finish()
    }

    override fun startIntentForResult(
        startForResult: ActivityResultLauncher<Intent>,
        nameActivity: String,
        isFinish: Boolean
    ) {
        startForResult.launch(
            Intent().apply {
                component = ComponentName(this@BaseActivity, nameActivity)
            }, null
        )
        if (isFinish) this.finish()
    }

    override fun startIntentForResult(
        startForResult: ActivityResultLauncher<Intent>,
        intent: Intent,
        isFinish: Boolean
    ) {
        startForResult.launch(intent, null)
        if (isFinish) this.finish()
    }

    fun checkPer(str: Array<String>): Boolean {
        if (str[0] == "") return true
        var isCheck = true
        for (i in str) {
            if (ContextCompat.checkSelfPermission(this, i) != PackageManager.PERMISSION_GRANTED)
                isCheck = false
        }

        return isCheck
    }

    fun haveNetworkConnection(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals("WIFI", ignoreCase = true))
                if (ni.isConnected) haveConnectedWifi = true
            if (ni.typeName.equals("MOBILE", ignoreCase = true))
                if (ni.isConnected) haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }

    override fun showLoading() {
        showLoading(true)
    }

    private fun initDialog(isCancel: Boolean) {
        val bindingDialog = DialogLoadingBinding.inflate(LayoutInflater.from(this@BaseActivity))

        loadingDialog = AlertDialog.Builder(this@BaseActivity, R.style.SheetDialog).create()
        loadingDialog?.setUpDialog(bindingDialog.root, isCancel)

        bindingDialog.root.layoutParams.width = (73.889f * w).toInt()
        bindingDialog.root.layoutParams.height = (34.556f * w).toInt()
    }

    override fun showLoading(cancelable: Boolean) {
        Handler(Looper.getMainLooper()).post {
            if (loadingDialog != null) {
                loadingDialog?.cancel()
                loadingDialog = null
            }
            initDialog(cancelable)
        }
    }

    override fun hideLoading() {
        //cho chắc :(
        Handler(Looper.getMainLooper()).post {
            if (loadingDialog != null && !isFinishing) {
                loadingDialog?.cancel()
                loadingDialog = null
            }
        }
    }

    override fun showNativeFull(
        isShowNativeFull: Boolean,
        strId: List<String>,
        isShowCd: Boolean,
        callback: ICallBackItem?
    ) {

    }

    override fun reCdNativeFull() {

    }
}