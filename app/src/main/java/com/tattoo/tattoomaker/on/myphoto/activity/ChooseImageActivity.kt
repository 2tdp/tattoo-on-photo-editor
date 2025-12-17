package com.tattoo.tattoomaker.on.myphoto.activity

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Insert
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseActivity
import com.tattoo.tattoomaker.on.myphoto.adapter.ChooseBackgroundAdapter
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.data.DataPic
import com.tattoo.tattoomaker.on.myphoto.databinding.ActivityChooseImageBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.model.picture.PicModel
import com.tattoo.tattoomaker.on.myphoto.helper.Constant
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsBitmap
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ChooseImageActivity: BaseActivity<ActivityChooseImageBinding>(ActivityChooseImageBinding::inflate) {

    override fun handleKeyboardUi(isVisible: Boolean, imeHeight: Int) {

    }

    @Inject lateinit var chooseImageAdapter: ChooseBackgroundAdapter

    private var photoUri: Uri? = null

    override fun setUp() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        chooseImageAdapter.callBack = object : ICallBackItem {
            override fun callBack(ob: Any?, position: Int) {

            }
        }
        binding.rcv.apply {
            layoutManager = GridLayoutManager(this@ChooseImageActivity, 3)
            adapter = chooseImageAdapter
        }
        chooseImageAdapter.setData(DataPic.getPicAssets(this, Constant.NAME_FOLDER_PICTURE_ASSETS))

        binding.tvCam.setOnUnDoubleClickListener { takePhoto() }
        binding.tvGallery.setOnUnDoubleClickListener { pickImageLauncher.launch("image/*") }
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Xử lý ảnh tại đây
                Log.d("PickImage", "Uri = $it")
            }
        }

    private fun takePhoto() {
        val photoFile = createImageFile()
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(
                this@ChooseImageActivity,
                "com.tattoo.tattoomaker.on.myphoto",
                photoFile
            )
            launchTakePhoto.launch(photoUri)
        }
    }

    private val launchTakePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSaved ->
        if (isSaved) {
            val size = UtilsBitmap.getImageSize(this@ChooseImageActivity, photoUri)
            val ratio = size[0] / size[1]
            val picModel = PicModel("-1", "takePhoto", ratio, photoUri.toString(), false)

        }
    }

    private fun createImageFile(): File? {
        @SuppressLint("SimpleDateFormat")
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "REMI_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image: File? = null
        try {
            image = File.createTempFile(imageFileName, ".png", storageDir)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }
}