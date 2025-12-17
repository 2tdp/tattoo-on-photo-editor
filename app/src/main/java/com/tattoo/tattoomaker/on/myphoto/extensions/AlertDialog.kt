package com.tattoo.tattoomaker.on.myphoto.extensions

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable

fun AlertDialog.setUpDialog(view: View, isCancel: Boolean) {
    setCancelable(isCancel)
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    setView(view)
    window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    show()
    window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
}