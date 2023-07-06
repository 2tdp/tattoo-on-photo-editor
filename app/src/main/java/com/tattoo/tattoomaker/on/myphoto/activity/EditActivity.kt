package com.tattoo.tattoomaker.on.myphoto.activity

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import codes.side.andcolorpicker.model.IntegerHSLColor
import codes.side.andcolorpicker.view.picker.ColorSeekBar
import com.remi.textonphoto.writeonphoto.addtext.customview.OnSeekbarResult
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseActivity
import com.tattoo.tattoomaker.on.myphoto.adapter.ColorAdapter
import com.tattoo.tattoomaker.on.myphoto.adapter.ImageAdapter
import com.tattoo.tattoomaker.on.myphoto.adapter.TattooAdapter
import com.tattoo.tattoomaker.on.myphoto.addview.viewdialog.ViewDialogDiscard
import com.tattoo.tattoomaker.on.myphoto.addview.viewedit.ViewEdit
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackCheck
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.data.DataColor
import com.tattoo.tattoomaker.on.myphoto.data.DataFilter
import com.tattoo.tattoomaker.on.myphoto.data.DataFrame
import com.tattoo.tattoomaker.on.myphoto.data.DataTattoo
import com.tattoo.tattoomaker.on.myphoto.fragment.AddTextFragment
import com.tattoo.tattoomaker.on.myphoto.fragment.CropImageBackgroundFragment
import com.tattoo.tattoomaker.on.myphoto.fragment.PreviewFragment
import com.tattoo.tattoomaker.on.myphoto.model.*
import com.tattoo.tattoomaker.on.myphoto.model.background.AdjustModel
import com.tattoo.tattoomaker.on.myphoto.model.background.BackgroundModel
import com.tattoo.tattoomaker.on.myphoto.model.text.TextModel
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager
import com.tattoo.tattoomaker.on.myphoto.undoredo.UndoRedo
import com.tattoo.tattoomaker.on.myphoto.utils.Constant
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsAdjust
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsBitmap
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.DrawableStickerCustom
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.TextStickerCustom
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.stickerview.Sticker
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.stickerview.StickerView
import org.wysaid.nativePort.CGENativeLibrary
import java.io.File

class EditActivity: BaseActivity() {

    private lateinit var viewEdit: ViewEdit
    private lateinit var stickerView: StickerView
    private var w = 0

    private lateinit var undoRedo: UndoRedo

    private var addTextFragment: AddTextFragment? = null
    private var previewFragment: PreviewFragment? = null
    private var cropTattooPremiumBackgroundFragment: CropImageBackgroundFragment? = null

    private var currentSticker: Sticker? = null
    private lateinit var stickerOld: Sticker
    private var project: ProjectModel? = null
    private var currentProject: UndoRedoModel? = null
    private var backgroundModel: BackgroundModel? = null
    private lateinit var bitmap: Bitmap
    private var indexProject = -1
    private var indexMatrix = 0
    private var isProject = false
    private var isShow = false
    private var isBack = false
    private var isShowDialog = false
    private var isEditAdjust = false

    private var nameFolderBackground = ""
    private var nameFolderImage = ""
    private var nameFolder = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewEdit = ViewEdit(this@EditActivity)
        setContentView(viewEdit)
        stickerView = viewEdit.stickerView
        w = resources.displayMetrics.widthPixels
        backgroundModel = BackgroundModel().apply {
            adjustModel = AdjustModel(0f, 0f, 0f, 0f, 0f, 0f)
        }

        undoRedo = UndoRedo(object : UndoRedo.OnUndoRedoEventListener {
            override fun onUndoFinished(projectOriginator: UndoRedoModel?) {
                projectOriginator?.let {
                    currentProject = projectOriginator
                    if (projectOriginator.isChangeTattoo) {
                        projectOriginator.tattooModel?.let { tattoo ->
                            var stickerDel : Sticker? = null
                            for (sticker in stickerView.listStickers) {
                                if (sticker is DrawableStickerCustom && sticker.typeSticker == Constant.TATTOO
                                    && sticker.tattooModel.id == tattoo.id){
                                    stickerDel = sticker
                                }
                            }
                            stickerDel?.let { stickerView.remove(it) }

                            val tattooSticker =
                                DrawableStickerCustom(this@EditActivity, tattoo, getId(), Constant.TATTOO)
                            stickerView.addSticker(tattooSticker)
                        }
                    }
                    if (projectOriginator.isChangeTattooPremium) {
                        projectOriginator.tattooPremiumModel?.let { tattooPremium ->
                            var stickerDel : Sticker? = null
                            for (sticker in stickerView.listStickers) {
                                if (sticker is DrawableStickerCustom && sticker.typeSticker == Constant.TATTOO_PREMIUM
                                    && sticker.tattooPremiumModel.id == tattooPremium.id){
                                    stickerDel = sticker
                                }
                            }
                            stickerDel?.let { stickerView.remove(it) }

                            val tattooSticker =
                                DrawableStickerCustom(this@EditActivity, tattooPremium, getId(), Constant.TATTOO_PREMIUM)
                            stickerView.addSticker(tattooSticker)
                        }
                    }
                    if (projectOriginator.isChangeText) {
                        projectOriginator.textModel?.let { text ->
                            var stickerDel : Sticker? = null
                            for (sticker in stickerView.listStickers) {
                                if (sticker is TextStickerCustom && sticker.getTextModel().id == text.id)
                                    stickerDel = sticker
                            }
                            stickerDel?.let { stickerView.remove(it) }

                            val textSticker = TextStickerCustom(this@EditActivity, text, text.id)
                            stickerView.addSticker(textSticker)
                        }
                    }
                    if (projectOriginator.isChangeFrame) {
                        projectOriginator.frameModel?.let { frame ->
                            if (!viewEdit.ivFrame.isVisible) viewEdit.ivFrame.visibility = View.VISIBLE
                            val bm =
                                UtilsBitmap.getBitmapFromAsset(this@EditActivity, frame.folder, frame.name)
                            viewEdit.ivFrame.setImageBitmap(bm)
                        }
                    } else viewEdit.ivFrame.visibility = View.GONE
                    if (projectOriginator.isChangeBackground) {
                        projectOriginator.background?.let {bg ->
                            val wMain = (94.44f * w / 100).toInt()
                            val hMain = (141.11f * w / 100).toInt()
                            val scaleScreen = wMain.toFloat() / hMain

                            viewEdit.viewLoading.visibility = View.VISIBLE

                            Thread {
                                val bm: Bitmap
                                val bitmap = BitmapFactory.decodeFile(bg.uriCache)
                                if (bitmap.width.toFloat() / bitmap.height > scaleScreen)
                                    bm = Bitmap.createScaledBitmap(
                                        bitmap,
                                        wMain,
                                        wMain * bitmap.height / bitmap.width,
                                        false
                                    )
                                else {
                                    bm = Bitmap.createScaledBitmap(
                                        bitmap,
                                        hMain * bitmap.width / bitmap.height,
                                        hMain,
                                        false
                                    )
                                    Handler(Looper.getMainLooper()).post {
                                        viewEdit.iv.scaleType = ImageView.ScaleType.FIT_XY
                                    }
                                }

                                Handler(Looper.getMainLooper()).post {
                                    viewEdit.iv.layoutParams.width = bm.width
                                    viewEdit.iv.layoutParams.height = bm.height
                                    viewEdit.stickerView.layoutParams.width = bm.width
                                    viewEdit.stickerView.layoutParams.height = bm.height

                                    viewEdit.ivFrame.layoutParams.width = bm.width
                                    viewEdit.ivFrame.layoutParams.height = bm.height
                                    viewEdit.iv.setImageBitmap(bm)
                                    clickTattoos()
                                    viewEdit.viewLoading.visibility = View.GONE
                                    cropTattooPremiumBackgroundFragment = null
                                }
                            }.start()
                        }
                    }
                }
            }

            override fun onRedoFinished(projectOriginator: UndoRedoModel?) {
                projectOriginator?.let {
                    currentProject = projectOriginator
                    if (projectOriginator.isChangeTattoo) {
                        projectOriginator.tattooModel?.let { tattoo ->
                            var stickerDel : Sticker? = null
                            for (sticker in stickerView.listStickers) {
                                if (sticker is DrawableStickerCustom && sticker.typeSticker == Constant.TATTOO
                                    && sticker.tattooModel.id == tattoo.id){
                                    stickerDel = sticker
                                }
                            }
                            stickerDel?.let { stickerView.remove(it) }

                            val tattooSticker =
                                DrawableStickerCustom(this@EditActivity, tattoo, getId(), Constant.TATTOO)
                            stickerView.addSticker(tattooSticker)
                        }
                    }
                    if (projectOriginator.isChangeTattooPremium) {
                        projectOriginator.tattooPremiumModel?.let { tattooPremium ->
                            var stickerDel : Sticker? = null
                            for (sticker in stickerView.listStickers) {
                                if (sticker is DrawableStickerCustom && sticker.typeSticker == Constant.TATTOO_PREMIUM
                                    && sticker.tattooPremiumModel.id == tattooPremium.id){
                                    stickerDel = sticker
                                }
                            }
                            stickerDel?.let { stickerView.remove(it) }

                            val tattooSticker =
                                DrawableStickerCustom(this@EditActivity, tattooPremium, getId(), Constant.TATTOO_PREMIUM)
                            stickerView.addSticker(tattooSticker)
                        }
                    }
                    if (projectOriginator.isChangeText) {
                        projectOriginator.textModel?.let { text ->
                            var stickerDel : Sticker? = null
                            for (sticker in stickerView.listStickers) {
                                if (sticker is TextStickerCustom && sticker.getTextModel().id == text.id)
                                    stickerDel = sticker
                            }
                            stickerDel?.let { stickerView.remove(it) }

                            val textSticker = TextStickerCustom(this@EditActivity, text, text.id)
                            stickerView.addSticker(textSticker)
                        }
                    }
                    if (projectOriginator.isChangeFrame) {
                        projectOriginator.frameModel?.let { frame ->
                            if (!viewEdit.ivFrame.isVisible) viewEdit.ivFrame.visibility = View.VISIBLE
                            val bm =
                                UtilsBitmap.getBitmapFromAsset(this@EditActivity, frame.folder, frame.name)
                            viewEdit.ivFrame.setImageBitmap(bm)
                        }
                    } else viewEdit.ivFrame.visibility = View.GONE
                    if (projectOriginator.isChangeBackground) {
                        projectOriginator.background?.let {bg ->
                            val wMain = (94.44f * w / 100).toInt()
                            val hMain = (141.11f * w / 100).toInt()
                            val scaleScreen = wMain.toFloat() / hMain

                            viewEdit.viewLoading.visibility = View.VISIBLE

                            Thread {
                                val bm: Bitmap
                                val bitmap = BitmapFactory.decodeFile(bg.uriCache)
                                if (bitmap.width.toFloat() / bitmap.height > scaleScreen)
                                    bm = Bitmap.createScaledBitmap(
                                        bitmap,
                                        wMain,
                                        wMain * bitmap.height / bitmap.width,
                                        false
                                    )
                                else {
                                    bm =Bitmap.createScaledBitmap(
                                        bitmap,
                                        hMain * bitmap.width / bitmap.height,
                                        hMain,
                                        false
                                    )
                                    Handler(Looper.getMainLooper()).post {
                                        viewEdit.iv.scaleType = ImageView.ScaleType.FIT_XY
                                    }
                                }

                                Handler(Looper.getMainLooper()).post {
                                    viewEdit.iv.layoutParams.width = bm.width
                                    viewEdit.iv.layoutParams.height = bm.height
                                    viewEdit.stickerView.layoutParams.width = bm.width
                                    viewEdit.stickerView.layoutParams.height = bm.height

                                    viewEdit.ivFrame.layoutParams.width = bm.width
                                    viewEdit.ivFrame.layoutParams.height = bm.height
                                    viewEdit.iv.setImageBitmap(bm)
                                    clickTattoos()
                                    viewEdit.viewLoading.visibility = View.GONE
                                    cropTattooPremiumBackgroundFragment = null
                                }
                            }.start()
                        }
                    }
                }
            }

            override fun onStateSaveFinished() {

            }
        })

        onBackPressedDispatcher.addCallback(this@EditActivity, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (checkLoading()) return

                if (cropTattooPremiumBackgroundFragment != null)
                    cropTattooPremiumBackgroundFragment?.let {
                    finish()
                    addTextFragment = null
                }
                else if (addTextFragment != null) addTextFragment?.let {
                    supportFragmentManager.popBackStack("AddTextFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    addTextFragment = null
                }
                else if (previewFragment != null) previewFragment?.let {
                    supportFragmentManager.popBackStack("PreviewFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    finish()
                }
                else if (viewEdit.viewBottomEditText.isVisible || viewEdit.viewBottomEditAdjust.isVisible) {
                    stickerView.currentSticker = null
                    viewEdit.swipeOptionBottom("main")
                    hideAndSeekViewBottom(Constant.VIEW_EDIT_MAIN)
                }
                else showDialogBack()
            }
        })

        stickerView.onStickerOperationListener = object : StickerView.OnStickerOperationListener {
            override fun onStickerAdded(sticker: Sticker) {
                currentSticker = sticker
                stickerOld = sticker
                stickerView.hideBorderAndIcon(1)
                if (project != null) checkMatrixStickerProject(sticker)
                checkTransformView(sticker)
                setMatrixToModel(sticker, false)
            }

            override fun onStickerClicked(sticker: Sticker) {
                Log.d("~~~", "callBack2: ${viewEdit.stickerView.layoutParams.height}")
                stickerView.currentSticker = sticker
                stickerView.hideBorderAndIcon(1)
                stickerView.invalidate()
            }

            override fun onStickerDeleted(sticker: Sticker) {
                hideAndSeekViewBottom(Constant.VIEW_EDIT_MAIN)
                viewEdit.swipeOptionBottom("main")
                clickTattoos()
            }

            override fun onStickerDragFinished(sticker: Sticker) {
                stickerView.hideBorderAndIcon(1)
                stickerView.invalidate()

                setMatrixToModel(sticker, true)
            }

            override fun onStickerTouchedDown(sticker: Sticker) {
                currentSticker = sticker
                checkTransformView(sticker)
            }

            override fun onStickerZoomFinished(sticker: Sticker) {
                stickerView.hideBorderAndIcon(1)
                stickerView.invalidate()

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
        if (project == null && !isBack) cropBackground()
        else {
            if (!viewEdit.viewLoading.isVisible) viewEdit.viewLoading.visibility = View.VISIBLE
            isProject = true
            indexProject = DataLocalManager.getInt("indexProject")
            project?.let { pro ->
                backgroundModel = pro.backgroundModel
                backgroundModel?.let { bg ->
                    val wMain = (94.44f * w / 100).toInt()
                    val hMain = (141.11f * w / 100).toInt()
                    val scaleScreen = wMain.toFloat() / hMain

                    viewEdit.viewLoading.visibility = View.VISIBLE

                    Thread {
                        val bm: Bitmap
                        val bitmap = BitmapFactory.decodeFile(bg.uriCache)
                        if (bitmap.width.toFloat() / bitmap.height > scaleScreen)
                            bm = Bitmap.createScaledBitmap(
                                bitmap,
                                wMain,
                                wMain * bitmap.height / bitmap.width,
                                false
                            )
                        else {
                            bm = Bitmap.createScaledBitmap(
                                bitmap,
                                hMain * bitmap.width / bitmap.height,
                                hMain,
                                false
                            )
                            Handler(Looper.getMainLooper()).post {
                                viewEdit.iv.scaleType = ImageView.ScaleType.FIT_XY
                            }
                        }

                        if (currentProject == null) {
                            currentProject = UndoRedoModel().apply {
                                this.background = BackgroundModel(bg)
                                this.isChangeBackground = true
                            }
                            undoRedo.projectOriginator = currentProject
                        } else currentProject?.let {
                            it.background = BackgroundModel(bg)
                            it.isChangeBackground = true
                        }
                        undoRedo.setState()

                        Handler(Looper.getMainLooper()).post {
                            viewEdit.iv.layoutParams.width = bm.width
                            viewEdit.iv.layoutParams.height = bm.height
                            viewEdit.stickerView.layoutParams.width = bm.width
                            viewEdit.stickerView.layoutParams.height = bm.height

                            viewEdit.ivFrame.layoutParams.width = bm.width
                            viewEdit.ivFrame.layoutParams.height = bm.height
                            viewEdit.iv.setImageBitmap(bm)
                            clickTattoos()
                            viewEdit.viewLoading.visibility = View.GONE
                            cropTattooPremiumBackgroundFragment = null
                        }
                    }.start()
                }
            }
            addMatrixProject()

            DataLocalManager.setProject(null, Constant.PROJECT)
        }
    }

    override fun onStop() {
        super.onStop()
        isBack = true
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
        val pic = DataLocalManager.getPicture(Constant.BACKGROUND_PICTURE)
        if (pic != null) {
            cropTattooPremiumBackgroundFragment = CropImageBackgroundFragment.newInstance(pic, object : ICallBackItem {
                override fun callBack(ob: Any, position: Int) {
                    val wMain = (94.44f * w / 100).toInt()
                    val hMain = (141.11f * w / 100).toInt()
                    val scaleScreen = wMain.toFloat() / hMain

                    viewEdit.viewLoading.visibility = View.VISIBLE

                    Thread {
                        val bm: Bitmap
                        val bitmap = ob as Bitmap
                        if (bitmap.width.toFloat() / bitmap.height > scaleScreen)
                            bm = Bitmap.createScaledBitmap(
                                bitmap,
                                wMain,
                                wMain * bitmap.height / bitmap.width,
                                false
                            )
                        else {
                            bm =Bitmap.createScaledBitmap(
                                bitmap,
                                hMain * bitmap.width / bitmap.height,
                                hMain,
                                false
                            )
                            Handler(Looper.getMainLooper()).post {
                                viewEdit.iv.scaleType = ImageView.ScaleType.FIT_XY
                            }
                        }

                        backgroundModel?.let {
                            it.uriCache = UtilsBitmap.saveBitmapToApp(
                                this@EditActivity,
                                bm,
                                nameFolderBackground,
                                Constant.BACKGROUND
                            )
                            it.uriRoot = UtilsBitmap.saveBitmapToApp(
                                this@EditActivity,
                                if (pic.bucket.equals("tattoo_background"))
                                    UtilsBitmap.getBitmapFromAsset(
                                        this@EditActivity,
                                        "tattoo/background",
                                        pic.uri
                                    )!!
                                else UtilsBitmap.getBitmapFromUri(
                                    this@EditActivity,
                                    Uri.parse(pic.uri)
                                )!!,
                                nameFolderBackground,
                                Constant.BACKGROUND_ROOT
                            )

                            if (currentProject == null) {
                                currentProject = UndoRedoModel().apply {
                                    this.background = BackgroundModel(it)
                                    this.isChangeBackground = true
                                }
                                undoRedo.projectOriginator = currentProject
                            } else currentProject?.let {bg ->
                                bg.background = BackgroundModel(it)
                                bg.isChangeBackground = true
                            }
                            undoRedo.setState()
                        }

                        Handler(Looper.getMainLooper()).post {
                            viewEdit.iv.layoutParams.width = bm.width
                            viewEdit.iv.layoutParams.height = bm.height
                            viewEdit.stickerView.layoutParams.width = bm.width
                            viewEdit.stickerView.layoutParams.height = bm.height

                            viewEdit.ivFrame.layoutParams.width = bm.width
                            viewEdit.ivFrame.layoutParams.height = bm.height
                            viewEdit.iv.setImageBitmap(bm)
                            clickTattoos()
                            viewEdit.viewLoading.visibility = View.GONE
                            cropTattooPremiumBackgroundFragment = null
                        }
                    }.start()
                }
            }, object : ICallBackCheck {
                override fun check(isCheck: Boolean) {
                    finish()
                }
            })
            Utils.replaceFragment(supportFragmentManager, cropTattooPremiumBackgroundFragment!!, true, true, true)
        }
    }

    private fun evenClick() {
        viewEdit.rlMain.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            stickerView.currentSticker = null
            viewEdit.swipeOptionBottom("main")
            hideAndSeekViewBottom(Constant.VIEW_EDIT_MAIN)
        }
        viewEdit.viewToolbar.ivRight.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            saveProject(Constant.LIST_COMPLETE, object : ICallBackItem {
                override fun callBack(ob: Any, position: Int) {
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
        viewEdit.viewToolbar.ivBack.setOnClickListener { if (!isShowDialog) showDialogBack() }
        viewEdit.viewToolbar.ivUndo.setOnClickListener {
            if (undoRedo.isUndoIsPossible) undoRedo.undo()
        }
        viewEdit.viewToolbar.ivRedo.setOnClickListener {
            if (undoRedo.isRedoIsPossible) undoRedo.redo()
        }

        viewEdit.viewBottomEdit.viewTattoos.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            clickTattoos()
        }

        viewEdit.viewBottomEdit.viewText.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            clickText()
        }
        viewEdit.viewBottomEditText.vTextColor.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            viewEdit.viewBottomEditText.chooseOption("color_text")
            editColorText()
        }
        viewEdit.viewBottomEditText.vShadowText.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            viewEdit.viewBottomEditText.chooseOption("shadow_text")
            editShadowText()
        }
        viewEdit.viewBottomEditText.vEditText.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            viewEdit.viewBottomEditText.chooseOption("edit_text")
            clickText()
        }

        viewEdit.viewBottomEdit.viewFilter.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            clickFilter()
        }

        viewEdit.viewBottomEdit.viewFrame.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            clickFrame()
        }

        viewEdit.viewBottomEdit.viewAdjust.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            clickAdjust()
            adjustEditOption("brightness", backgroundModel!!.adjustModel)
        }
        viewEdit.viewBottomEditAdjust.viewBrightness.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            viewEdit.viewBottomEditAdjust.chooseOption("brightness")
            adjustEditOption("brightness", backgroundModel!!.adjustModel)
        }
        viewEdit.viewBottomEditAdjust.viewContrast.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            viewEdit.viewBottomEditAdjust.chooseOption("contrast")
            adjustEditOption("contrast", backgroundModel!!.adjustModel)
        }
        viewEdit.viewBottomEditAdjust.viewExposure.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            viewEdit.viewBottomEditAdjust.chooseOption("exposure")
            adjustEditOption("exposure", backgroundModel!!.adjustModel)
        }
        viewEdit.viewBottomEditAdjust.viewSaturation.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            viewEdit.viewBottomEditAdjust.chooseOption("saturation")
            adjustEditOption("saturation", backgroundModel!!.adjustModel)
        }
        viewEdit.viewBottomEditAdjust.viewSharpen.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            viewEdit.viewBottomEditAdjust.chooseOption("sharpen")
            adjustEditOption("sharpen", backgroundModel!!.adjustModel)
        }
        viewEdit.viewBottomEditAdjust.viewVignette.setOnClickListener {
            if (checkLoading()) return@setOnClickListener
            viewEdit.viewBottomEditAdjust.chooseOption("vignette")
            adjustEditOption("vignette", backgroundModel!!.adjustModel)
        }
    }

    /**
     * dialog back
     */
    private fun showDialogBack() {
        val viewDialogDiscard = ViewDialogDiscard(this@EditActivity)

        val dialog = AlertDialog.Builder(this, R.style.SheetDialog).create()
        dialog.setView(viewDialogDiscard)
        dialog.setCancelable(true)
        dialog.show()
        if (dialog.isShowing) isShowDialog = true

        viewDialogDiscard.layoutParams.width = (75f * w / 100).toInt()
        viewDialogDiscard.layoutParams.height = (35.83f * w / 100).toInt()

        viewDialogDiscard.tvDiscard.setOnClickListener {
            if (checkLoading()) return@setOnClickListener

            if (!viewEdit.viewLoading.isVisible) viewEdit.viewLoading.visibility = View.VISIBLE
            Thread {
                if (!isProject) Utils.delFileInFolder(this, nameFolder, "")
            }.start()
            dialog.cancel()
            finish()
            Utils.setAnimExit(this)
            isShowDialog = false
        }
        viewDialogDiscard.tvSave.setOnClickListener {
            if (checkLoading()) return@setOnClickListener

            if (!viewEdit.viewLoading.isVisible) viewEdit.viewLoading.visibility = View.VISIBLE
            saveProject(Constant.LIST_DRAFT, object : ICallBackItem {
                override fun callBack(ob: Any, position: Int) {
                    finish()
                    Utils.setAnimExit(this@EditActivity)
                    isShowDialog = false
                }
            })
        }
        viewDialogDiscard.tvCancel.setOnClickListener { dialog.cancel() }
    }

    /**
     * save project
     */
    private fun saveProject(name: String, isDone: ICallBackItem) {
        if (!viewEdit.viewLoading.isVisible) viewEdit.viewLoading.visibility = View.VISIBLE

        val lstSticker = stickerView.listStickers
        val project = ProjectModel()

        Thread {
            val bitmap = if (!isProject) {
                val bmImage = UtilsBitmap.loadBitmapFromView(this, viewEdit.iv)
                UtilsBitmap.overlay(bmImage, stickerView.saveImage(bmImage.width, bmImage.height), false)
            } else {
                val bmImage = UtilsBitmap.loadBitmapFromView(this, viewEdit.iv)
                UtilsBitmap.overlay(bmImage, stickerView.saveImage(bmImage.width, bmImage.height), false)
            }

            project.uriSaved =
                UtilsBitmap.saveBitmapToApp(this, bitmap, nameFolder, Constant.PIC_SAVED)

            project.backgroundModel = backgroundModel
            project.nameFolder = nameFolder
            for (sticker in lstSticker) {
                if (sticker is DrawableStickerCustom) {
                    when (sticker.typeSticker) {
                        Constant.TATTOO_PREMIUM -> project.lstTattooPremiumModel.add(sticker.tattooPremiumModel)
                        Constant.TATTOO -> project.lstTattooModel.add(sticker.tattooModel)
                    }
                }
                else if (sticker is TextStickerCustom) project.lstTextModel.add(sticker.getTextModel())
            }
            val lstProject = DataLocalManager.getListProject(this, name)
            if (indexProject == -1) lstProject.add(0, project)
            else lstProject[indexProject] = project
            DataLocalManager.setListProject(this@EditActivity, lstProject, name)

            Handler(Looper.getMainLooper()).post {
                isDone.callBack(project, -1)
                if (viewEdit.viewLoading.isVisible) viewEdit.viewLoading.visibility = View.GONE
            }
        }.start()
    }

    /**
     * swap view operation sticker
     */
    private fun checkTransformView(sticker: Sticker) {
        if (!sticker.isLook) return
        if (sticker != stickerOld || viewEdit.viewChildTattoo.isVisible || isShow) {
            if (sticker is TextStickerCustom) hideAndSeekViewBottom(Constant.VIEW_ADD_TEXT)
            if (sticker is DrawableStickerCustom) {
                viewEdit.swipeOptionBottom("main")
                when (sticker.typeSticker) {
                    Constant.TATTOO_PREMIUM -> {
                        viewEdit.hideViewTattooPremium()
                        hideAndSeekViewBottom(Constant.VIEW_TATTOO_PREMIUM)
                    }
                    Constant.TATTOO -> {
                        viewEdit.hideViewTattoo()
                        hideAndSeekViewBottom(Constant.VIEW_TATTOO)
                    }
                }
            }
            stickerOld = sticker
        }
    }
    private fun hideAndSeekViewBottom(position: Int) {
        if (isEditAdjust) {
            Thread {
                backgroundModel!!.uriCache = UtilsBitmap.saveBitmapToApp(
                    this@EditActivity, bitmap,
                    nameFolderBackground, Constant.BACKGROUND
                )
            }.start()
            isEditAdjust = false
        }
        when(position) {
            Constant.VIEW_ADD_TEXT -> {
                if (viewEdit.viewChildTattoo.isVisible) {
                    viewEdit.viewChildTattoo.animation =
                        AnimationUtils.loadAnimation(this@EditActivity, R.anim.slide_out_left)
                    viewEdit.viewChildTattoo.visibility = View.GONE
                }
                if (viewEdit.viewColorTattoo.isVisible) {
                    viewEdit.viewColorTattoo.animation =
                        AnimationUtils.loadAnimation(this@EditActivity, R.anim.slide_out_left)
                    viewEdit.viewColorTattoo.visibility = View.GONE
                }
                if (viewEdit.viewColorTattooPremium.isVisible) {
                    viewEdit.viewColorTattooPremium.animation =
                        AnimationUtils.loadAnimation(this@EditActivity, R.anim.slide_out_left)
                    viewEdit.viewColorTattooPremium.visibility = View.GONE
                }
                viewEdit.swipeOptionBottom("text")
            }
            Constant.VIEW_TATTOO_PREMIUM -> {
                if (viewEdit.viewChildTattoo.isVisible) {
                    viewEdit.viewChildTattoo.animation =
                        AnimationUtils.loadAnimation(this@EditActivity, R.anim.slide_out_left)
                    viewEdit.viewChildTattoo.visibility = View.GONE
                }
                if (!viewEdit.viewColorTattooPremium.isVisible) {
                    viewEdit.viewColorTattooPremium.animation =
                        AnimationUtils.loadAnimation(this@EditActivity, R.anim.slide_in_right)
                    viewEdit.viewColorTattooPremium.visibility = View.VISIBLE
                    editColorTattooPremium()
                    isShow = false
                }
                if (viewEdit.viewColorTattoo.isVisible) {
                    viewEdit.viewColorTattoo.animation =
                        AnimationUtils.loadAnimation(this@EditActivity, R.anim.slide_out_left)
                    viewEdit.viewColorTattoo.visibility = View.GONE
                }
            }
            Constant.VIEW_TATTOO -> {
                if (viewEdit.viewChildTattoo.isVisible) {
                    viewEdit.viewChildTattoo.animation =
                        AnimationUtils.loadAnimation(this@EditActivity, R.anim.slide_out_left)
                    viewEdit.viewChildTattoo.visibility = View.GONE
                }
                if (!viewEdit.viewColorTattoo.isVisible) {
                    viewEdit.viewColorTattoo.animation =
                        AnimationUtils.loadAnimation(this@EditActivity, R.anim.slide_in_right)
                    viewEdit.viewColorTattoo.visibility = View.VISIBLE
                    editColorTattoo()
                    isShow = false
                }
                if (viewEdit.viewColorTattooPremium.isVisible) {
                    viewEdit.viewColorTattooPremium.animation =
                        AnimationUtils.loadAnimation(this@EditActivity, R.anim.slide_out_left)
                    viewEdit.viewColorTattooPremium.visibility = View.GONE
                }
            }
            Constant.VIEW_EDIT_MAIN -> {
                viewEdit.hideViewAdjust()
                viewEdit.hideViewText()
                viewEdit.hideViewTattooPremium()
                clickTattoos()
                isShow = true
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
            stickerView.invalidate()
        } else {
            val drawableSticker = sticker as DrawableStickerCustom
            when (drawableSticker.typeSticker) {
                Constant.TATTOO_PREMIUM -> {
                    matrix.setValues(drawableSticker.tattooPremiumModel.matrix)
                    sticker.setMatrix(matrix)
                    indexMatrix++
                    setMatrixTattooPremium(indexMatrix)
                }
                Constant.TATTOO -> {
                    matrix.setValues(drawableSticker.tattooModel.matrix)
                    sticker.setMatrix(matrix)
                    indexMatrix++
                    setMatrixTattoo(indexMatrix)
                }
            }
            stickerView.invalidate()
        }
    }
    private fun addMatrixProject() {
        project?.let { pro ->
            if (pro.lstTextModel.isNotEmpty()) {
                indexMatrix = 0
                setMatrixText(indexMatrix)
            }
            else if (pro.lstTattooPremiumModel.isNotEmpty()) {
                indexMatrix = 0
                setMatrixTattooPremium(indexMatrix)
            }
            else if (pro.lstTattooModel.isNotEmpty()) {
                indexMatrix = 0
                setMatrixTattoo(indexMatrix)
            }
            else project = null
        }

        if (viewEdit.viewLoading.isVisible) viewEdit.viewLoading.visibility = View.GONE
    }
    private fun setMatrixText(index: Int) {
        project?.let { pro ->
            if (index >= pro.lstTextModel.size) {
                if (pro.lstTattooPremiumModel.isNotEmpty()) {
                    indexMatrix = 0
                    setMatrixTattooPremium(indexMatrix)
                } else if (pro.lstTattooModel.isNotEmpty()) {
                    indexMatrix = 0
                    setMatrixTattoo(indexMatrix)
                } else project = null
                return
            }
            val textModel = pro.lstTextModel[index]
            val textSticker = TextStickerCustom(this, textModel, getId())
            stickerView.addSticker(textSticker)
        }
    }
    private fun setMatrixTattooPremium(index: Int) {
        project?.let { pro ->
            if (index >= pro.lstTattooPremiumModel.size) {
                if (pro.lstTattooModel.isNotEmpty()) {
                    indexMatrix = 0
                    setMatrixTattoo(indexMatrix)
                } else project = null
                return
            }
            val image = pro.lstTattooPremiumModel[index]
            val drawableSticker = DrawableStickerCustom(this, image, getId(), Constant.TATTOO_PREMIUM)
            stickerView.addSticker(drawableSticker)
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
            stickerView.addSticker(drawableSticker)
        }
    }

    private fun setMatrixToModel(sticker: Sticker, isUndoRedo: Boolean) {
        val matrix = FloatArray(9)
        sticker.matrix.getValues(matrix)
        if (sticker is TextStickerCustom) {
            sticker.getTextModel().matrix = matrix

            if (isUndoRedo) {
                currentProject?.let {
                    it.isChangeTattoo = false
                    it.isChangeTattooPremium = false
                    it.isChangeText = true
                    it.isChangeFrame = false
                    it.isChangeBackground = false
                    it.textModel = TextModel(sticker.getTextModel())
                }
                undoRedo.setState()
            }
        }
        if (sticker is DrawableStickerCustom) {
            when (sticker.typeSticker) {
                Constant.TATTOO_PREMIUM -> {
                    sticker.tattooPremiumModel.matrix = matrix

                    if (isUndoRedo) {
                        currentProject?.let {
                            it.isChangeTattoo = false
                            it.isChangeTattooPremium = true
                            it.isChangeText = false
                            it.isChangeFrame = false
                            it.isChangeBackground = false
                            it.tattooPremiumModel = TattooPremiumModel(sticker.tattooPremiumModel)
                        }
                        undoRedo.setState()
                    }
                }
                Constant.TATTOO -> {
                    sticker.tattooModel.matrix = matrix

                    if (isUndoRedo) {
                        currentProject?.let {
                            it.isChangeTattoo = true
                            it.isChangeTattooPremium = false
                            it.isChangeText = false
                            it.isChangeFrame = false
                            it.isChangeBackground = false
                            it.tattooModel = TattooModel(sticker.tattooModel)
                        }
                        undoRedo.setState()
                    }
                }
            }
        }
    }

    /**
     * tattoos
     */
    private fun clickTattoos() {
        viewEdit.viewBottomEdit.chooseOption("tattoos")

        val tattooAdapter = TattooAdapter(this@EditActivity, object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                val tattoo = ob as TattooModel
                tattoo.apply { id = getId() }
                val tattooSticker =
                    DrawableStickerCustom(this@EditActivity, tattoo, tattoo.id, Constant.TATTOO)
                stickerView.addSticker(tattooSticker)
                hideAndSeekViewBottom(Constant.VIEW_TATTOO)

                currentProject = UndoRedoModel().apply {
                    isChangeTattoo = true
                    isChangeTattooPremium = false
                    isChangeText = false
                    isChangeFrame = false
                    isChangeBackground = false
                    tattooModel = TattooModel(tattoo)
                }
                undoRedo.projectOriginator = currentProject
                undoRedo.setState()
            }
        })

        tattooAdapter.setData(DataTattoo.getDataTattoo(this@EditActivity, Constant.NAME_FOLDER_TATTOO_FREE_ASSETS))

        viewEdit.viewChildTattoo.rcv.layoutManager =
            LinearLayoutManager(this@EditActivity, LinearLayoutManager.HORIZONTAL, false)
        viewEdit.viewChildTattoo.rcv.adapter = tattooAdapter

        //click none tattoo
        viewEdit.viewChildTattoo.ivNone.setOnClickListener {
            var isTattoo = false
            var isTattooPremium = false
            val lstDel = ArrayList<DrawableStickerCustom>()
            for (sticker in stickerView.listStickers)  {
                if (sticker is DrawableStickerCustom) {
                    lstDel.add(sticker)
                    if (sticker.typeSticker == Constant.TATTOO) isTattoo = true
                    if (sticker.typeSticker == Constant.TATTOO_PREMIUM) isTattooPremium = true
                }
            }
            for (sticker in lstDel) stickerView.remove(sticker)

            currentProject?.let {
                it.isChangeTattoo = isTattoo
                it.isChangeTattooPremium = isTattooPremium
                it.isChangeText = false
                it.isChangeFrame = false
                it.isChangeBackground = false
            }
            undoRedo.setState()
        }

        //click tattoo premium
        viewEdit.viewChildTattoo.ivPremium.setOnClickListener {
            viewEdit.viewChildTattoo.clickPremium("premium")

            val imageAdapter = ImageAdapter(this@EditActivity, Constant.TATTOO_PREMIUM, object : ICallBackItem {
                override fun callBack(ob: Any, position: Int) {
                    val tattooPremium = ob as TattooPremiumModel
                    tattooPremium.apply { id = getId() }
                    val tattooPremiumSticker =
                        DrawableStickerCustom(this@EditActivity, tattooPremium, tattooPremium.id, Constant.TATTOO_PREMIUM)
                    stickerView.addSticker(tattooPremiumSticker)

                    currentProject?.let {
                        it.isChangeTattoo = false
                        it.isChangeTattooPremium = true
                        it.isChangeText = false
                        it.isChangeFrame = false
                        it.isChangeBackground = false
                        it.tattooPremiumModel = TattooPremiumModel(tattooPremium)
                    }
                    undoRedo.setState()
                }
            })

            imageAdapter.setDataTattooPremium(DataTattoo.getDataTattooPremium(this@EditActivity, Constant.NAME_FOLDER_TATTOO_PREMIUM_ASSETS))

            viewEdit.viewChildTattoo.rcvPremium.layoutManager =
                LinearLayoutManager(this@EditActivity, LinearLayoutManager.HORIZONTAL, false)
            viewEdit.viewChildTattoo.rcvPremium.adapter = imageAdapter
        }
    }
    private fun editColorTattoo() {
        if (checkLoading()) return

        val drawableSticker = currentSticker as DrawableStickerCustom
        viewEdit.viewColorTattoo.vOpacity.sbOpacity.setProgress(drawableSticker.tattooModel.opacity * 100 / 255)

        val colorAdapter = ColorAdapter(this@EditActivity, object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                drawableSticker.setColor(ob as ColorModel)
                stickerView.invalidate()

                currentProject?.let {
                    it.isChangeTattoo = true
                    it.isChangeTattooPremium = false
                    it.isChangeText = false
                    it.isChangeFrame = false
                    it.isChangeBackground = false
                    it.tattooModel = TattooModel(drawableSticker.tattooModel)
                }
                undoRedo.setState()
            }
        })

        colorAdapter.setData(DataColor.getListColor(this@EditActivity))
        viewEdit.viewColorTattoo.rcv.layoutManager =
            LinearLayoutManager(this@EditActivity, LinearLayoutManager.HORIZONTAL, false)
        viewEdit.viewColorTattoo.rcv.adapter = colorAdapter

        drawableSticker.tattooModel.colorModel?.let {
            colorAdapter.setCurrent(it)
            viewEdit.viewColorTattoo.rcv.scrollToPosition(colorAdapter.getPosition(it))
        }

        //opacity
        viewEdit.viewColorTattoo.vOpacity.sbOpacity.onSeekbarResult = object : OnSeekbarResult {
                override fun onDown(v: View) {

                }

                override fun onMove(v: View, value: Int) {
                    drawableSticker.tattooModel.apply { opacity = value * 255 / 100 }
                    stickerView.replace(
                        drawableSticker.tattooModel.opacity(this@EditActivity, drawableSticker),
                        true
                    )
                }

                override fun onUp(v: View, value: Int) {
                    currentProject?.let {
                        it.isChangeTattoo = true
                        it.isChangeTattooPremium = false
                        it.isChangeText = false
                        it.isChangeFrame = false
                        it.isChangeBackground = false
                        it.tattooModel = TattooModel(drawableSticker.tattooModel)
                    }
                    undoRedo.setState()
                }

            }

        //pick color
        viewEdit.viewColorTattoo.ivPickColor.setOnClickListener {
            DataColor.showDialogPickColor(this@EditActivity, object : ICallBackItem {
                override fun callBack(ob: Any, position: Int) {
                    drawableSticker.setColor(ob as ColorModel)
                    stickerView.invalidate()

                    currentProject?.let {
                        it.isChangeTattoo = true
                        it.isChangeTattooPremium = false
                        it.isChangeText = false
                        it.isChangeFrame = false
                        it.isChangeBackground = false
                        it.tattooModel = TattooModel(drawableSticker.tattooModel)
                    }
                    undoRedo.setState()
                }
            })
        }
    }
    private fun editColorTattooPremium() {
        if (checkLoading()) return

        val drawableSticker = currentSticker as DrawableStickerCustom
        viewEdit.viewColorTattooPremium.vOpacity.sbOpacity.setProgress(drawableSticker.tattooPremiumModel.opacity * 100 / 255)

        //opacity
        viewEdit.viewColorTattooPremium.vOpacity.sbOpacity.onSeekbarResult = object : OnSeekbarResult {
            override fun onDown(v: View) {

            }

            override fun onMove(v: View, value: Int) {
                drawableSticker.tattooPremiumModel.apply { opacity = value * 255 / 100 }
                stickerView.replace(
                    drawableSticker.tattooPremiumModel.opacity(this@EditActivity, drawableSticker),
                    true
                )

            }

            override fun onUp(v: View, value: Int) {
                currentProject?.let {
                    it.isChangeTattoo = false
                    it.isChangeTattooPremium = true
                    it.isChangeText = false
                    it.isChangeFrame = false
                    it.isChangeBackground = false
                    it.tattooPremiumModel = TattooPremiumModel(drawableSticker.tattooPremiumModel)
                }
                undoRedo.setState()
            }
        }

        //pick color
        viewEdit.viewColorTattooPremium.sbColor.addListener(object :
            ColorSeekBar.OnColorPickListener<ColorSeekBar<IntegerHSLColor>, IntegerHSLColor> {
            override fun onColorChanged(picker: ColorSeekBar<IntegerHSLColor>, color: IntegerHSLColor, value: Int) {
                drawableSticker.tattooPremiumModel.apply { colorFilter = color.floatH }
                stickerView.replace(
                    drawableSticker.tattooPremiumModel.colorFilter(this@EditActivity, drawableSticker),
                    true
                )
            }

            override fun onColorPicked(picker: ColorSeekBar<IntegerHSLColor>, color: IntegerHSLColor, value: Int, fromUser: Boolean) {
                currentProject?.let {
                    it.isChangeTattoo = false
                    it.isChangeTattooPremium = true
                    it.isChangeText = false
                    it.isChangeFrame = false
                    it.isChangeBackground = false
                    it.tattooPremiumModel = TattooPremiumModel(drawableSticker.tattooPremiumModel)
                }
                undoRedo.setState()
            }

            override fun onColorPicking(picker: ColorSeekBar<IntegerHSLColor>, color: IntegerHSLColor, value: Int, fromUser: Boolean) {
                drawableSticker.tattooPremiumModel.apply { colorFilter = color.floatH }
                stickerView.replace(
                    drawableSticker.tattooPremiumModel.colorFilter(this@EditActivity, drawableSticker),
                    true
                )
            }

        })
    }

    /**
     * text
     */
    private fun clickText() {
        viewEdit.viewBottomEdit.chooseOption("text")

        var textModel : TextModel? = null
        if (currentSticker != null && currentSticker is TextStickerCustom)
            textModel = (currentSticker as TextStickerCustom).getTextModel()

        addTextFragment = AddTextFragment.newInstance(textModel, object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                val text = ob as TextModel
                text.apply { id = getId() }
                if (textModel != null) {
                    (currentSticker as TextStickerCustom).setData(textModel.apply {
                        content = text.content
                        fontModel = text.fontModel
                    })
                    stickerView.invalidate()

                    currentProject = UndoRedoModel().apply {
                        isChangeTattoo = false
                        isChangeTattooPremium = false
                        isChangeText = true
                        isChangeFrame = false
                        isChangeBackground = false
                        this.textModel = TextModel(textModel)
                    }
                    undoRedo.projectOriginator = currentProject
                    undoRedo.setState()
                } else {
                    val textSticker = TextStickerCustom(this@EditActivity, text, text.id)
                    stickerView.addSticker(textSticker)

                    currentProject = UndoRedoModel().apply {
                        isChangeTattoo = false
                        isChangeTattooPremium = false
                        isChangeText = true
                        isChangeFrame = false
                        isChangeBackground = false
                        this.textModel = TextModel(text)
                    }
                    undoRedo.projectOriginator = currentProject
                    undoRedo.setState()
                }
                hideAndSeekViewBottom(Constant.VIEW_ADD_TEXT)
                editColorText()
                addTextFragment = null
            }
        })
        replaceFragment(supportFragmentManager, addTextFragment!!, true, true, true)
    }
    private fun editColorText() {
        if (checkLoading()) return

        val textSticker = currentSticker as TextStickerCustom
        viewEdit.viewColorText.vOpacity.sbOpacity.setProgress(textSticker.getTextModel().opacity * 100 / 255)

        val colorAdapter = ColorAdapter(this@EditActivity, object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                textSticker.setTextColor(ob as ColorModel)
                stickerView.invalidate()

                currentProject?.let {
                    it.isChangeTattoo = false
                    it.isChangeTattooPremium = false
                    it.isChangeText = true
                    it.isChangeFrame = false
                    it.isChangeBackground = false
                    it.textModel = TextModel(textSticker.getTextModel())
                }
                undoRedo.setState()
            }
        })

        colorAdapter.setData(DataColor.getListColor(this@EditActivity))
        viewEdit.viewColorText.rcv.layoutManager =
            LinearLayoutManager(this@EditActivity, LinearLayoutManager.HORIZONTAL, false)
        viewEdit.viewColorText.rcv.adapter = colorAdapter

        textSticker.getTextModel().colorModel?.let {
            colorAdapter.setCurrent(it)
            viewEdit.viewColorText.rcv.scrollToPosition(colorAdapter.getPosition(it))
        }

        //opacity
        viewEdit.viewColorText.vOpacity.sbOpacity.onSeekbarResult = object : OnSeekbarResult {
            override fun onDown(v: View) {

            }

            override fun onMove(v: View, value: Int) {
                textSticker.getTextModel().apply { opacity = value * 255 / 100 }
                stickerView.replace(
                    textSticker.getTextModel().opacity(this@EditActivity, textSticker),
                    true
                )
            }

            override fun onUp(v: View, value: Int) {
                currentProject?.let {
                    it.isChangeTattoo = false
                    it.isChangeTattooPremium = false
                    it.isChangeText = true
                    it.isChangeFrame = false
                    it.isChangeBackground = false
                    it.textModel = TextModel(textSticker.getTextModel())
                }
                undoRedo.setState()
            }

        }

        //pick color
        viewEdit.viewColorText.ivPickColor.setOnClickListener {
            DataColor.showDialogPickColor(this@EditActivity, object : ICallBackItem {
                override fun callBack(ob: Any, position: Int) {
                    textSticker.setTextColor(ob as ColorModel)
                    stickerView.invalidate()

                    currentProject?.let {
                        it.isChangeTattoo = false
                        it.isChangeTattooPremium = false
                        it.isChangeText = true
                        it.isChangeFrame = false
                        it.isChangeBackground = false
                        it.textModel = TextModel(textSticker.getTextModel())
                    }
                    undoRedo.setState()
                }
            })
        }
    }
    private fun editShadowText() {
        if (checkLoading()) return

        val textSticker = currentSticker as TextStickerCustom
        val shadowText = textSticker.getTextModel().shadowModel!!

        val colorAdapter = ColorAdapter(this@EditActivity, object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                textSticker.getTextModel().shadowModel!!.colorBlur = (ob as ColorModel).colorStart
                stickerView.replace(
                    textSticker.getTextModel().shadow(this@EditActivity, textSticker),
                    true
                )

                currentProject?.let {
                    it.isChangeTattoo = false
                    it.isChangeTattooPremium = false
                    it.isChangeText = true
                    it.isChangeFrame = false
                    it.isChangeBackground = false
                    it.textModel = TextModel(textSticker.getTextModel())
                }
                undoRedo.setState()
            }
        })

        colorAdapter.setData(DataColor.getListColor(this@EditActivity))
        viewEdit.viewShadowText.rcv.layoutManager =
            LinearLayoutManager(this@EditActivity, LinearLayoutManager.HORIZONTAL, false)
        viewEdit.viewShadowText.rcv.adapter = colorAdapter

        textSticker.getTextModel().shadowModel?.let {
            colorAdapter.setCurrent(it.colorBlur)
            viewEdit.viewShadowText.rcv.scrollToPosition(colorAdapter.getPosition(it.colorBlur))
        }

        //blur shadow
        viewEdit.viewShadowText.vOpacity.sbOpacity.setProgress((shadowText.blur * 10).toInt())
        viewEdit.viewShadowText.vOpacity.sbOpacity.onSeekbarResult = object : OnSeekbarResult {
            override fun onDown(v: View) {

            }

            override fun onMove(v: View, value: Int) {
                textSticker.getTextModel().shadowModel?.let {
                    it.blur = value / 10f
                }
                stickerView.replace(
                    textSticker.getTextModel().shadow(this@EditActivity, textSticker),
                    true
                )
            }

            override fun onUp(v: View, value: Int) {
                currentProject?.let {
                    it.isChangeTattoo = false
                    it.isChangeTattooPremium = false
                    it.isChangeText = true
                    it.isChangeFrame = false
                    it.isChangeBackground = false
                    it.textModel = TextModel(textSticker.getTextModel())
                }
                undoRedo.setState()
            }
        }
    }

    /**
     * filter
     */
    private fun clickFilter() {
        viewEdit.viewBottomEdit.chooseOption("filter")

        val bitmap = BitmapFactory.decodeFile(backgroundModel!!.uriCache)

        val filterAdapter = ImageAdapter(this@EditActivity, Constant.BACKGROUND_FILTER, object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                val filter = ob as FilterModel
                if (!viewEdit.viewLoading.isVisible) viewEdit.viewLoading.visibility = View.VISIBLE
                Thread {
                    val bm = CGENativeLibrary.cgeFilterImage_MultipleEffects(
                        bitmap, "@adjust lut " + filter.parameterFilter, 0.8f
                    )
                    backgroundModel!!.positionFilterBackground = position
                    backgroundModel!!.uriCache =
                        UtilsBitmap.saveBitmapToApp(this@EditActivity, bm,
                            nameFolderBackground, Constant.BACKGROUND
                        )

                    currentProject = UndoRedoModel().apply {
                        background = BackgroundModel(backgroundModel!!)
                        isChangeTattoo = false
                        isChangeTattooPremium = false
                        isChangeText = false
                        isChangeFrame = false
                        isChangeBackground = true
                    }
                    undoRedo.projectOriginator = currentProject
                    undoRedo.setState()

                    Handler(Looper.getMainLooper()).post {
                        viewEdit.iv.setImageBitmap(BitmapFactory.decodeFile(backgroundModel!!.uriCache))
                        if (viewEdit.viewLoading.isVisible) viewEdit.viewLoading.visibility = View.GONE
                    }
                }.start()
            }
        })

        Thread {
            val lstFilter = DataFilter.getDataFilter(
                Bitmap.createScaledBitmap(
                    bitmap,
                    320, 320 * bitmap.height / bitmap.width,
                    false
                )
            )
            Handler(Looper.getMainLooper()).post {
                filterAdapter.setDataFilter(lstFilter)
                if (backgroundModel!!.positionFilterBackground != -1) {
                    viewEdit.viewEditFilter.rcv.smoothScrollToPosition(backgroundModel!!.positionFilterBackground)
                    filterAdapter.setCurrent(backgroundModel!!.positionFilterBackground)
                }
            }
        }.start()

        viewEdit.viewEditFilter.rcv.layoutManager =
            LinearLayoutManager(this@EditActivity, LinearLayoutManager.HORIZONTAL, false)
        viewEdit.viewEditFilter.rcv.adapter = filterAdapter

        //click none filter
        viewEdit.viewEditFilter.ivNone.setOnClickListener {
            if (!viewEdit.viewLoading.isVisible) viewEdit.viewLoading.visibility = View.VISIBLE
            Thread {
                val bm = CGENativeLibrary.cgeFilterImage_MultipleEffects(
                    bitmap, "@adjust lut ", 0.8f
                )
                backgroundModel!!.positionFilterBackground = -1
                backgroundModel!!.uriCache =
                    UtilsBitmap.saveBitmapToApp(this@EditActivity, bm,
                        nameFolderBackground, Constant.BACKGROUND
                    )

                if (currentProject == null) {
                    currentProject = UndoRedoModel().apply {
                        background = BackgroundModel(backgroundModel!!)
                    }
                    undoRedo.projectOriginator = currentProject
                } else currentProject?.let {
                    it.background = BackgroundModel(backgroundModel!!)
                    it.isChangeTattoo = false
                    it.isChangeTattooPremium = false
                    it.isChangeText = false
                    it.isChangeFrame = false
                    it.isChangeBackground = true
                }
                undoRedo.setState()

                Handler(Looper.getMainLooper()).post {
                    viewEdit.iv.setImageBitmap(BitmapFactory.decodeFile(backgroundModel!!.uriCache))
                    if (viewEdit.viewLoading.isVisible) viewEdit.viewLoading.visibility = View.GONE
                }
            }.start()
        }
    }

    /**
     * frame
     */
    private fun clickFrame() {
        viewEdit.viewBottomEdit.chooseOption("frame")

        val frameAdapter = ImageAdapter(this@EditActivity, Constant.FRAME, object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                val frame = ob as FrameModel
                val bm = UtilsBitmap.getBitmapFromAsset(this@EditActivity, frame.folder, frame.name)
                viewEdit.ivFrame.setImageBitmap(bm)

                currentProject = UndoRedoModel().apply {
                    background = BackgroundModel(backgroundModel!!)
                    isChangeTattoo = false
                    isChangeTattooPremium = false
                    isChangeText = false
                    isChangeFrame = true
                    isChangeBackground = false
                    frameModel = FrameModel(frame)
                }
                undoRedo.projectOriginator = currentProject
                undoRedo.setState()
            }
        })

        frameAdapter.setDataFrame(DataFrame.getDataFrame(this@EditActivity, Constant.NAME_FOLDER_FRAME_ASSETS))

        viewEdit.viewAddFrame.rcv.layoutManager =
            LinearLayoutManager(this@EditActivity, LinearLayoutManager.HORIZONTAL, false)
        viewEdit.viewAddFrame.rcv.adapter = frameAdapter

        //click none frame
        viewEdit.viewAddFrame.ivNone.setOnClickListener {
            viewEdit.ivFrame.visibility = View.GONE

            if (currentProject == null) {
                currentProject = UndoRedoModel().apply {
                    background = BackgroundModel(backgroundModel!!)
                }
                undoRedo.projectOriginator = currentProject
            } else currentProject?.let {
                it.background = BackgroundModel(backgroundModel!!)
                it.isChangeTattoo = false
                it.isChangeTattooPremium = false
                it.isChangeText = false
                it.isChangeFrame = false
                it.isChangeBackground = true
            }
            undoRedo.setState()
        }
    }

    /**
     * adjust
     */
    private fun clickAdjust() {
        viewEdit.viewBottomEdit.chooseOption("adjust")
        viewEdit.swipeOptionBottom("adjust")
    }
    private fun adjustEditOption(option: String, adjust: AdjustModel?) {
        when (option) {
            "brightness" ->
                viewEdit.viewEditAdjust.sbTwoWay.setProgress(adjust!!.brightness.toInt() / 2)
            "contrast" ->
                viewEdit.viewEditAdjust.sbTwoWay.setProgress(adjust!!.contrast.toInt())
            "saturation" ->
                viewEdit.viewEditAdjust.sbTwoWay.setProgress(adjust!!.saturation.toInt() / 2)
            "exposure" -> viewEdit.viewEditAdjust.sbTwoWay.setProgress(adjust!!.exposure.toInt() / 4)
            "sharpen" -> viewEdit.viewEditAdjust.sbTwoWay.setProgress(adjust!!.sharpen.toInt() / 2)
            "vignette" -> viewEdit.viewEditAdjust.sbTwoWay.setProgress(adjust!!.vignette.toInt() / 2)
        }
        viewEdit.viewEditAdjust.sbTwoWay.onSeekbarResult = object : OnSeekbarResult {
            override fun onDown(v: View) {

            }

            override fun onMove(v: View, value: Int) {
                when (option) {
                    "brightness" -> {
                        adjust!!.brightness = value * 2f
                        adjust(adjust)
                    }
                    "contrast" -> {
                        adjust!!.contrast = value.toFloat()
                        adjust(adjust)
                    }
                    "saturation" -> {
                        adjust!!.saturation = value * 2f
                        adjust(adjust)
                    }
                    "exposure" -> {
                        adjust!!.exposure = value * 4f
                        adjust(adjust)
                    }
                    "sharpen" -> {
                        adjust!!.sharpen = value * 2f
                        adjust(adjust)
                    }
                    "vignette" -> {
                        adjust!!.vignette = value * 2f
                        adjust(adjust)
                    }
                }
            }

            override fun onUp(v: View, value: Int) {

            }
        }
    }
    private fun adjust(adjust: AdjustModel) {
        bitmap = UtilsAdjust.adjust(BitmapFactory.decodeFile(backgroundModel!!.uriCache!!), adjust)
        viewEdit.iv.setImageBitmap(bitmap)
        isEditAdjust = true
    }

    /**
     * create id sticker
     */
    private fun getId(): Int {
        return if (stickerView.stickerCount == 0) 0 else stickerView.stickerCount + 1
    }

    /**
     * check loading run
     */
    private fun checkLoading(): Boolean {
        return viewEdit.viewLoading.isVisible
    }
}