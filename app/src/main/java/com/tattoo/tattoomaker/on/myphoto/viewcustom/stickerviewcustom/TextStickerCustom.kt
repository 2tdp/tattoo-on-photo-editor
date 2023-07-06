package com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.annotation.Dimension
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.model.ColorModel
import com.tattoo.tattoomaker.on.myphoto.model.ShadowModel
import com.tattoo.tattoomaker.on.myphoto.model.text.TextModel
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsAdjust
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsBitmap
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.stickerview.Sticker


open class TextStickerCustom(context: Context, textModel: TextModel, id: Int) : Sticker() {

    private val context: Context
    private var textModel: TextModel
    val id: Int

    private var realBounds: Rect? = null
    private var textRect: Rect? = null
    var textPaint: TextPaint? = null
    var shadowPaint: TextPaint? = null
    private var drawable: Drawable? = null
    private var staticLayout: StaticLayout? = null
    private lateinit var staticLayoutShadow: StaticLayout
    private var alignment: Layout.Alignment? = null

    /**
     * Upper bounds for text size.
     * This acts as a starting point for resizing.
     */
    private var maxTextSizePixels = 0f

    /**
     * Lower bounds for text size.
     */
    private var minTextSizePixels = 0f

    /**
     * Line spacing multiplier.
     */
    private val lineSpacingMultiplier = 1.0f

    /**
     * Additional line spacing.
     */
    private var lineSpacingExtra = 0.0f

    init {
        this.id = id
        this.context = context
        this.textModel = textModel

        init()
        setData(textModel)
    }

    private fun init() {
        if (drawable == null)
            drawable = ContextCompat.getDrawable(context, R.drawable.sticker_transparent_text)

        textPaint = TextPaint(TextPaint.ANTI_ALIAS_FLAG)
        shadowPaint = TextPaint(TextPaint.ANTI_ALIAS_FLAG)
        realBounds = Rect(0, 0, width, height)
        textRect = Rect(0, 0, width, height)
        minTextSizePixels = convertSpToPx(6f)
        maxTextSizePixels = convertSpToPx(32f)
        alignment = Layout.Alignment.ALIGN_CENTER
        textPaint!!.textSize = maxTextSizePixels
        shadowPaint!!.textSize = maxTextSizePixels
    }

    fun setData(textModel: TextModel) {
        setText(textModel.content!!)

        setTypeface(
            Typeface.createFromAsset(
                context.assets,
                "font_text/${textModel.fontModel!!.nameFont}"
            )
        )
        textModel.colorModel?.let { setTextColor(it) }
        setAlpha(textModel.opacity)
        textModel.shadowModel?.let { setShadow(it) }
        resizeText()
        setTextSize(textModel.size.toFloat())
    }

    override fun draw(canvas: Canvas) {
        if (staticLayout == null) return

        canvas.save()
        canvas.concat(matrix)
        drawable!!.bounds = realBounds!!
        drawable!!.draw(canvas)
        canvas.restore()

        //shadow
        canvas.save()
        canvas.concat(matrix)
        if (textRect!!.width() == width) {
            val dy = height / 2 - staticLayoutShadow.height / 2
            // center vertical
            canvas.translate(0f, dy.toFloat())
        } else {
            val dx = textRect!!.left
            val dy = textRect!!.top + textRect!!.height() / 2 - staticLayoutShadow.height / 2
            canvas.translate(dx.toFloat(), dy.toFloat())
        }
        staticLayoutShadow.draw(canvas)
        canvas.restore()

        //text
        canvas.save()
        canvas.concat(matrix)
        if (textRect!!.width() == width) {
            val dy = height / 2 - staticLayout!!.height / 2
            // center vertical
            canvas.translate(0f, dy.toFloat())
        } else {
            val dx = textRect!!.left
            val dy = textRect!!.top + textRect!!.height() / 2 - staticLayout!!.height / 2
            canvas.translate(dx.toFloat(), dy.toFloat())
        }
        staticLayout!!.draw(canvas)
        canvas.restore()
    }

    fun setText(str: String): TextStickerCustom {
        textModel.apply { content = str }

        createDrawableText()
        return this
    }

    fun setTextModel(textModel: TextModel) {
        this.textModel = textModel
        setData(textModel)
        createDrawableText()
    }

    fun getTextModel(): TextModel {
        return textModel
    }

    val text: String? get() = textModel.content

    fun setTextAlign(alignment: Layout.Alignment): TextStickerCustom {
        this.alignment = alignment

        resizeText()
        setTextSize(textModel.size.toFloat())
        return this
    }

    private fun setTextSize(@Dimension(unit = Dimension.SP) size: Float): TextStickerCustom {
//        createDrawableText();
        textPaint!!.textSize = convertSpToPx(size)
        shadowPaint!!.textSize = convertSpToPx(size)
        maxTextSizePixels = convertSpToPx(size)
        return this
    }

    val textSize: Float get() = convertPxToSp(textPaint!!.textSize)

    private fun setTypeface(typeface: Typeface?): TextStickerCustom {
        textPaint!!.typeface = typeface
        shadowPaint!!.typeface = typeface

        return this
    }

    val color: Int get() = textPaint!!.color

    fun setTextColor(color: ColorModel): TextStickerCustom {
        textModel.colorModel = color
        UtilsAdjust.setColor(color, textPaint!!, width.toFloat(), height.toFloat())
        return this
    }

    fun setShadow(shadow: ShadowModel?): TextStickerCustom {
        if (shadow != null) {
            if (shadow.colorBlur == 0 && shadow.blur == 0f && shadow.xPos == 0f && shadow.yPos == 0f)
                shadowPaint!!.apply {
                    setShadowLayer(shadow.blur, shadow.xPos, shadow.yPos, Color.TRANSPARENT)
                    color = Color.TRANSPARENT
                }
            else if (shadow.colorBlur != 0)
                shadowPaint!!.apply {
                    setShadowLayer(shadow.blur, shadow.xPos, shadow.yPos, shadow.colorBlur)
                    color = shadow.colorBlur
                }
            else shadowPaint!!.apply {
                setShadowLayer(shadow.blur, shadow.xPos, shadow.yPos, ContextCompat.getColor(context, R.color.black))
                color =ContextCompat.getColor(context, R.color.black)
            }
        }

        resizeText()
        setTextSize(textModel.size.toFloat())
        return this
    }

    /**
     * Resize this view's text size with respect to its width and height
     * (minus padding). You should always call this method after the initialization.
     */
    private fun resizeText(): TextStickerCustom {
        val availableHeightPixels = textRect!!.height()
        val availableWidthPixels = textRect!!.width()
        val text: CharSequence? = text

        // Safety check
        // (Do not resize if the view does not have dimensions or if there is no text)
        if (text == null || text.isEmpty() || availableHeightPixels <= 0 || availableWidthPixels <= 0 || maxTextSizePixels <= 0) {
            return this
        }
        var targetTextSizePixels = maxTextSizePixels
        var targetTextHeightPixels =
            getTextHeightPixels(text, availableWidthPixels, targetTextSizePixels)

        // Until we either fit within our TextView or we have reached our minimum text size,
        // incrementally try smaller sizes
        while (targetTextHeightPixels > availableHeightPixels && targetTextSizePixels > minTextSizePixels) {
            targetTextSizePixels = (targetTextSizePixels - 2).coerceAtLeast(minTextSizePixels)
            targetTextHeightPixels =
                getTextHeightPixels(text, availableWidthPixels, targetTextSizePixels)
        }

        // If we have reached our minimum text size and the text still doesn't fit, append an ellipsis
        // (NOTE: Auto-ellipsize doesn't work hence why we have to do it here)
        if (targetTextSizePixels == minTextSizePixels && targetTextHeightPixels > availableHeightPixels) {
            // Make a copy of the original TextPaint object for measuring
            val textPaintCopy = TextPaint(textPaint)
            textPaintCopy.textSize = targetTextSizePixels

            // Measure using a StaticLayout instance
            val staticLayout = StaticLayout.Builder
                .obtain(text, 0, text.length, textPaintCopy, availableWidthPixels)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
                .setIncludePad(false)
                .build()

            // Check that we have a least one line of rendered text
            if (staticLayout.lineCount > 0) {
                // Since the line at the specific vertical position would be cut off,
                // we must trim up to the previous line and add an ellipsis
                val lastLine = staticLayout.getLineForVertical(availableHeightPixels) - 1
                if (lastLine >= 0) {
                    val startOffset = staticLayout.getLineStart(lastLine)
                    var endOffset = staticLayout.getLineEnd(lastLine)
                    var lineWidthPixels = staticLayout.getLineWidth(lastLine)
                    val ellipseWidth = textPaintCopy.measureText(mEllipsis)

                    // Trim characters off until we have enough room to draw the ellipsis
                    while (availableWidthPixels < lineWidthPixels + ellipseWidth) {
                        endOffset--
                        lineWidthPixels = textPaintCopy.measureText(
                            text.subSequence(startOffset, endOffset + 1).toString()
                        )
                    }
                    textModel.content = text.subSequence(0, endOffset).toString() + mEllipsis
                }
            }
        }
        textPaint!!.textSize = targetTextSizePixels
        shadowPaint!!.textSize = targetTextSizePixels
        staticLayout = StaticLayout.Builder
            .obtain(
                textModel.content!!,
                0,
                textModel.content!!.length,
                textPaint!!,
                textRect!!.width()
            )
            .setAlignment(alignment!!)
            .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
            .setIncludePad(true)
            .build()
        staticLayoutShadow = StaticLayout.Builder
            .obtain(
                textModel.content!!,
                0,
                textModel.content!!.length,
                shadowPaint!!,
                textRect!!.width()
            )
            .setAlignment(alignment!!)
            .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
            .setIncludePad(true)
            .build()
        return this
    }

    private fun getTextHeightPixels(
        source: CharSequence,
        availableWidthPixels: Int,
        textSizePixels: Float
    ): Int {
        textPaint!!.textSize = textSizePixels
        // It's not efficient to create a StaticLayout instance every time when measuring,
        // we can use StaticLayout.Builder since api 23.
        val staticLayout = StaticLayout.Builder
            .obtain(source, 0, source.length, textPaint!!, availableWidthPixels)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
            .setIncludePad(true)
            .build()
        return staticLayout.height
    }

    override fun getWidth(): Int {
        return drawable!!.intrinsicWidth
    }

    override fun getHeight(): Int {
        return drawable!!.intrinsicHeight
    }

    override fun setDrawable(drawable: Drawable): TextStickerCustom {
        this.drawable = drawable
        realBounds!![0, 0, width] = height
        textRect!![0, 0, width] = height
        return this
    }

    override fun getDrawable(): Drawable {
        return drawable!!
    }

    override fun setAlpha(@IntRange(from = 0, to = 255) alpha: Int): Sticker {
        textPaint!!.alpha = alpha
        shadowPaint!!.alpha = alpha
        shadowPaint!!.setShadowLayer(
            textModel.shadowModel!!.blur,
            textModel.shadowModel!!.xPos,
            textModel.shadowModel!!.yPos,
            Color.parseColor(UtilsBitmap.toARGBString(alpha, shadowPaint!!.color)))

        return this
    }

    fun setCharacterSpacing(@FloatRange(from = 0.0, to = 1.0) spacing: Float): Sticker {
        textPaint!!.letterSpacing = spacing
        shadowPaint!!.letterSpacing = spacing

        resizeText()
        setTextSize(textModel.size.toFloat())
        return this
    }

    fun setLineSpacing(@FloatRange(from = 0.0, to = 100.0) spacing: Float): Sticker {
        lineSpacingExtra = spacing

        resizeText()
        setTextSize(textModel.size.toFloat())
        return this
    }

    private fun convertSpToPx(scaledPixels: Float): Float {
        return scaledPixels * context.resources.displayMetrics.scaledDensity
    }

    private fun convertPxToSp(scaledPixels: Float): Float {
        return scaledPixels / context.resources.displayMetrics.scaledDensity
    }

    private fun createDrawableText() {
        val text = text
        if (text != null) {
            val line: Int
            var max = 0
            val temp = text.split("\n").toTypedArray()
            line = temp.size
            if (temp.size == 1) max = text.length
            for (s in temp) {
                if (s.length > max) max = s.length
            }
            textPaint!!.getTextBounds(text, 0, text.length, textRect)
            val drawable = GradientDrawable()
            if (text.length > 20 && line > 2) drawable.setSize(
                (textRect!!.width() * 1.1f * max / text.length).toInt(),
                (textRect!!.height() * 1.2f * line).toInt()
            ) else if (text.length < 20)
                drawable.setSize(
                    (textRect!!.width() * 1.2f).toInt(), (textRect!!.height() * line * 2f).toInt()
                ) else
                drawable.setSize(
                    (convertSpToPx(284f) + convertSpToPx(textPaint!!.textSize)).toInt(),
                    (convertSpToPx(134f) + convertSpToPx(textPaint!!.textSize)).toInt()
                )

            drawable.setColor(Color.TRANSPARENT)
            setDrawable(drawable)
            staticLayout = StaticLayout.Builder
                .obtain(text, 0, text.length, textPaint!!, textRect!!.width())
                .setAlignment(alignment!!)
                .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
                .setIncludePad(true)
                .build()
            staticLayoutShadow = StaticLayout.Builder
                .obtain(text, 0, text.length, shadowPaint!!, textRect!!.width())
                .setAlignment(alignment!!)
                .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
                .setIncludePad(true)
                .build()
        }
    }

    override fun release() {
        super.release()
        if (drawable != null) drawable = null
    }

    companion object {
        private const val mEllipsis = "\u2026"
    }
}