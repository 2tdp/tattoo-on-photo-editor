package com.tattoo.tattoomaker.on.myphoto.model

import android.content.Context
import com.tattoo.tattoomaker.on.myphoto.utils.Constant
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.DrawableStickerCustom
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.EditSticker
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.stickerview.Sticker
import java.io.Serializable
import java.util.ArrayList

class TattooModel : EditSticker, Serializable {

    var id: Int
    var nameDecor: String
    var nameFolder: String
    var lstPathData: ArrayList<String>
    var colorModel: ColorModel?
    var shadowModel: ShadowModel?
    var opacity = 255
    var isFlipX: Boolean
    var isFlipY: Boolean
    var isPremium = false
    var matrix: FloatArray? = null

    fun clone(): TattooModel {
        return TattooModel(
            this.id,
            this.nameDecor,
            this.nameFolder,
            this.lstPathData,
            this.colorModel,
            this.shadowModel,
            this.opacity,
            this.isFlipX,
            this.isFlipY,
            this.isPremium,
            this.matrix)
    }

    constructor(tattooModel: TattooModel) {
        this.id = tattooModel.id
        this.nameDecor = tattooModel.nameDecor
        this.nameFolder = tattooModel.nameFolder
        this.lstPathData = tattooModel.lstPathData
        this.colorModel = tattooModel.colorModel
        this.shadowModel = tattooModel.shadowModel
        this.opacity = tattooModel.opacity
        this.isFlipX = tattooModel.isFlipX
        this.isFlipY = tattooModel.isFlipY
        this.isPremium = tattooModel.isPremium
        this.matrix = tattooModel.matrix
    }

    constructor(
        id: Int, nameDecor: String, nameFolder: String, lstPathData: ArrayList<String>,
        colorModel: ColorModel?, shadowModel: ShadowModel?, opacity: Int, flipX: Boolean,
        flipY: Boolean, isPremium: Boolean, matrix: FloatArray?
    ) {
        this.id = id
        this.nameDecor = nameDecor
        this.nameFolder = nameFolder
        this.lstPathData = lstPathData
        this.colorModel = colorModel
        this.shadowModel = shadowModel
        this.opacity = opacity
        this.isFlipX = flipX
        this.isFlipY = flipY
        this.isPremium = isPremium
        this.matrix = matrix
    }

    override fun duplicate(context: Context, id: Int): Sticker {
        var shadow: ShadowModel? = null
        if (shadowModel != null)
            shadow = ShadowModel(shadowModel!!.xPos, shadowModel!!.yPos, shadowModel!!.blur, shadowModel!!.colorBlur)
        val tattooModel = TattooModel(
            id, nameDecor, nameFolder, lstPathData, colorModel, shadow, opacity, isFlipX, isFlipY,
            isPremium, matrix
        )
        return DrawableStickerCustom(context, tattooModel, id, Constant.TATTOO)
    }

    override fun shadow(context: Context, sticker: Sticker): Sticker? {
        if (sticker is DrawableStickerCustom) return sticker.apply {
            setShadowPathShape(lstPathData)
            setShadow(shadowModel!!)
        }
        return null
    }

    override fun opacity(context: Context, sticker: Sticker): Sticker? {
        if (sticker is DrawableStickerCustom) return sticker.apply { setAlpha(opacity) }
        return null
    }

    override fun flip(context: Context, sticker: Sticker): Sticker? {
        return null
    }
}