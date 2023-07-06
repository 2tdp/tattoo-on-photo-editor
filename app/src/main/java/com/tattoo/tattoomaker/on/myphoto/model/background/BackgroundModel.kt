package com.tattoo.tattoomaker.on.myphoto.model.background

import java.io.Serializable

class BackgroundModel : Serializable {

    var uriFrame: String? = null
    var uriCache: String? = null
    var uriRoot: String? = null
    var adjustModel: AdjustModel? = null
    var positionFilterBackground = -1
    var opacity = 100

    constructor() {}
    constructor(backgroundModel: BackgroundModel) {
        this.uriFrame = backgroundModel.uriFrame
        this.uriCache = backgroundModel.uriCache
        this.uriRoot = backgroundModel.uriRoot
        this.adjustModel = backgroundModel.adjustModel
        this.positionFilterBackground = backgroundModel.positionFilterBackground
        this.opacity = backgroundModel.opacity
    }
    constructor(
        uriFrame: String?,
        uriCache: String?,
        uriRoot: String?,
        adjustModel: AdjustModel?,
        positionFilterBackground: Int,
        opacity: Int
    ) {
        this.uriFrame = uriFrame
        this.uriCache = uriCache
        this.uriRoot = uriRoot
        this.adjustModel = adjustModel
        this.positionFilterBackground = positionFilterBackground
        this.opacity = opacity
    }
}