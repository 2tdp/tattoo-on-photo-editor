package com.tattoo.tattoomaker.on.myphoto.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.tattoo.tattoomaker.on.myphoto.adapter.ChooseBackgroundAdapter
import com.tattoo.tattoomaker.on.myphoto.addview.viewhome.ViewChooseBackground
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackCheck
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.data.DataPic
import com.tattoo.tattoomaker.on.myphoto.model.picture.PicModel
import com.tattoo.tattoomaker.on.myphoto.utils.Constant
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsBitmap
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ChooseBackgroundFragment(callBack: ICallBackItem) : Fragment() {

    private lateinit var viewChooseBackground: ViewChooseBackground

    private var callBack: ICallBackItem
    private var w = 0

    private lateinit var chooseBackgroundAdapter: ChooseBackgroundAdapter
    private lateinit var lstPicAsset: ArrayList<PicModel>
    private var photoUri: Uri? = null

    companion object {
        fun newInstance(callBack: ICallBackItem): ChooseBackgroundFragment {
            val args = Bundle()

            val fragment = ChooseBackgroundFragment(callBack)
            fragment.arguments = args
            return fragment
        }
    }

    init {
        this.callBack = callBack
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewChooseBackground = ViewChooseBackground(requireContext())
        w = resources.displayMetrics.widthPixels / 100

        Thread {
            lstPicAsset = DataPic.getPicAssets(requireContext(), Constant.NAME_FOLDER_PICTURE_ASSETS)
            Handler(Looper.getMainLooper()).post {
                viewChooseBackground.viewLoading.visibility = View.GONE
                if (lstPicAsset.isNotEmpty()) chooseBackgroundAdapter.setData(lstPicAsset)
            }
        }.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return viewChooseBackground
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        evenClick()
    }

    private fun initView() {
        chooseBackgroundAdapter = ChooseBackgroundAdapter(requireContext(), object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                Utils.clearBackStack(parentFragmentManager)
                callBack.callBack(ob, position)
            }
        })

        viewChooseBackground.rcv.layoutManager = GridLayoutManager(requireContext(), 3)
        viewChooseBackground.rcv.adapter = chooseBackgroundAdapter
    }

    private fun evenClick() {
        viewChooseBackground.viewToolbar.ivBack.setOnClickListener { parentFragmentManager.popBackStack() }
        viewChooseBackground.tvGallery.setOnClickListener { clickAddPic() }
        viewChooseBackground.tvCamera.setOnClickListener { takePhoto() }
    }

    private fun clickAddPic() {
        Thread { DataPic.getBucketPictureList(requireContext()) }.start()

        val pickPictureFragment = PickPictureFragment.newInstance((object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                Utils.clearBackStack(parentFragmentManager)
                callBack.callBack(ob, position)
            }
        }), object : ICallBackCheck {
            override fun check(isCheck: Boolean) {

            }
        })
        Utils.replaceFragment(parentFragmentManager, pickPictureFragment, true, true, true)
    }

    private fun takePhoto() {
        val photoFile = createImageFile()
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(
                requireContext(),
                "com.tattoo.tattoomaker.on.myphoto",
                photoFile
            )
            launchTakePhoto.launch(photoUri)
        }
    }

    private val launchTakePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSaved ->
        if (isSaved) {
            val size = UtilsBitmap.getImageSize(requireContext(), photoUri)
            val ratio = size[0] / size[1]
            val picModel = PicModel("-1", "takePhoto", ratio, photoUri.toString(), false)

            Utils.clearBackStack(parentFragmentManager)
            callBack.callBack(picModel, -1)
        }
    }

    private fun createImageFile(): File? {
        @SuppressLint("SimpleDateFormat")
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "REMI_" + timeStamp + "_"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image: File? = null
        try {
            image = File.createTempFile(imageFileName, ".png", storageDir)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }
}