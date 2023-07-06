package com.tattoo.tattoomaker.on.myphoto.activity.premium

import android.os.Bundle
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseActivity
import com.tattoo.tattoomaker.on.myphoto.addview.viewpremium.ViewPremiumGo
import java.util.*


class PremiumGoActivity : BaseActivity() {

    private lateinit var viewPremiumGo: ViewPremiumGo

    private var scrollTimer: Timer? = null
    private var scrollerSchedule: TimerTask? = null
    private var scrollMax = 0
    private var scrollPos = 0
    private var w = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewPremiumGo = ViewPremiumGo(this@PremiumGoActivity)
        setContentView(viewPremiumGo)
        w = resources.displayMetrics.widthPixels

        evenCLick()
    }

    private fun evenCLick(){
        viewPremiumGo.ivBack.setOnClickListener { finish() }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            getScrollMaxAmount()
            startAutoScrolling(false)
        }
    }

    private fun startAutoScrolling(isBack: Boolean) {
        if (scrollTimer == null) {
            scrollTimer = Timer()
            val timerTick = Runnable {
                if (!isBack) moveRightScrollView()
                else moveLeftScrollView()
            }
            if (scrollerSchedule != null) {
                scrollerSchedule!!.cancel()
                scrollerSchedule = null
            }
            scrollerSchedule = object : TimerTask() {
                override fun run() {
                    runOnUiThread(timerTick)
                }
            }
            scrollTimer!!.schedule(scrollerSchedule, 30, 30)
        }
    }

    private fun getScrollMaxAmount() {
        scrollMax = viewPremiumGo.ll.measuredWidth - viewPremiumGo.scrollHorizontal.width
    }

    private fun moveRightScrollView() {
        scrollPos = (viewPremiumGo.scrollHorizontal.scrollX + 2.5).toInt()
        if (scrollPos >= scrollMax) {
            clearTimerTask(scrollerSchedule)
            clearTimers(scrollTimer)
            scrollerSchedule = null
            scrollTimer = null

            startAutoScrolling(true)
        }
        viewPremiumGo.scrollHorizontal.scrollTo(scrollPos, 0)
    }

    private fun moveLeftScrollView() {
        scrollPos = (scrollPos - 2.5).toInt()
        if (scrollPos <= 2) {
            clearTimerTask(scrollerSchedule)
            clearTimers(scrollTimer)
            scrollerSchedule = null
            scrollTimer = null

            startAutoScrolling(false)
        }
        viewPremiumGo.scrollHorizontal.scrollTo(scrollPos, 0)
    }

    override fun onDestroy() {
        clearTimerTask(scrollerSchedule)
        clearTimers(scrollTimer)
        scrollerSchedule = null
        scrollTimer = null
        super.onDestroy()
    }

    private fun clearTimers(timer: Timer?) {
        timer?.cancel()
    }

    private fun clearTimerTask(timerTask: TimerTask?) {
        timerTask?.cancel()
    }
}