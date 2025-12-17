package com.tattoo.tattoomaker.on.myphoto.model.background

import java.io.Serializable

data class BackgroundModel(
    var uriFrame: String = "",
    var uriCache: String = "",
    var uriRoot: String = "",
    var adjustModel: AdjustModel? = null,
    var positionFilterBackground: Int = -1,
    var opacity: Int = 100
) : Serializable {

    constructor(backgroundModel: BackgroundModel) : this() {
        this.uriFrame = backgroundModel.uriFrame
        this.uriCache = backgroundModel.uriCache
        this.uriRoot = backgroundModel.uriRoot
        this.adjustModel = backgroundModel.adjustModel
        this.positionFilterBackground = backgroundModel.positionFilterBackground
        this.opacity = backgroundModel.opacity
    }
}