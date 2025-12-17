package com.tattoo.tattoomaker.on.myphoto

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager
import kotlin.div

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