package com.tattoo.tattoomaker.on.myphoto.undoredo

import com.tattoo.tattoomaker.on.myphoto.model.FrameModel
import com.tattoo.tattoomaker.on.myphoto.model.TattooModel
import com.tattoo.tattoomaker.on.myphoto.model.TattooPremiumModel
import com.tattoo.tattoomaker.on.myphoto.model.background.BackgroundModel
import com.tattoo.tattoomaker.on.myphoto.model.text.TextModel

class ProjectMemento {

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

    constructor()
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
}