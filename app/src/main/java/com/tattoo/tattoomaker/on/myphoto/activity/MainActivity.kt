package com.tattoo.tattoomaker.on.myphoto.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseActivity
import com.tattoo.tattoomaker.on.myphoto.activity.premium.PremiumActivity
import com.tattoo.tattoomaker.on.myphoto.addview.viewhome.ViewHome
import com.tattoo.tattoomaker.on.myphoto.addview.viewdialog.ViewDialogText
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackCheck
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.fragment.ChooseBackgroundFragment
import com.tattoo.tattoomaker.on.myphoto.model.picture.PicModel
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager
import com.tattoo.tattoomaker.on.myphoto.utils.Constant
import org.wysaid.nativePort.CGENativeLibrary
import java.io.IOException
import java.io.InputStream

class MainActivity : BaseActivity() {

    private lateinit var viewHome: ViewHome

    private var w = 0

    private var chooseBackgroundFragment: ChooseBackgroundFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewHome = ViewHome(this)
        setContentView(viewHome)
        w= resources.displayMetrics.widthPixels / 100

        onBackPressedDispatcher.addCallback(this@MainActivity, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (chooseBackgroundFragment != null)
                    chooseBackgroundFragment?.let {
                        if (it.isVisible) {
                            supportFragmentManager.popBackStack(
                                "ChooseBackgroundFragment",
                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                            )
                            chooseBackgroundFragment = null
                        }
                    }
                else onBackPressed(true, false)
            }
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
                showDialogPermission()
            else setIntent(PremiumActivity::class.java.name, false)
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                showDialogPermission()
            else setIntent(PremiumActivity::class.java.name, false)
        }

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
        viewHome.ivPremium.setOnClickListener {
            setIntent(PremiumActivity::class.java.name, false)
        }

        viewHome.tvSettings.setOnClickListener {
            setIntent(SettingsActivity::class.java.name, false)
        }

        viewHome.tvMyStore.setOnClickListener {
            setIntent(MyTattooActivity::class.java.name, false)
        }

        viewHome.tvCreate.setOnClickListener {
            chooseBackgroundFragment = ChooseBackgroundFragment.newInstance(object : ICallBackItem {
                override fun callBack(ob: Any, position: Int) {
                    val pic = ob as PicModel
                    DataLocalManager.setPicture(pic, Constant.BACKGROUND_PICTURE)
                    setIntent(EditActivity::class.java.name, false)
                }
            })
            replaceFragment(supportFragmentManager, chooseBackgroundFragment!!, true, true, true)
        }
    }

    private fun showDialogPermission() {
        val viewDialog = ViewDialogText(this@MainActivity)
        viewDialog.tv.text = getString(R.string.des_permission)
        viewDialog.tvYes.text = getString(R.string.yes)
        viewDialog.tvCancel.text = getString(R.string.cancel)

        val dialog = AlertDialog.Builder(this@MainActivity, R.style.SheetDialog).create()
        dialog.setView(viewDialog)
        dialog.setCancelable(true)
        dialog.show()

        viewDialog.layoutParams.width = (75f * w).toInt()
        viewDialog.layoutParams.height = (38.61f * w).toInt()

        viewDialog.tvYes.setOnClickListener {
            isGranted(object : ICallBackCheck {
                override fun check(isCheck: Boolean) {
                    dialog.cancel()
                }
            })
            dialog.cancel()
        }
        viewDialog.tvCancel.setOnClickListener {
            Toast.makeText(this@MainActivity, R.string.des_permission_2, Toast.LENGTH_SHORT).show()
            openSettingPermission()
        }
    }

    private fun isGranted(check: ICallBackCheck) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withContext(this@MainActivity)
                .withPermission(Manifest.permission.READ_MEDIA_IMAGES)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                        check.check(true)
                    }

                    override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                        openSettingPermission()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissionRequest: PermissionRequest,
                        permissionToken: PermissionToken
                    ) {
                        Toast.makeText(this@MainActivity, R.string.des_permission_2, Toast.LENGTH_SHORT).show()
                        permissionToken.continuePermissionRequest()
                    }
                }).check()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Dexter.withContext(this@MainActivity)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                            check.check(true)
                        }

                        override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                            openSettingPermission()
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissionRequest: PermissionRequest,
                            permissionToken: PermissionToken
                        ) {
                            Toast.makeText(
                                this@MainActivity,
                                R.string.des_permission_2,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            permissionToken.continuePermissionRequest()
                        }
                    }).check()
            } else {
                Dexter.withContext(this@MainActivity)
                    .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionRationaleShouldBeShown(
                            p0: MutableList<PermissionRequest>?,
                            p1: PermissionToken?
                        ) {
                            openSettingPermission()
                        }

                        override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                            if (p0!!.areAllPermissionsGranted()) check.check(true)
                        }
                    }).check()
            }
        }
    }

    private fun openSettingPermission() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", this@MainActivity.packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}