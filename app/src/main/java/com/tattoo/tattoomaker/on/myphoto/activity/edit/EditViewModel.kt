package com.tattoo.tattoomaker.on.myphoto.activity.edit

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tattoo.tattoomaker.on.myphoto.helper.Constant
import com.tattoo.tattoomaker.on.myphoto.model.ProjectModel
import com.tattoo.tattoomaker.on.myphoto.model.UndoRedoModel
import com.tattoo.tattoomaker.on.myphoto.model.background.AdjustModel
import com.tattoo.tattoomaker.on.myphoto.model.background.BackgroundModel
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager
import com.tattoo.tattoomaker.on.myphoto.undoredo.UndoRedo
import com.tattoo.tattoomaker.on.myphoto.utils.Utils
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsBitmap
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.DrawableStickerCustom
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.TextStickerCustom
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.stickerview.Sticker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * ViewModel for [EditActivity].
 *
 * Responsibilities:
 * - Holds state that must survive config changes:
 *   [backgroundModel], folder paths, session flags ([isProject], [indexProject], [isCropped])
 * - Encapsulates IO operations: [saveProject], [initProject], [deleteProjectFolder]
 * - Exposes [undoRedo] instance (survives rotation) and fires events via [undoRedoEvent] SharedFlow
 * - Exposes save result via [saveProjectResult] SharedFlow
 */
@HiltViewModel
class EditViewModel @Inject constructor() : ViewModel() {

    // ─── Project folder paths ─────────────────────────────────────────────────
    // Written once by initProject(), read-only from Activity via property delegates

    var nameFolder = ""
        private set
    var nameFolderBackground = ""
        private set
    var nameFolderImage = ""
        private set

    // ─── Edit session flags ───────────────────────────────────────────────────
    var isProject = false
    var indexProject = -1
    var isCropped = false

    // ─── Background model ─────────────────────────────────────────────────────
    // Public var: Activity mutates properties (positionFilterBackground, uriCache) inline
    var backgroundModel: BackgroundModel? = null

    // ─── UndoRedo ─────────────────────────────────────────────────────────────

    /** Events fired when undo or redo completes. Activity collects and updates UI. */
    sealed class UndoRedoEvent {
        data class Undo(val model: UndoRedoModel) : UndoRedoEvent()
        data class Redo(val model: UndoRedoModel) : UndoRedoEvent()
    }

    private val _undoRedoEvent = MutableSharedFlow<UndoRedoEvent>(extraBufferCapacity = 1)
    val undoRedoEvent: SharedFlow<UndoRedoEvent> = _undoRedoEvent.asSharedFlow()

    /**
     * UndoRedo instance exposed so Activity can call setState(), undo(), redo(),
     * isUndoIsPossible, isRedoIsPossible, and projectOriginator directly.
     * Survives config change since it lives in ViewModel scope.
     */
    val undoRedo = UndoRedo(object : UndoRedo.OnUndoRedoEventListener {
        override fun onUndoFinished(projectOriginator: UndoRedoModel?) {
            projectOriginator?.let {
                viewModelScope.launch { _undoRedoEvent.emit(UndoRedoEvent.Undo(it)) }
            }
        }

        override fun onRedoFinished(projectOriginator: UndoRedoModel?) {
            projectOriginator?.let {
                viewModelScope.launch { _undoRedoEvent.emit(UndoRedoEvent.Redo(it)) }
            }
        }

        override fun onStateSaveFinished() { /* no-op */ }
    })

    // ─── Save project result ──────────────────────────────────────────────────

    sealed class SaveProjectResult {
        data class Success(val project: ProjectModel) : SaveProjectResult()
        data class Error(val message: String) : SaveProjectResult()
    }

    private val _saveProjectResult = MutableSharedFlow<SaveProjectResult>(extraBufferCapacity = 1)
    val saveProjectResult: SharedFlow<SaveProjectResult> = _saveProjectResult.asSharedFlow()

    // ─── Background initialization ────────────────────────────────────────────

    /**
     * Initialize background model from intent URI.
     * Idempotent: skips re-init on config change (backgroundModel already set).
     */
    fun initBackground(uri: String) {
        if (backgroundModel != null) return
        backgroundModel = BackgroundModel().apply {
            uriRoot = uri
            uriCache = uri
            adjustModel = AdjustModel(0f, 0f, 0f, 0f, 0f, 0f)
        }
    }

    // ─── Project folder setup ─────────────────────────────────────────────────

    /**
     * Create project folder structure on disk. Runs in viewModelScope (IO).
     * Guard: skips if already initialized to handle config change correctly.
     * Should only be called for NEW projects (from Activity.setUp()).
     */
    fun initProject(context: Context) {
        if (nameFolder.isNotEmpty()) return  // config change protection
        viewModelScope.launch(Dispatchers.IO) {
            val numberCurrentProject = DataLocalManager.getInt(Constant.NUMB_PROJECT)
            if (numberCurrentProject == -1)
                DataLocalManager.setInt(1, Constant.NUMB_PROJECT)
            else
                DataLocalManager.setInt(numberCurrentProject + 1, Constant.NUMB_PROJECT)

            nameFolder = Constant.NUMB_PROJECT + "_" + DataLocalManager.getInt(Constant.NUMB_PROJECT)
            Utils.makeFolder(context, nameFolder)
            val file = File(Utils.getStore(context), nameFolder)
            if (file.exists()) {
                nameFolderBackground = "$nameFolder/${Constant.BACKGROUND}"
                Utils.makeFolder(context, nameFolderBackground)
                nameFolderImage = "$nameFolder/${Constant.IMAGE}"
                Utils.makeFolder(context, nameFolderImage)
            }
        }
    }

    // ─── Save project ─────────────────────────────────────────────────────────

    /**
     * Persist project data and composite bitmap to storage (IO).
     *
     * IMPORTANT: Activity must capture and composite the bitmap on Main thread
     * BEFORE calling this function (view captures cannot happen on IO thread).
     *
     * Result delivered via [saveProjectResult] SharedFlow.
     */
    fun saveProject(
        context: Context,
        listName: String,
        lstSticker: List<Sticker>,
        compositeBitmap: Bitmap
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val project = ProjectModel().apply {
                    uriSaved = UtilsBitmap.saveBitmapToApp(
                        context, compositeBitmap, this@EditViewModel.nameFolder, Constant.PIC_SAVED
                    )
                    this.backgroundModel = this@EditViewModel.backgroundModel
                    this.nameFolder = this@EditViewModel.nameFolder
                    for (sticker in lstSticker) {
                        when (sticker) {
                            is DrawableStickerCustom -> sticker.tattooModel?.let { lstTattooModel.add(it) }
                            is TextStickerCustom -> lstTextModel.add(sticker.getTextModel())
                        }
                    }
                }
                val lstProject = DataLocalManager.getListProject(context, listName)
                if (indexProject == -1) lstProject.add(0, project)
                else lstProject[indexProject] = project
                DataLocalManager.setListProject(context, lstProject, listName)
                _saveProjectResult.emit(SaveProjectResult.Success(project))
            } catch (e: Exception) {
                _saveProjectResult.emit(SaveProjectResult.Error(e.message ?: "Unknown error"))
            }
        }
    }

    // ─── Delete project folder ────────────────────────────────────────────────

    /**
     * Delete project folder when user discards.
     * Runs in viewModelScope — completes even if Activity finishes first.
     */
    fun deleteProjectFolder(context: Context) {
        if (nameFolder.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                Utils.delFileInFolder(context, nameFolder, "")
            }
        }
    }
}
