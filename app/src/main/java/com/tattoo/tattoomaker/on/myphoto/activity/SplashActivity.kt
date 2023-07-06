package com.tattoo.tattoomaker.on.myphoto.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseActivity
import com.tattoo.tattoomaker.on.myphoto.addview.ViewSplash

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private lateinit var viewSplash: ViewSplash

    private var progressStatus = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewSplash = ViewSplash(this@SplashActivity)
        setContentView(viewSplash)

        Thread {
            while (progressStatus < 99) {
                progressStatus += 1
                handler.post { viewSplash.customSeekbarLoading.setProgress(progressStatus) }
                try {
                    Thread.sleep(34)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }.start()
    }
}