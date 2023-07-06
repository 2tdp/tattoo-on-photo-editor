package com.tattoo.tattoomaker.on.myphoto.activity.base

import android.app.ActivityOptions
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import java.lang.Exception
import java.util.*

abstract class BaseActivity : AppCompatActivity() {
    private var finish = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setStatusBarTransparent(this)
        window.navigationBarColor = Color.parseColor("#000000")
    }

    protected fun setIntent(nameActivity: String?, isFinish: Boolean) {
        val intent = Intent()
        intent.component = nameActivity?.let { ComponentName(this, it) }
        startActivity(
            intent,
            ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
                .toBundle()
        )
        if (isFinish) finish()
    }

    protected fun openNavigation(nameActivity: String?, isFinish: Boolean) {
        val intent = Intent()
        intent.component = nameActivity?.let { ComponentName(this, it) }
        startActivity(
            intent,
            ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_left_small,
                R.anim.slide_out_right
            ).toBundle()
        )
        if (isFinish) finish()
    }

    protected fun showToast(msg: String?, gravity: Int) {
        val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        toast.setGravity(gravity, 0, 0)
        toast.show()
    }

    protected fun replaceFragment(manager: FragmentManager, fragment: Fragment, isAdd: Boolean, addBackStack: Boolean, isAnimation: Boolean) {
        try {
            val fragmentTransaction = manager.beginTransaction()
            if (isAnimation) fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit)
            if (addBackStack) fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
            if (isAdd) fragmentTransaction.add(res, fragment, fragment.javaClass.simpleName)
            else fragmentTransaction.replace(res, fragment, fragment.javaClass.simpleName)

            fragmentTransaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun hideFragment(fragmentManager: FragmentManager, fragment: Fragment?, animEnter: Int, animExit: Int) {
        fragmentManager.beginTransaction()
            .setCustomAnimations(animEnter, animExit)
            .hide(fragment!!)
            .commit()
    }

    protected fun showFragment(fragmentManager: FragmentManager, fragment: Fragment?, animEnter: Int, animExit: Int) {
        fragmentManager.beginTransaction()
            .setCustomAnimations(animEnter, animExit)
            .show(fragment!!)
            .commit()
    }

    protected fun dropPopUp(viewDrop: View?, viewContent: View?, width: Int, xOff: Int, yOff: Int) {
        val w: Int = resources.displayMetrics.widthPixels
        val popUp = PopupWindow(viewContent, (width * w / 100), RelativeLayout.LayoutParams.WRAP_CONTENT, true)
        popUp.showAsDropDown(viewDrop, xOff, yOff)
    }

    protected fun onBackPressed(isFinish: Boolean, isNavigation: Boolean) {
        if (isFinish) {
            if (finish != 0) {
                finish = 0
                finish()
                Utils.setAnimExit(this)
            } else {
                Toast.makeText(this, R.string.finish, Toast.LENGTH_SHORT).show()
                finish++
                val timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        finish = 0
                    }
                }, 1000)
            }
        } else onBackPressedDispatcher.onBackPressed()
    }

    companion object {
        //animation
        const val res = android.R.id.content
        val enter: Int = R.anim.slide_in_right
        val exit: Int = R.anim.slide_out_left
        val popEnter: Int = R.anim.slide_in_left_small
        val popExit: Int = R.anim.slide_out_right
    }
}