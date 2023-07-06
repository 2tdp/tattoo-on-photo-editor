package com.tattoo.tattoomaker.on.myphoto.fragment

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.addview.ViewPreview
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackCheck
import com.tattoo.tattoomaker.on.myphoto.model.ProjectModel
import com.tattoo.tattoomaker.on.myphoto.utils.Utils

class PreviewFragment(project: ProjectModel, isFinish: ICallBackCheck): Fragment() {

    private lateinit var viewPreview: ViewPreview

    private val isFinish: ICallBackCheck
    private lateinit var bitmap: Bitmap
    private var project : ProjectModel
    private var typeView = -1
    private var w = 0F

    companion object {
        const val TYPE_VIEW = "typeView"
        fun newInstance(project: ProjectModel, type: Int, isFinish: ICallBackCheck): PreviewFragment {
            val args = Bundle()

            val fragment = PreviewFragment(project, isFinish)
            args.putInt(TYPE_VIEW, type)
            fragment.arguments = args
            return fragment
        }
    }

    init {
        this.isFinish = isFinish
        this.project = project
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        w = requireContext().resources.displayMetrics.widthPixels / 100F

        viewPreview = ViewPreview(requireContext())
        typeView = requireArguments().getInt(TYPE_VIEW)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return viewPreview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bitmap = BitmapFactory.decodeFile(project.uriSaved)
        viewPreview.iv.setImageBitmap(bitmap)

        initView()
        evenClick()
    }

    private fun initView() {
        viewPreview.chooseTypeView(typeView)
    }

    private fun evenClick() {
        viewPreview.viewToolbar.ivBack.setOnClickListener {
            parentFragmentManager.popBackStack("PreviewFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            if (typeView == 0) isFinish.check(true)
        }

        viewPreview.viewBottomPreview.vDownload.setOnClickListener {
            Utils.saveImage(requireContext(), bitmap, "remiTattoo")
            Toast.makeText(requireContext(), resources.getString(R.string.done), Toast.LENGTH_SHORT).show()
        }

        viewPreview.viewBottomPreview.vShare.setOnClickListener {
            Utils.shareFile(requireContext(), bitmap, null)
        }

        viewPreview.viewBottomPreview.vWallpaper.setOnClickListener {
            val wallpaper = WallpaperManager.getInstance(requireContext())
            wallpaper.setBitmap(bitmap)
            Toast.makeText(requireContext(), resources.getString(R.string.done), Toast.LENGTH_SHORT).show()
        }

        viewPreview.viewBottomPreview.vEdit.setOnClickListener {
            parentFragmentManager.popBackStack("PreviewFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            isFinish.check(false)
        }
    }
}