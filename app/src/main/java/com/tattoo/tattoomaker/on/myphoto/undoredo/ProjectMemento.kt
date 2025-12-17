package com.tattoo.tattoomaker.on.myphoto.undoredo

import com.tattoo.tattoomaker.on.myphoto.model.FrameModel
import com.tattoo.tattoomaker.on.myphoto.model.TattooModel
import com.tattoo.tattoomaker.on.myphoto.model.background.BackgroundModel
import com.tattoo.tattoomaker.on.myphoto.model.text.TextModel

data class ProjectMemento(
    var background: BackgroundModel? = null,
    var frameModel: FrameModel? = null,
    var tattooModel: TattooModel? = null,
    var textModel: TextModel? = null,
    var isChangeBackground: Boolean = false,
    var isChangeFrame: Boolean = false,
    var isChangeText: Boolean = false,
    var isChangeTattoo: Boolean = false
)