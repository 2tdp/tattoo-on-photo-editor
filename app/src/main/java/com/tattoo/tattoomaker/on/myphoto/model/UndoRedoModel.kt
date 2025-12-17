package com.tattoo.tattoomaker.on.myphoto.model

import com.tattoo.tattoomaker.on.myphoto.model.background.BackgroundModel
import com.tattoo.tattoomaker.on.myphoto.model.text.TextModel
import com.tattoo.tattoomaker.on.myphoto.undoredo.ProjectMemento
import java.io.Serializable
import java.util.ArrayList

data class UndoRedoModel(
    var background: BackgroundModel? = null,
    var frameModel: FrameModel? = null,
    var tattooModel: TattooModel? = null,
    var textModel: TextModel? = null,
    var isChangeBackground: Boolean = false,
    var isChangeFrame: Boolean = false,
    var isChangeText: Boolean = false,
    var isChangeTattoo: Boolean = false,
) : Serializable {

    fun createMemento(): ProjectMemento {
        return ProjectMemento(background, frameModel, tattooModel, textModel, isChangeBackground, isChangeFrame, isChangeText,
            isChangeTattoo)
    }

    fun restoreMemento(projectMemento: ProjectMemento?) {
        projectMemento?.let { it->
            this.background = it.background
            this.frameModel = it.frameModel
            this.isChangeBackground = it.isChangeBackground
            this.isChangeFrame = it.isChangeFrame
            this.isChangeText = it.isChangeText
            this.isChangeTattoo = it.isChangeTattoo
            this.textModel = it.textModel
            this.tattooModel = it.tattooModel
        }
    }
}