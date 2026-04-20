package com.tattoo.tattoomaker.on.myphoto

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlin.div

@HiltAndroidApp
class MyApp : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var ctx: Context

        @SuppressLint("StaticFieldLeak")
        var w = 0f
    }

    override fun onCreate() {
        super.onCreate()
        DataLocalManager.init(applicationContext)

        ctx = applicationContext
        w = resources.displayMetrics.widthPixels / 100f
    }
}