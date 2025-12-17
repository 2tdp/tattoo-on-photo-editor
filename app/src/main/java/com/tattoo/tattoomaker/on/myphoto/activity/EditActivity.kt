package com.tattoo.tattoomaker.on.myphoto.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.scale
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import codes.side.andcolorpicker.model.IntegerHSLColor
import codes.side.andcolorpicker.view.picker.ColorSeekBar
import com.remi.textonphoto.writeonphoto.addtext.customview.OnSeekbarResult
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseActivity
import com.tattoo.tattoomaker.on.myphoto.activity.edit.AddTextDialog
import com.tattoo.tattoomaker.on.myphoto.activity.edit.CropImageDialog
import com.tattoo.tattoomaker.on.myphoto.adapter.ColorAdapter
import com.tattoo.tattoomaker.on.myphoto.adapter.ImageAdapter
import com.tattoo.tattoomaker.on.myphoto.adapter.TattooAdapter
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackCheck
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.data.DataColor
import com.tattoo.tattoomaker.on.myphoto.data.DataFilter
import com.tattoo.tattoomaker.on.myphoto.data.DataFrame
import com.tattoo.tattoomaker.on.myphoto.data.DataTattoo
import com.tattoo.tattoomaker.on.myphoto.databinding.ActivityEditBinding
import com.tattoo.tattoomaker.on.myphoto.databinding.DialogDiscardBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.gone
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.extensions.setUpDialog
import com.tattoo.tattoomaker.on.myphoto.extensions.showToast
import com.tattoo.tattoomaker.on.myphoto.extensions.slideInUp
import com.tattoo.tattoomaker.on.myphoto.extensions.slideOutDown
import com.tattoo.tattoomaker.on.myphoto.extensions.visible
import com.tattoo.tattoomaker.on.myphoto.fragment.PreviewFragment
import com.tattoo.tattoomaker.on.myphoto.helper.Constant
import com.tattoo.tattoomaker.on.myphoto.helper.Constant.FROM_ASSETS
import com.tattoo.tattoomaker.on.myphoto.model.*
import com.tattoo.tattoomaker.on.myphoto.model.background.AdjustModel
import com.tattoo.tattoomaker.on.myphoto.model.background.BackgroundModel
import com.tattoo.tattoomaker.on.myphoto.model.text.TextModel
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager
import com.tattoo.tattoomaker.on.myphoto.undoredo.UndoRedo
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.Utils.replaceFragment
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsAdjust
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsBitmap
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.DrawableStickerCustom
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.TextStickerCustom
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.stickerview.Sticker
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.stickerview.StickerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.wysaid.nativePort.CGENativeLibrary
import java.io.File

@AndroidEntryPoint
class EditActivity: BaseActivity<ActivityEditBinding>(ActivityEditBinding::inflate) {

    override fun handleKeyboardUi(isVisible: Boolean, imeHeight: Int) {

    }

    private lateinit var undoRedo: UndoRedo

    private var currentSticker: Sticker? = null
    private lateinit var stickerOld: Sticker
    private var project: ProjectModel? = null
    private var currentProject: UndoRedoModel? = null
    private var backgroundModel: BackgroundModel? = null
    private lateinit var bitmap: Bitmap
    private var indexProject = -1
    private var indexMatrix = 0
    private var isProject = false

    private var nameFolderBackground = ""
    private var nameFolderImage = ""
    private var nameFolder = ""

    override fun setUp() {
        backgroundModel = BackgroundModel().apply {
            adjustModel = AdjustModel(0f, 0f, 0f, 0f, 0f, 0f)
        }

        undoRedo = UndoRedo(object : UndoRedo.OnUndoRedoEventListener {
            override fun onUndoFinished(projectOriginator: UndoRedoModel?) {
                projectOriginator?.let { finishUndoRedo(it) }
            }

            override fun onRedoFinished(projectOriginator: UndoRedoModel?) {
                projectOriginator?.let { finishUndoRedo(it) }
            }

            override fun onStateSaveFinished() {

            }
        })

        onBackPressedDispatcher.addCallback(this@EditActivity, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                showDialogBack()
            }
        })

        binding.vSticker.onStickerOperationListener = object : StickerView.OnStickerOperationListener {
            override fun onStickerAdded(sticker: Sticker) {
                currentSticker = sticker
                stickerOld = sticker
                binding.vSticker.hideBorderAndIcon(1)
                if (project != null) checkMatrixStickerProject(sticker)
                checkTransformView(sticker)
                setMatrixToModel(sticker, false)
            }

            override fun onStickerClicked(sticker: Sticker) {
                binding.vSticker.currentSticker = sticker
                binding.vSticker.hideBorderAndIcon(1)
                binding.vSticker.invalidate()
            }

            override fun onStickerDeleted(sticker: Sticker) {
                hideAndSeekViewBottom(Constant.VIEW_EDIT_MAIN)
            }

            override fun onStickerDragFinished(sticker: Sticker) {
                binding.vSticker.hideBorderAndIcon(1)
                binding.vSticker.invalidate()

                setMatrixToModel(sticker, true)
            }

            override fun onStickerTouchedDown(sticker: Sticker) {
                currentSticker = sticker
                checkTransformView(sticker)
            }

            override fun onStickerZoomFinished(sticker: Sticker) {
                binding.vSticker.hideBorderAndIcon(1)
                binding.vSticker.invalidate()

                setMatrixToModel(sticker, true)
            }

            override fun onStickerFlipped(sticker: Sticker) {}
            override fun onStickerDoubleTapped(sticker: Sticker) {}
        }

        createProject()
        evenClick()
    }

    override fun onResume() {
        super.onResume()

        project = DataLocalManager.getProject(Constant.PROJECT)
        if (project == null) cropBackground()
        else {
            showLoading(false)
            isProject = true
            indexProject = DataLocalManager.getInt("indexProject")
            project?.let { pro ->
                backgroundModel = pro.backgroundModel
                backgroundModel?.let { bg -> setBackground(bg, null) }
            }
            addMatrixProject()

            DataLocalManager.setProject(null, Constant.PROJECT)
        }
    }

    /**
     * create folder project
     */
    private fun createProject() {
        Thread {
            val numberCurrentProject = DataLocalManager.getInt(Constant.NUMB_PROJECT)
            if (DataLocalManager.getInt(Constant.NUMB_PROJECT) == -1)
                DataLocalManager.setInt(1, Constant.NUMB_PROJECT)
            else
                DataLocalManager.setInt(numberCurrentProject + 1, Constant.NUMB_PROJECT)

            nameFolder =
                Constant.NUMB_PROJECT + "_" + DataLocalManager.getInt(Constant.NUMB_PROJECT)
            Utils.makeFolder(this, nameFolder)
            val file = File(Utils.getStore(this@EditActivity), nameFolder)
            if (file.exists()) {
                nameFolderBackground = nameFolder + "/" + Constant.BACKGROUND
                Utils.makeFolder(this, nameFolderBackground)
                nameFolderImage = nameFolder + "/" + Constant.IMAGE
                Utils.makeFolder(this, nameFolderImage)
            }
        }.start()
    }

    private fun cropBackground() {
        DataLocalManager.getPicture(Constant.BACKGROUND_PICTURE)?.let { pic ->
            val fromAssets = intent.getBooleanExtra(FROM_ASSETS, false)
            val dialogCrop = CropImageDialog(this, fromAssets, pic.uri).apply {
                callBack = object : ICallBackItem {
                    override fun callBack(ob: Any?, position: Int) {
                        if (ob is Bitmap) setBackground(BackgroundModel(), ob)
                    }
                }
            }
            dialogCrop.showDialog()
        }
    }

    private fun setBackground(background: BackgroundModel, tmpBm: Bitmap?) {
        val wMain = (94.44f * w / 100).toInt()
        val hMain = (141.11f * w / 100).toInt()
        val scaleScreen = wMain.toFloat() / hMain

        showLoading()
        lifecycleScope.launch(Dispatchers.IO) {
            val bm: Bitmap
            val bitmap = tmpBm ?: BitmapFactory.decodeFile(background.uriCache)
            if (bitmap.width.toFloat() / bitmap.height > scaleScreen)
                bm = bitmap.scale(wMain, wMain * bitmap.height / bitmap.width, false)
            else {
                bm = bitmap.scale(hMain * bitmap.width / bitmap.height, hMain, false)
                Handler(Looper.getMainLooper()).post {
                    binding.iv.scaleType = ImageView.ScaleType.FIT_XY
                }
            }
            withContext(Dispatchers.Main) {
                binding.iv.layoutParams.width = bm.width
                binding.iv.layoutParams.height = bm.height
                binding.vSticker.layoutParams.width = bm.width
                binding.vSticker.layoutParams.height = bm.height

                binding.ivFrame.layoutParams.width = bm.width
                binding.ivFrame.layoutParams.height = bm.height
                binding.iv.setImageBitmap(bm)
                hideLoading()
            }
        }
    }

    private fun finishUndoRedo(undoRedo: UndoRedoModel) {
        currentProject = undoRedo
        if (undoRedo.isChangeTattoo) {
            undoRedo.tattooModel?.let { tattoo ->
                val tmp = binding.vSticker.listStickers.filter { sticker ->
                    sticker is DrawableStickerCustom && sticker.typeSticker == Constant.TATTOO
                            && sticker.tattooModel.id == tattoo.id
                }
                if (tmp.isNotEmpty()) binding.vSticker.remove(tmp[0])

                val tattooSticker =
                    DrawableStickerCustom(this@EditActivity, tattoo, getId(), Constant.TATTOO)
                binding.vSticker.addSticker(tattooSticker)
            }
        }
        if (undoRedo.isChangeText) {
            undoRedo.textModel?.let { text ->
                val tmp = binding.vSticker.listStickers.filter { sticker ->
                    sticker is TextStickerCustom && sticker.getTextModel().id == text.id
                }
                if (tmp.isNotEmpty()) binding.vSticker.remove(tmp[0])

                val textSticker = TextStickerCustom(this@EditActivity, text, text.id)
                binding.vSticker.addSticker(textSticker)
            }
        }
        if (undoRedo.isChangeFrame) {
            undoRedo.frameModel?.let { frame ->
                if (!binding.ivFrame.isVisible) binding.ivFrame.visible()
                val bm =
                    UtilsBitmap.getBitmapFromAsset(this@EditActivity, frame.folder, frame.name)
                binding.ivFrame.setImageBitmap(bm)
            }
        } else binding.ivFrame.gone()
        if (undoRedo.isChangeBackground) undoRedo.background?.let { setBackground(it, null) }
    }

    private fun evenClick() {
        binding.rlMain.setOnClickListener {
            binding.vSticker.currentSticker = null
            hideAndSeekViewBottom(Constant.VIEW_EDIT_MAIN)
        }
        binding.ivExport.setOnClickListener {
            saveProject(Constant.LIST_COMPLETE, object : ICallBackItem {
                override fun callBack(ob: Any?, position: Int) {
                    showToast(resources.getString(R.string.done), Gravity.BOTTOM)
                    previewFragment =
                        PreviewFragment.newInstance(ob as ProjectModel, 0, object : ICallBackCheck {
                        override fun check(isCheck: Boolean) {
                            finish()
                        }
                    })
                    replaceFragment(supportFragmentManager, previewFragment!!, true, true, true)
                }
            })
        }
        binding.ivBack.setOnClickListener { showDialogBack() }
        binding.ivUndo.setOnClickListener {
            if (undoRedo.isUndoIsPossible) undoRedo.undo()
        }
        binding.ivRedo.setOnClickListener {
            if (undoRedo.isRedoIsPossible) undoRedo.redo()
        }

        //action_main
        with(binding.vMainBar) {
            llTattoos.setOnUnDoubleClickListener {
                binding.vEditText.root.slideOutDown()
                binding.vFilter.root.slideOutDown()
                binding.vFrame.root.slideOutDown()
                binding.vAdjust.root.slideOutDown()

                binding.vTattoos.root.slideInUp()
            }
            llText.setOnUnDoubleClickListener { clickText() }
            llFilter.setOnUnDoubleClickListener {
                binding.vTattoos.root.slideOutDown()
                binding.vEditText.root.slideOutDown()
                binding.vFrame.root.slideOutDown()
                binding.vAdjust.root.slideOutDown()

                binding.vFilter.root.slideInUp()
            }
            llFrame.setOnUnDoubleClickListener {
                binding.vTattoos.root.slideOutDown()
                binding.vEditText.root.slideOutDown()
                binding.vFilter.root.slideOutDown()
                binding.vAdjust.root.slideOutDown()

                binding.vFrame.root.slideInUp()
            }
            llAdjust.setOnUnDoubleClickListener {
                binding.vTattoos.root.slideOutDown()
                binding.vEditText.root.slideOutDown()
                binding.vFilter.root.slideOutDown()
                binding.vFrame.root.slideOutDown()

                binding.vAdjust.root.slideInUp()
                backgroundModel?.let { background -> adjustEditOption("brightness", background.adjustModel) }
            }
        }

        //tattoos
        with (binding.vTattoos) {
            //data_tattoos
            val tattooAdapter = TattooAdapter(this@EditActivity, object : ICallBackItem {
                override fun callBack(ob: Any?, position: Int) {
                    val tattoo = ob as TattooModel
                    tattoo.apply { id = getId() }
                    val tattooSticker =
                        DrawableStickerCustom(this@EditActivity, tattoo, tattoo.id, Constant.TATTOO)
                    binding.vSticker.addSticker(tattooSticker)

                    currentProject = UndoRedoModel().apply {
                        isChangeTattoo = true
                        tattooModel = TattooModel(tattoo)
                    }
                    undoRedo.projectOriginator = currentProject
                    undoRedo.setState()

                    hideAndSeekViewBottom(Constant.VIEW_TATTOO)
                    if (tattoo.isPremium) editColorTattooPremium() else editColorTattoo()
                }
            })
            tattooAdapter.setData(DataTattoo.getDataTattoo(this@EditActivity, Constant.NAME_FOLDER_TATTOO_ASSETS))
            binding.vTattoos.rcvTattoos.apply {
                layoutManager =
                    LinearLayoutManager(this@EditActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = tattooAdapter
            }

            //click none tattoo
            ivNone.setOnUnDoubleClickListener {
                var isTattoo = false
                val lstDel = ArrayList<DrawableStickerCustom>()
                for (sticker in binding.vSticker.listStickers)  {
                    if (sticker is DrawableStickerCustom) {
                        lstDel.add(sticker)
                        isTattoo = sticker.typeSticker == Constant.TATTOO
                    }
                }
                for (sticker in lstDel) binding.vSticker.remove(sticker)

                currentProject?.let {
                    it.isChangeTattoo = isTattoo
                }
                undoRedo.setState()
            }
        }

        //edit_text
        with(binding.vEditText) {
            llColor.setOnUnDoubleClickListener { editColorText() }
            llShadow.setOnUnDoubleClickListener { editShadowText() }
            llEdit.setOnUnDoubleClickListener { clickText() }
        }

        //filter
        with(binding.vFilter) {
            backgroundModel?.let { background ->
                val bitmap = BitmapFactory.decodeFile(background.uriCache)

                val filterAdapter = ImageAdapter(this@EditActivity).apply {
                    callBack = object : ICallBackItem {
                        override fun callBack(ob: Any?, position: Int) {
                            if (ob is FilterModel) clickFilter(ob, position)
                        }
                    }
                }

                lifecycleScope.launch(Dispatchers.IO) {
                    val lstFilter = DataFilter.getDataFilter(
                        bitmap.scale(320, 320 * bitmap.height / bitmap.width, false)
                    )
                    withContext(Dispatchers.Main) {
                        filterAdapter.setData(lstFilter)
                        if (background.positionFilterBackground != -1) {
                            rcvFilter.smoothScrollToPosition(background.positionFilterBackground)
                            filterAdapter.setCurrent(background.positionFilterBackground)
                        }
                    }
                }

                rcvFilter.apply {
                    layoutManager =
                        LinearLayoutManager(this@EditActivity, LinearLayoutManager.HORIZONTAL, false)
                    adapter = filterAdapter
                }
            }

            //click none filter
            ivNone.setOnUnDoubleClickListener {
                showLoading(false)
                lifecycleScope.launch(Dispatchers.IO) {
                    val bm = CGENativeLibrary.cgeFilterImage_MultipleEffects(
                        bitmap, "@adjust lut ", 0.8f
                    )
                    backgroundModel?.let { background ->
                        background.positionFilterBackground = -1
                        background.uriCache = UtilsBitmap.saveBitmapToApp(this@EditActivity, bm,
                            nameFolderBackground, Constant.BACKGROUND
                        )

                        currentProject?.let {
                            it.background = BackgroundModel(background)
                            it.isChangeBackground = true
                        } ?: run {
                            currentProject = UndoRedoModel(background = BackgroundModel(background))
                            undoRedo.projectOriginator = currentProject
                        }
                        undoRedo.setState()
                    }

                    with(Dispatchers.Main) {
                        binding.iv.setImageBitmap(BitmapFactory.decodeFile(backgroundModel!!.uriCache))
                        hideLoading()
                    }
                }
            }
        }

        //frame
        with(binding.vFrame) {
            val frameAdapter = ImageAdapter(this@EditActivity).apply {
                callBack = object : ICallBackItem {
                    override fun callBack(ob: Any?, position: Int) {
                        if (ob is FrameModel) clickFrame(ob)
                    }
                }
                setData(DataFrame.getDataFrame(this@EditActivity, Constant.NAME_FOLDER_FRAME_ASSETS))
            }

            rcvFrame.apply {
                layoutManager =
                    LinearLayoutManager(this@EditActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = frameAdapter
            }

            //click none frame
            ivNone.setOnUnDoubleClickListener {
                binding.ivFrame.gone()

                currentProject?.let {
                    it.background = BackgroundModel(backgroundModel!!)
                    it.isChangeBackground = true
                } ?: run {
                    currentProject = UndoRedoModel(background = BackgroundModel(backgroundModel!!))
                    undoRedo.projectOriginator = currentProject
                }
                undoRedo.setState()
            }
        }

        //adjust
        with(binding.vAdjust) {
            llBrightness.setOnUnDoubleClickListener {
                backgroundModel?.let { background -> adjustEditOption("brightness", background.adjustModel) }
            }
            llContrast.setOnUnDoubleClickListener {
                backgroundModel?.let { background -> adjustEditOption("contrast", background.adjustModel) }
            }
            llExposure.setOnUnDoubleClickListener {
                backgroundModel?.let { background -> adjustEditOption("exposure", background.adjustModel) }
            }
            llSaturation.setOnUnDoubleClickListener {
                backgroundModel?.let { background -> adjustEditOption("saturation", background.adjustModel) }
            }
            llSharpen.setOnUnDoubleClickListener {
                backgroundModel?.let { background -> adjustEditOption("sharpen", background.adjustModel) }
            }
            llVignette.setOnUnDoubleClickListener {
                backgroundModel?.let { background -> adjustEditOption("vignette", background.adjustModel) }
            }
        }
    }

    /**
     * dialog back
     */
    private fun showDialogBack() {
        val bindingDialog = DialogDiscardBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(this, R.style.SheetDialog).create()
        dialog.setUpDialog(bindingDialog.root, true)

        bindingDialog.root.layoutParams.width = (84.44f * w).toInt()

        bindingDialog.tvDiscard.setOnUnDoubleClickListener {
            dialog.dismiss()
            showLoading(false)
            lifecycleScope.launch(Dispatchers.IO) {
                if (!isProject) Utils.delFileInFolder(this@EditActivity, nameFolder, "")

                withContext(Dispatchers.Main) {
                    hideLoading()
                    finish()
                }
            }
        }
        bindingDialog.tvSave.setOnUnDoubleClickListener {
            dialog.dismiss()
            saveProject(Constant.LIST_DRAFT, object : ICallBackItem {
                override fun callBack(ob: Any?, position: Int) {
                    finish()
                }
            })
        }
        bindingDialog.tvCancel.setOnUnDoubleClickListener { dialog.dismiss() }
    }

    /**
     * save project
     */
    private fun saveProject(name: String, isDone: ICallBackItem) {
        showLoading(false)
        val lstSticker = binding.vSticker.listStickers
        val project = ProjectModel()

        lifecycleScope.launch(Dispatchers.IO) {
            val bitmap = if (!isProject) {
                val bmImage = UtilsBitmap.loadBitmapFromView(this@EditActivity, binding.iv)
                UtilsBitmap.overlay(bmImage, binding.vSticker.saveImage(bmImage.width, bmImage.height), false)
            } else {
                val bmImage = UtilsBitmap.loadBitmapFromView(this@EditActivity, binding.iv)
                UtilsBitmap.overlay(bmImage, binding.vSticker.saveImage(bmImage.width, bmImage.height), false)
            }

            project.uriSaved =
                UtilsBitmap.saveBitmapToApp(this@EditActivity, bitmap, nameFolder, Constant.PIC_SAVED)

            project.backgroundModel = backgroundModel
            project.nameFolder = nameFolder
            for (sticker in lstSticker) {
                if (sticker is DrawableStickerCustom) project.lstTattooModel.add(sticker.tattooModel)
                else if (sticker is TextStickerCustom) project.lstTextModel.add(sticker.getTextModel())
            }
            val lstProject = DataLocalManager.getListProject(this@EditActivity, name)
            if (indexProject == -1) lstProject.add(0, project)
            else lstProject[indexProject] = project
            DataLocalManager.setListProject(this@EditActivity, lstProject, name)

            withContext(Dispatchers.Main) {
                hideLoading()
                isDone.callBack(project, -1)
            }
        }
    }

    /**
     * swap view operation sticker
     */
    private fun checkTransformView(sticker: Sticker) {
        if (!sticker.isLook) return
        if (sticker != stickerOld) {
            if (sticker is TextStickerCustom) hideAndSeekViewBottom(Constant.VIEW_ADD_TEXT)
            if (sticker is DrawableStickerCustom) hideAndSeekViewBottom(Constant.VIEW_TATTOO)
            stickerOld = sticker
        }
    }
    private fun hideAndSeekViewBottom(position: Int) {

        when(position) {
            Constant.VIEW_ADD_TEXT -> {
                if (binding.vTattoos.vColor.root.isVisible) binding.vTattoos.vColor.root.gone()

                if (!binding.vEditText.vColor.root.isVisible) binding.vEditText.vColor.root.visible()
            }
            Constant.VIEW_TATTOO -> {
                if (binding.vEditText.vColor.root.isVisible) binding.vEditText.vColor.root.gone()

                if (!binding.vTattoos.vColor.root.isVisible) binding.vTattoos.vColor.root.visible()
            }
            Constant.VIEW_EDIT_MAIN -> {
                if (binding.vTattoos.vColor.root.isVisible) binding.vTattoos.vColor.root.gone()
                if (binding.vEditText.vColor.root.isVisible) binding.vEditText.vColor.root.gone()
            }
        }
    }

    /**
     * matrix sticker
     */
    private fun checkMatrixStickerProject(sticker: Sticker) {
        val matrix = Matrix()
        if (sticker is TextStickerCustom) {
            matrix.setValues(sticker.getTextModel().matrix)
            sticker.setMatrix(matrix)
            indexMatrix++
            setMatrixText(indexMatrix)
            binding.vSticker.invalidate()
        } else {
            val drawableSticker = sticker as DrawableStickerCustom
            matrix.setValues(drawableSticker.tattooModel.matrix)
            sticker.setMatrix(matrix)
            indexMatrix++
            setMatrixTattoo(indexMatrix)
            binding.vSticker.invalidate()
        }
    }
    private fun addMatrixProject() {
        project?.let { pro ->
            if (pro.lstTextModel.isNotEmpty()) {
                indexMatrix = 0
                setMatrixText(indexMatrix)
            }
            else if (pro.lstTattooModel.isNotEmpty()) {
                indexMatrix = 0
                setMatrixTattoo(indexMatrix)
            }
            else project = null
        }

        hideLoading()
    }
    private fun setMatrixText(index: Int) {
        project?.let { pro ->
            if (index >= pro.lstTextModel.size) {
                if (pro.lstTattooModel.isNotEmpty()) {
                    indexMatrix = 0
                    setMatrixTattoo(indexMatrix)
                } else project = null
                return
            }
            val textModel = pro.lstTextModel[index]
            val textSticker = TextStickerCustom(this, textModel, getId())
            binding.vSticker.addSticker(textSticker)
        }
    }
    private fun setMatrixTattoo(index: Int) {
        project?.let { pro ->
            if (index >= pro.lstTattooModel.size) {
                project = null
                return
            }
            val tattoo = pro.lstTattooModel[index]
            val drawableSticker = DrawableStickerCustom(this, tattoo, getId(), Constant.TATTOO)
            binding.vSticker.addSticker(drawableSticker)
        }
    }
    private fun setMatrixToModel(sticker: Sticker, isUndoRedo: Boolean) {
        val matrix = FloatArray(9)
        sticker.matrix.getValues(matrix)
        if (sticker is TextStickerCustom) {
            sticker.getTextModel().matrix = matrix

            if (isUndoRedo) {
                currentProject?.let {
                    it.isChangeText = true
                    it.textModel = TextModel(sticker.getTextModel())
                }
                undoRedo.setState()
            }
        }
        if (sticker is DrawableStickerCustom) {
            sticker.tattooModel.matrix = matrix

            if (isUndoRedo) {
                currentProject?.let {
                    it.isChangeTattoo = true
                    it.tattooModel = TattooModel(sticker.tattooModel)
                }
                undoRedo.setState()
            }
        }
    }

    /**
     * tattoos
     */
    private fun editColorTattoo() {

        val drawableSticker = currentSticker as DrawableStickerCustom
        binding.vTattoos.vColor.vSeekbar.setProgress(drawableSticker.tattooModel.opacity * 100 / 255)

        val colorAdapter = ColorAdapter(this@EditActivity).apply {
            callBack = object : ICallBackItem {
                override fun callBack(ob: Any?, position: Int) {
                    if (ob is ColorModel) {
                        if (ob.colorEnd == null && ob.colorStart == null) {
                            DataColor.showDialogPickColor(this@EditActivity, object : ICallBackItem {
                                override fun callBack(ob: Any?, position: Int) {
                                    drawableSticker.setColor(ob as ColorModel)
                                    binding.vSticker.invalidate()

                                    currentProject?.let {
                                        it.isChangeTattoo = true
                                        it.tattooModel = TattooModel(drawableSticker.tattooModel)
                                    }
                                    undoRedo.setState()
                                }
                            })
                        } else {
                            drawableSticker.setColor(ob)
                            binding.vSticker.invalidate()

                            currentProject?.let {
                                it.isChangeTattoo = true
                                it.tattooModel = TattooModel(drawableSticker.tattooModel)
                            }
                            undoRedo.setState()
                        }
                    }
                }
            }
            setData(DataColor.getListColor(this@EditActivity))
        }

        binding.vTattoos.vColor.vHSL.gone()
        binding.vTattoos.vColor.rcv.apply {
            visible()
            layoutManager =
                LinearLayoutManager(this@EditActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = colorAdapter
        }

        drawableSticker.tattooModel.colorModel?.let {
            colorAdapter.setCurrent(it)
            binding.vTattoos.vColor.rcv.scrollToPosition(colorAdapter.getPosition(it))
        }

        //opacity
        binding.vTattoos.vColor.vSeekbar.onSeekbarResult = object : OnSeekbarResult {
                override fun onDown(v: View) {

                }

                override fun onMove(v: View, value: Int) {
                    drawableSticker.tattooModel.apply { opacity = value * 255 / 100 }
                    binding.vSticker.replace(
                        drawableSticker.tattooModel.opacity(this@EditActivity, drawableSticker),
                        true
                    )
                }

                override fun onUp(v: View, value: Int) {
                    currentProject?.let {
                        it.isChangeTattoo = true
                        it.tattooModel = TattooModel(drawableSticker.tattooModel)
                    }
                    undoRedo.setState()
                }
            }
    }

    private fun editColorTattooPremium() {

        val drawableSticker = currentSticker as DrawableStickerCustom
        binding.vTattoos.vColor.vSeekbar.setProgress(drawableSticker.tattooModel.opacity * 100 / 255)

        //opacity
        binding.vTattoos.vColor.vSeekbar.onSeekbarResult = object : OnSeekbarResult {
            override fun onDown(v: View) {

            }

            override fun onMove(v: View, value: Int) {
                drawableSticker.tattooModel.opacity = value * 255 / 100
                binding.vSticker.replace(drawableSticker, true)
            }

            override fun onUp(v: View, value: Int) {
                currentProject?.let {
                    it.isChangeTattoo = true
                    it.tattooModel = TattooModel(drawableSticker.tattooModel)
                }
                undoRedo.setState()
            }
        }

        //pick color
        binding.vTattoos.vColor.rcv.gone()
        binding.vTattoos.vColor.vHSL.apply {
            visible()
            addListener(object :
                ColorSeekBar.OnColorPickListener<ColorSeekBar<IntegerHSLColor>, IntegerHSLColor> {
                override fun onColorChanged(picker: ColorSeekBar<IntegerHSLColor>, color: IntegerHSLColor, value: Int) {
                    drawableSticker.tattooModel.colorModel?.let {
                        it.colorStart = color.floatH.toInt()
                        it.colorEnd = color.floatH.toInt()
                    }
                    binding.vSticker.replace(drawableSticker, true)
                }

                override fun onColorPicked(picker: ColorSeekBar<IntegerHSLColor>, color: IntegerHSLColor, value: Int, fromUser: Boolean) {
                    currentProject?.let {
                        it.isChangeTattoo = true
                        it.tattooModel = TattooModel(drawableSticker.tattooModel)
                    }
                    undoRedo.setState()
                }

                override fun onColorPicking(picker: ColorSeekBar<IntegerHSLColor>, color: IntegerHSLColor, value: Int, fromUser: Boolean) {
                    drawableSticker.tattooModel.colorModel?.let {
                        it.colorStart = color.floatH.toInt()
                        it.colorEnd = color.floatH.toInt()
                    }
                    binding.vSticker.replace(drawableSticker, true)
                }
            })
        }
    }

    /**
     * text
     */
    private fun clickText() {
        if (currentSticker is TextStickerCustom) {
            val textSticker = currentSticker as TextStickerCustom

            val textModel = textSticker.getTextModel()

            val addTextDialog = AddTextDialog(this, textModel).apply {
                callBack = object : ICallBackItem {
                    override fun callBack(ob: Any?, position: Int) {
                        if (ob is TextModel) {
                            ob.id = getId()

                            textModel.content = ob.content
                            textModel.fontModel = ob.fontModel
                            textSticker.setData(textModel)
                            binding.vSticker.invalidate()

                            currentProject = UndoRedoModel().apply {
                                isChangeText = true
                                this.textModel = TextModel(textModel)
                            }
                            undoRedo.projectOriginator = currentProject
                            undoRedo.setState()
                            hideAndSeekViewBottom(Constant.VIEW_ADD_TEXT)
                            editColorText()
                        }
                    }
                }
            }
            if (!(isFinishing || isDestroyed)) addTextDialog.showDialog()
        }
    }
    private fun editColorText() {
        val textSticker = currentSticker as TextStickerCustom
        binding.vEditText.vColor.vSeekbar.setProgress(textSticker.getTextModel().opacity * 100 / 255)
        binding.vEditText.vColor.tv.text = getString(R.string.opacity)

        val colorAdapter = ColorAdapter(this@EditActivity).apply {
            callBack = object : ICallBackItem {
                override fun callBack(ob: Any?, position: Int) {
                    if (ob is ColorModel) {
                        if (ob.colorEnd == null && ob.colorStart == null)
                            DataColor.showDialogPickColor(this@EditActivity, object : ICallBackItem {
                                override fun callBack(ob: Any?, position: Int) {
                                    textSticker.setTextColor(ob as ColorModel)
                                    binding.vSticker.invalidate()

                                    currentProject?.let {
                                        it.isChangeText = true
                                        it.textModel = TextModel(textSticker.getTextModel())
                                    }
                                    undoRedo.setState()
                                }
                            })
                        else {
                            textSticker.setTextColor(ob)
                            binding.vSticker.invalidate()

                            currentProject?.let {
                                it.isChangeText = true
                                it.textModel = TextModel(textSticker.getTextModel())
                            }
                            undoRedo.setState()
                        }
                    }
                }
            }
            setData(DataColor.getListColor(this@EditActivity))
        }

        binding.vEditText.vColor.rcv.apply {
            layoutManager =
                LinearLayoutManager(this@EditActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = colorAdapter
        }

        textSticker.getTextModel().colorModel?.let {
            colorAdapter.setCurrent(it)
            binding.vEditText.vColor.rcv.scrollToPosition(colorAdapter.getPosition(it))
        }

        //opacity
        binding.vEditText.vColor.vSeekbar.onSeekbarResult = object : OnSeekbarResult {
            override fun onDown(v: View) {

            }

            override fun onMove(v: View, value: Int) {
                textSticker.getTextModel().apply { opacity = value * 255 / 100 }
                binding.vSticker.replace(
                    textSticker.getTextModel().opacity(this@EditActivity, textSticker),
                    true
                )
            }

            override fun onUp(v: View, value: Int) {
                currentProject?.let {
                    it.isChangeText = true
                    it.textModel = TextModel(textSticker.getTextModel())
                }
                undoRedo.setState()
            }
        }
    }
    private fun editShadowText() {

        val textSticker = currentSticker as TextStickerCustom
        textSticker.getTextModel().shadowModel?.let {
            binding.vEditText.vColor.vSeekbar.setProgress((it.blur * 10).toInt())
        }

        binding.vEditText.vColor.tv.text = getString(R.string.shadow)
        val colorAdapter = ColorAdapter(this@EditActivity).apply {
            callBack = object : ICallBackItem {
                override fun callBack(ob: Any?, position: Int) {
                    if (ob is ColorModel) {
                        if (ob.colorStart == null && ob.colorEnd == null) {
                            DataColor.showDialogPickColor(this@EditActivity, object : ICallBackItem {
                                override fun callBack(ob: Any?, position: Int) {
                                    textSticker.setTextColor(ob as ColorModel)
                                    binding.vSticker.invalidate()

                                    currentProject?.let {
                                        it.isChangeText = true
                                        it.textModel = TextModel(textSticker.getTextModel())
                                    }
                                    undoRedo.setState()
                                }
                            })
                        } else {
                            textSticker.getTextModel().shadowModel?.colorBlur = ob.colorStart!!
                            binding.vSticker.replace(
                                textSticker.getTextModel().shadow(this@EditActivity, textSticker),
                                true
                            )

                            currentProject?.let {
                                it.isChangeText = true
                                it.textModel = TextModel(textSticker.getTextModel())
                            }
                            undoRedo.setState()
                        }
                    }
                }
            }
            setData(DataColor.getListColor(this@EditActivity))
        }

        binding.vEditText.vColor.rcv.apply {
            layoutManager =
                LinearLayoutManager(this@EditActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = colorAdapter
        }

        textSticker.getTextModel().shadowModel?.let {
            colorAdapter.setCurrent(it.colorBlur)
            binding.vEditText.vColor.rcv.scrollToPosition(colorAdapter.getPosition(it.colorBlur))
        }

        //blur shadow
        binding.vEditText.vColor.vSeekbar.onSeekbarResult = object : OnSeekbarResult {
            override fun onDown(v: View) {

            }

            override fun onMove(v: View, value: Int) {
                textSticker.getTextModel().shadowModel?.let {
                    it.blur = value / 10f
                }
                binding.vSticker.replace(
                    textSticker.getTextModel().shadow(this@EditActivity, textSticker),
                    true
                )
            }

            override fun onUp(v: View, value: Int) {
                currentProject?.let {
                    it.isChangeText = true
                    it.textModel = TextModel(textSticker.getTextModel())
                }
                undoRedo.setState()
            }
        }
    }

    /**
     * filter
     */
    private fun clickFilter(filter: FilterModel, position: Int) {
        showLoading(false)
        lifecycleScope.launch(Dispatchers.IO) {
            val bm = CGENativeLibrary.cgeFilterImage_MultipleEffects(
                bitmap, "@adjust lut " + filter.parameterFilter, 0.8f
            )
            backgroundModel?.let { background ->
                background.positionFilterBackground = position
                background.uriCache =
                    UtilsBitmap.saveBitmapToApp(this@EditActivity, bm,
                        nameFolderBackground, Constant.BACKGROUND
                    )
                currentProject = UndoRedoModel(
                    background = BackgroundModel(background),
                    isChangeBackground = true
                )
                undoRedo.projectOriginator = currentProject
                undoRedo.setState()
            }

            withContext(Dispatchers.Main) {
                binding.iv.setImageBitmap(BitmapFactory.decodeFile(backgroundModel!!.uriCache))
                hideLoading()
            }
        }
    }

    /**
     * frame
     */
    private fun clickFrame(frame: FrameModel) {
        val bm = UtilsBitmap.getBitmapFromAsset(this@EditActivity, frame.folder, frame.name)
        binding.ivFrame.setImageBitmap(bm)

        backgroundModel?.let { background ->
            currentProject = UndoRedoModel(
                background = BackgroundModel(background),
                isChangeFrame = true,
                frameModel = FrameModel(frame)
            )
            undoRedo.projectOriginator = currentProject
            undoRedo.setState()
        }
    }

    /**
     * adjust
     */
    private fun adjustEditOption(option: String, adjust: AdjustModel?) {
        adjust?.let {
            when (option) {
                "brightness" -> {
                    binding.vAdjust.tv.text = getString(R.string.brightness)
                    binding.vAdjust.vSeekbar.setProgress(adjust.brightness.toInt() / 2)
                }
                "contrast" -> {
                    binding.vAdjust.tv.text = getString(R.string.contrast)
                    binding.vAdjust.vSeekbar.setProgress(adjust.contrast.toInt())
                }
                "saturation" -> {
                    binding.vAdjust.tv.text = getString(R.string.saturation)
                    binding.vAdjust.vSeekbar.setProgress(adjust.saturation.toInt() / 2)
                }
                "exposure" -> {
                    binding.vAdjust.tv.text = getString(R.string.exposure)
                    binding.vAdjust.vSeekbar.setProgress(adjust.exposure.toInt() / 4)
                }
                "sharpen" -> {
                    binding.vAdjust.tv.text = getString(R.string.sharpen)
                    binding.vAdjust.vSeekbar.setProgress(adjust.sharpen.toInt() / 2)
                }
                "vignette" -> {
                    binding.vAdjust.tv.text = getString(R.string.vignette)
                    binding.vAdjust.vSeekbar.setProgress(adjust.vignette.toInt() / 2)
                }
            }

            binding.vAdjust.vSeekbar.onSeekbarResult = object : OnSeekbarResult {
                override fun onDown(v: View) {

                }

                override fun onMove(v: View, value: Int) {
                    when (option) {
                        "brightness" -> adjust.brightness = value * 2f
                        "contrast" -> adjust.contrast = value.toFloat()
                        "saturation" -> adjust.saturation = value * 2f
                        "exposure" -> adjust.exposure = value * 4f
                        "sharpen" -> adjust.sharpen = value * 2f
                        "vignette" -> adjust.vignette = value * 2f
                    }
                    adjust(adjust)
                }

                override fun onUp(v: View, value: Int) {

                }
            }
        }
    }
    private fun adjust(adjust: AdjustModel) {
        backgroundModel?.let {
            bitmap = UtilsAdjust.adjust(BitmapFactory.decodeFile(it.uriCache), adjust)
            binding.iv.setImageBitmap(bitmap)

            lifecycleScope.launch(Dispatchers.IO) {
                it.uriCache = UtilsBitmap.saveBitmapToApp(
                    this@EditActivity, bitmap, nameFolderBackground, Constant.BACKGROUND
                )
            }
        }
    }

    /**
     * create id sticker
     */
    private fun getId(): Int {
        return if (binding.vSticker.stickerCount == 0) 0 else binding.vSticker.stickerCount + 1
    }
}