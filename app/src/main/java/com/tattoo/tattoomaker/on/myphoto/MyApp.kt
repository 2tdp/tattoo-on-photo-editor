package com.tattoo.tattoomaker.on.myphoto

import android.app.Application
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager

class MyApp : Application() {
    override fun onCreate() {
        DataLocalManager.init(applicationContext)
        super.onCreate()
    }
}