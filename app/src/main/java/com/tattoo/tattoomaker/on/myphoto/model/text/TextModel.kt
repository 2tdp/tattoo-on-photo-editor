package com.tattoo.tattoomaker.on.myphoto.model.text

import android.content.Context
import com.tattoo.tattoomaker.on.myphoto.model.ColorModel
import com.tattoo.tattoomaker.on.myphoto.model.ShadowModel
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.EditSticker
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.TextStickerCustom
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.stickerview.Sticker
import java.io.Serializable

class TextModel : EditSticker, Serializable {
    var id = -1
    var content: String? = null
    var fontModel: FontModel? = null
    var colorModel: ColorModel? = null
    var shadowModel: ShadowModel? = null
    var size = 24
    var isFlipX = false
    var isFlipY = false
    var opacity = 255
    var matrix: FloatArray? = null

    constructor()
    constructor(textModel: TextModel) {
        this.id = textModel.id
        this.content = textModel.content
        this.fontModel = textModel.fontModel
        this.colorModel = textModel.colorModel
        this.shadowModel = textModel.shadowModel
        this.size = textModel.size
        this.isFlipX = textModel.isFlipX
        this.isFlipY = textModel.isFlipY
        this.opacity = textModel.opacity
        this.matrix = textModel.matrix
    }
    constructor(
        id: Int,
        content: String?,
        fontModel: FontModel?,
        colorModel: ColorModel?,
        shadowModel: ShadowModel?,
        size: Int,
        flipX: Boolean,
        flipY: Boolean,
        opacity: Int,
        matrix: FloatArray?
    ) {
        this.id = id
        this.content = content
        this.fontModel = fontModel
        this.colorModel = colorModel
        this.shadowModel = shadowModel
        this.size = size
        this.isFlipX = flipX
        this.isFlipY = flipY
        this.opacity = opacity
        this.matrix = matrix
    }

    override fun duplicate(context: Context, id: Int): Sticker {
        var shadow: ShadowModel? = null
        if (shadowModel != null)
            shadow = ShadowModel(shadowModel!!.xPos, shadowModel!!.yPos, shadowModel!!.blur, shadowModel!!.colorBlur)
        val textModel =
            TextModel(this.id, content, fontModel, colorModel, shadow, size, isFlipX, isFlipY, opacity, matrix)
        return TextStickerCustom(context, textModel, id)
    }

    override fun shadow(context: Context, sticker: Sticker): Sticker? {
        if (sticker is TextStickerCustom) return sticker.setShadow(shadowModel)
        return null
    }

    override fun opacity(context: Context, sticker: Sticker): Sticker? {
        if (sticker is TextStickerCustom) return sticker.setAlpha(opacity)

        return null
    }

    override fun flip(context: Context, sticker: Sticker): Sticker? {
        if (sticker is TextStickerCustom) {
            if (isFlipX) sticker.isFlippedHorizontally = true
            if (isFlipY) sticker.isFlippedVertically = true
            return sticker
        }
        return null
    }
}