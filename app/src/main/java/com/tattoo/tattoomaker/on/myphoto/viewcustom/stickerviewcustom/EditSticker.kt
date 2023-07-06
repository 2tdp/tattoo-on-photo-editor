package com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom

import android.content.Context
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.stickerview.Sticker

abstract class EditSticker {
    abstract fun duplicate(context: Context, id: Int): Sticker?
    abstract fun shadow(context: Context, sticker: Sticker): Sticker?
    abstract fun opacity(context: Context, sticker: Sticker): Sticker?
    abstract fun flip(context: Context, sticker: Sticker): Sticker?
}