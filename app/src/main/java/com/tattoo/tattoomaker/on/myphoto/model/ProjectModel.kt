package com.tattoo.tattoomaker.on.myphoto.model

import com.tattoo.tattoomaker.on.myphoto.model.background.BackgroundModel
import com.tattoo.tattoomaker.on.myphoto.model.text.TextModel
import com.tattoo.tattoomaker.on.myphoto.undoredo.ProjectMemento
import java.io.Serializable


class ProjectModel : Serializable {
    var nameFolder: String? = null
    var uriSaved: String? = null
    var backgroundModel: BackgroundModel? = null
    var lstTextModel = ArrayList<TextModel>()
    var lstTattooPremiumModel = ArrayList<TattooPremiumModel>()
    var lstTattooModel = ArrayList<TattooModel>()
    var isSelect = false

    constructor() {}
    constructor(project: ProjectModel) {
        nameFolder = project.nameFolder
        uriSaved = project.uriSaved
        backgroundModel = project.backgroundModel
        lstTextModel = project.lstTextModel
        lstTattooPremiumModel = project.lstTattooPremiumModel
        lstTattooModel = project.lstTattooModel
        isSelect = project.isSelect
    }

    constructor(
        nameFolder: String?, uriSaved: String?, backgroundModel: BackgroundModel?,
        lstTextModel: ArrayList<TextModel>, lstTattooPremiumModel: ArrayList<TattooPremiumModel>,
        lstTattooModel: ArrayList<TattooModel>, isSelect: Boolean
    ) {
        this.nameFolder = nameFolder
        this.uriSaved = uriSaved
        this.backgroundModel = backgroundModel
        this.lstTextModel = lstTextModel
        this.lstTattooPremiumModel = lstTattooPremiumModel
        this.lstTattooModel = lstTattooModel
        this.isSelect = isSelect
    }
}