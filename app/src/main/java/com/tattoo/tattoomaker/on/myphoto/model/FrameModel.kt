package com.tattoo.tattoomaker.on.myphoto.model

import java.io.Serializable

class FrameModel : Serializable {

    var name: String
    var folder: String
    var isSelected: Boolean

    constructor(frameModel: FrameModel) {
        this.name = frameModel.name
        this.folder = frameModel.folder
        this.isSelected = frameModel.isSelected
    }

    constructor(name: String, folder: String, isSelected: Boolean) {
        this.name = name
        this.folder = folder
        this.isSelected = isSelected
    }
}