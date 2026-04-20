package com.tattoo.tattoomaker.on.myphoto.activity

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.google.gson.Gson
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseActivity
import com.tattoo.tattoomaker.on.myphoto.activity.edit.EditActivity
import com.tattoo.tattoomaker.on.myphoto.databinding.ActivitySuccessBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.gone
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.extensions.visible
import com.tattoo.tattoomaker.on.myphoto.helper.Constant
import com.tattoo.tattoomaker.on.myphoto.helper.Constant.PROJECT_SUCCESS
import com.tattoo.tattoomaker.on.myphoto.model.ProjectModel
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import org.json.JSONObject

class SuccessActivity: BaseActivity<ActivitySuccessBinding>(ActivitySuccessBinding::inflate) {

    override fun handleKeyboardUi(isVisible: Boolean, imeHeight: Int) {

    }

    private var bitmap: Bitmap? = null
    private var project: ProjectModel? = null

    override fun setUp() {
        intent.getStringExtra(PROJECT_SUCCESS)?.let {
            project = Gson().fromJson(JSONObject(it).toString(), ProjectModel::class.java)
            bitmap = BitmapFactory.decodeFile(project?.uriSaved)
        }
        intent.getIntExtra(Constant.TYPE_SUCCESS, 0).let {
            if (it == 0) binding.llEdit.gone() else binding.llEdit.visible()
        }

        binding.ivHome.setOnUnDoubleClickListener {
            startIntent(Intent(this@SuccessActivity, MainActivity::class.java), false)
            finishAffinity()
        }

        binding.llDownload.setOnUnDoubleClickListener {
            bitmap?.let {
                Utils.saveImage(this@SuccessActivity, it, "tattoo-${System.currentTimeMillis()}-2tdp")
                Toast.makeText(this@SuccessActivity, resources.getString(R.string.done), Toast.LENGTH_SHORT).show()
            }
        }

        binding.llShare.setOnUnDoubleClickListener {
            bitmap?.let { Utils.shareFile(this@SuccessActivity, it, null) }
        }

        binding.llWallpaper.setOnUnDoubleClickListener {
            val wallpaper = WallpaperManager.getInstance(this@SuccessActivity)
            wallpaper.setBitmap(bitmap)
            Toast.makeText(this@SuccessActivity, resources.getString(R.string.done), Toast.LENGTH_SHORT).show()
        }

        binding.llEdit.setOnUnDoubleClickListener {
            DataLocalManager.setProject(project, Constant.PROJECT)
            startIntent(EditActivity::class.java.name, true)
        }
    }
}