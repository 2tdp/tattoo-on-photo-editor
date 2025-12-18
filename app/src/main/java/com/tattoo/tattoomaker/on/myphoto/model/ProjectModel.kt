package com.tattoo.tattoomaker.on.myphoto.model

import com.tattoo.tattoomaker.on.myphoto.model.background.BackgroundModel
import com.tattoo.tattoomaker.on.myphoto.model.text.TextModel
import com.tattoo.tattoomaker.on.myphoto.undoredo.ProjectMemento
import java.io.Serializable


data class ProjectModel(
    var nameFolder: String? = null,
    var uriSaved: String? = null,
    var backgroundModel: BackgroundModel? = null,
    var lstTextModel: MutableList<TextModel> = mutableListOf(),
    var lstTattooModel: MutableList<TattooModel> = mutableListOf(),
    var isSelect: Boolean = false
) : Serializable