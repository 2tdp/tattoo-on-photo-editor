package com.tattoo.tattoomaker.on.myphoto.model

import com.tattoo.tattoomaker.on.myphoto.model.background.BackgroundModel
import com.tattoo.tattoomaker.on.myphoto.model.text.TextModel
import com.tattoo.tattoomaker.on.myphoto.undoredo.ProjectMemento
import java.io.Serializable
import java.util.ArrayList

class UndoRedoModel : Serializable {

    var background: BackgroundModel? = null
    var frameModel: FrameModel? = null
    var tattooModel: TattooModel? = null
    var tattooPremiumModel: TattooPremiumModel? = null
    var textModel: TextModel? = null

    var isChangeBackground = false
    var isChangeFrame = false
    var isChangeText = false
    var isChangeTattoo = false
    var isChangeTattooPremium = false

    constructor() {}
    constructor(
        backgroundModel: BackgroundModel?,
        frameModel: FrameModel?,
        isChangeBackground: Boolean,
        isChangeFrame: Boolean,
        isChangeText: Boolean,
        isChangeTattoo: Boolean,
        isChangeTattooPremium: Boolean,
        tattooModel: TattooModel?,
        tattooPremiumModel: TattooPremiumModel?,
        textModel: TextModel?
        ) {
        this.background = backgroundModel
        this.frameModel = frameModel
        this.isChangeBackground = isChangeBackground
        this.isChangeFrame = isChangeFrame
        this.isChangeText = isChangeText
        this.isChangeTattoo = isChangeTattoo
        this.isChangeTattooPremium = isChangeTattooPremium
        this.textModel = textModel
        this.tattooPremiumModel = tattooPremiumModel
        this.tattooModel = tattooModel
    }

    fun createMemento(): ProjectMemento {
        return ProjectMemento(background, frameModel, isChangeBackground, isChangeFrame, isChangeText,
            isChangeTattoo, isChangeTattooPremium, tattooModel, tattooPremiumModel, textModel)
    }

    fun restoreMemento(projectMemento: ProjectMemento?) {
        projectMemento?.let { it->
            this.background = it.background
            this.frameModel = it.frameModel
            this.isChangeBackground = it.isChangeBackground
            this.isChangeFrame = it.isChangeFrame
            this.isChangeText = it.isChangeText
            this.isChangeTattoo = it.isChangeTattoo
            this.isChangeTattooPremium = it.isChangeTattooPremium
            this.textModel = it.textModel
            this.tattooPremiumModel = it.tattooPremiumModel
            this.tattooModel = it.tattooModel
        }
    }
}