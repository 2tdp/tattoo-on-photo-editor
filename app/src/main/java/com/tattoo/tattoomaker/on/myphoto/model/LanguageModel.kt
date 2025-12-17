package com.tattoo.tattoomaker.on.myphoto.model

import java.util.Locale

data class LanguageModel(
    var name: String = "",
    var uri: String = "",
    var nativeName: String = "",
    var locale: Locale = Locale.ENGLISH,
    var isCheck: Boolean
): Cloneable {
    public override fun clone(): LanguageModel {
        try {
            return super.clone() as LanguageModel
        } catch (e: CloneNotSupportedException) {
            throw kotlin.RuntimeException(e)
        }
    }
}