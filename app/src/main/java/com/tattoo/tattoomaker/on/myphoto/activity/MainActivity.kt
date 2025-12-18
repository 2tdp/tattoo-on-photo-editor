package com.tattoo.tattoomaker.on.myphoto.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.OnBackPressedCallback
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseActivity
import com.tattoo.tattoomaker.on.myphoto.activity.edit.ChooseImageActivity
import com.tattoo.tattoomaker.on.myphoto.activity.my_tattoo.MyTattooActivity
import com.tattoo.tattoomaker.on.myphoto.databinding.ActivityMainBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.helper.Constant
import org.wysaid.nativePort.CGENativeLibrary
import java.io.IOException
import java.io.InputStream

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun handleKeyboardUi(isVisible: Boolean, imeHeight: Int) {

    }

    override fun setUp() {
        onBackPressedDispatcher.addCallback(this@MainActivity, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                finish()
            }
        })

        CGENativeLibrary.setLoadImageCallback(object : CGENativeLibrary.LoadImageCallback {
            //Notice: the 'name' passed in is just what you write in the rule, e.g: 1.jpg
            override fun loadImage(name: String, arg: Any?): Bitmap? {
                Log.d(Constant.TAG, "loadImage: $name")
                val am = assets
                val `is`: InputStream = try {
                    am.open("filter/$name")
                } catch (e: IOException) {
                    return BitmapFactory.decodeFile(name)
                }
                return BitmapFactory.decodeStream(`is`)
            }

            override fun loadImageOK(bmp: Bitmap, arg: Any?) {
                //The bitmap is which you returned at 'loadImage'.
                //You can call recycle when this function is called, or just keep it for further usage.
                bmp.recycle()
            }
        }, Object())

        evenClick()
    }

    private fun evenClick() {
        binding.ivSetting.setOnUnDoubleClickListener {
            startIntent(SettingsActivity::class.java.name, false)
        }

        binding.tvMyTatoo.setOnClickListener {
            startIntent(MyTattooActivity::class.java.name, false)
        }

        binding.tvCreate.setOnClickListener {
            startIntent(ChooseImageActivity::class.java.name, false)
        }
    }
}