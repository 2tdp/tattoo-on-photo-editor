package com.tattoo.tattoomaker.on.myphoto.model

import android.content.Context
import com.tattoo.tattoomaker.on.myphoto.utils.Constant
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.DrawableStickerCustom
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.EditSticker
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.stickerview.Sticker
import java.io.Serializable

class TattooPremiumModel : EditSticker, Serializable {
    var id: Int
    var nameTattoo: String
    var folder: String
    var colorFilter = 0F
    var opacity = 255
    var isFlipX: Boolean
    var isFlipY: Boolean
    var isSelected: Boolean
    var matrix: FloatArray? = null

    constructor(tattooPremiumModel: TattooPremiumModel) {
        id = tattooPremiumModel.id
        nameTattoo = tattooPremiumModel.nameTattoo
        folder = tattooPremiumModel.folder
        colorFilter = tattooPremiumModel.colorFilter
        opacity = tattooPremiumModel.opacity
        isFlipX = tattooPremiumModel.isFlipX
        isFlipY = tattooPremiumModel.isFlipY
        isSelected = tattooPremiumModel.isSelected
        matrix = tattooPremiumModel.matrix
    }

    constructor(
        id: Int, nameTattoo: String, folder: String, colorFilter: Float, opacity: Int,
        flipX: Boolean, flipY: Boolean, isSelected: Boolean, matrix: FloatArray?
    ) {
        this.id = id
        this.nameTattoo = nameTattoo
        this.folder = folder
        this.colorFilter = colorFilter
        this.opacity = opacity
        isFlipX = flipX
        isFlipY = flipY
        this.isSelected = isSelected
        this.matrix = matrix
    }

    override fun duplicate(context: Context, id: Int): Sticker {
        val tattooPremiumModel =
            TattooPremiumModel(id, nameTattoo, folder, colorFilter, opacity, isFlipX, isFlipY, isSelected, matrix)
        return DrawableStickerCustom(context, tattooPremiumModel, id, Constant.TATTOO_PREMIUM)
    }

    override fun flip(context: Context, sticker: Sticker): Sticker? {
        return null
    }

    override fun opacity(context: Context, sticker: Sticker): Sticker? {
        if (sticker is DrawableStickerCustom) return sticker.setAlpha(opacity)
        return null
    }

    override fun shadow(context: Context, sticker: Sticker): Sticker? {
        return null
    }

    fun colorFilter(context: Context, sticker: Sticker): Sticker? {
        if (sticker is DrawableStickerCustom) return sticker.setColorFilter(colorFilter)
        return null
    }
}