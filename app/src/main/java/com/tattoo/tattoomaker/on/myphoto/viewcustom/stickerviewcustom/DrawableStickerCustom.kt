package com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.core.graphics.PathParser
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.model.ColorModel
import com.tattoo.tattoomaker.on.myphoto.model.TattooModel
import com.tattoo.tattoomaker.on.myphoto.model.ShadowModel
import com.tattoo.tattoomaker.on.myphoto.model.TattooPremiumModel
import com.tattoo.tattoomaker.on.myphoto.utils.ColorFilterGenerator
import com.tattoo.tattoomaker.on.myphoto.utils.Constant
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsAdjust
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsBitmap
import com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.stickerview.Sticker

open class DrawableStickerCustom(context: Context, o: Any?, id: Int, typeSticker: String) : Sticker() {

    private val context: Context
    val typeSticker: String
    val id: Int

    private val distance = 10
    private var drawable: Drawable? = null
    var bitmap: Bitmap? = null
    private var realBounds: RectF? = null
    private var rectFShadow: RectF? = null
    private var rectFTattoo: RectF? = null
    private var shadowPath: Path? = null
    private var pathTattoo: Path? = null
    private var shadowPaint: Paint? = null
    private var paintTattoo: Paint? = null
    private val paintBitmap = Paint(Paint.FILTER_BITMAP_FLAG)

    var isShadow = false
    lateinit var tattooModel: TattooModel
    lateinit var tattooPremiumModel: TattooPremiumModel

    init {
        this.id = id
        this.context = context
        this.typeSticker = typeSticker

        if (typeSticker == Constant.TATTOO) {
            tattooModel = o as TattooModel
            initTattoo()
        } else if (typeSticker == Constant.TATTOO_PREMIUM) {
            tattooPremiumModel = o as TattooPremiumModel
            initTattooPremium()
        }
    }

    /**
     * tattoo
     */
    private fun initTattoo() {
        if (drawable == null)
            drawable = ContextCompat.getDrawable(context, R.drawable.sticker_transparent_text)

        if (paintTattoo == null) paintTattoo = Paint(Paint.ANTI_ALIAS_FLAG)
        if (pathTattoo == null) pathTattoo = Path()

        if (tattooModel.lstPathData.isNotEmpty()) {
            pathTattoo!!.reset()
            for (path in tattooModel.lstPathData) {
                if (path == "evenOdd") pathTattoo!!.fillType = Path.FillType.EVEN_ODD
                else if (path.contains("#"))
                    paintTattoo!!.color = Color.parseColor(path)
                else pathTattoo!!.addPath(PathParser.createPathFromPathData(path))
            }
            scalePath()
        }
        if (tattooModel.colorModel != null) setColor(tattooModel.colorModel!!)
        if (tattooModel.shadowModel != null) {
            setShadowPathShape(tattooModel.lstPathData)
            setShadow(tattooModel.shadowModel!!)
        }
        setAlpha(tattooModel.opacity)
    }

    private fun scalePath() {
        if (rectFTattoo == null) rectFTattoo = RectF()
        pathTattoo!!.computeBounds(rectFTattoo!!, true)
        val maxRect = rectFTattoo!!.width().coerceAtLeast(rectFTattoo!!.height())
        val scale = (width - 2f * distance) / maxRect
        val matrix = Matrix()
        matrix.preScale(scale, scale)
        pathTattoo!!.transform(matrix)
        pathTattoo!!.computeBounds(rectFTattoo!!, true)
        createDrawable()
    }

    /**
     * tattoo premium
     */
    private fun initTattooPremium() {
        if (drawable == null)
            drawable = ContextCompat.getDrawable(context, R.drawable.sticker_transparent_text)

        val bm = UtilsBitmap.getBitmapFromAsset(context, tattooPremiumModel.folder, tattooPremiumModel.nameTattoo)

        bm?.let {
            bitmap = if (it.width > it.height)
                Bitmap.createScaledBitmap(it, 720, 720 * it.height / it.width, false)
            else
                Bitmap.createScaledBitmap(it, 720 * it.width / it.height, 720, false)
        }


        setColorFilter(tattooPremiumModel.colorFilter)
        setAlpha(tattooPremiumModel.opacity)

        realBounds = RectF(
            distance.toFloat(),
            distance.toFloat(),
            (width - distance).toFloat(),
            (height - distance).toFloat()
        )
    }

    override fun draw(canvas: Canvas) {
        /**
         * draw shadow
         */
        if (isShadow && (typeSticker == Constant.TATTOO_PREMIUM || typeSticker == Constant.TATTOO)) {
            if (typeSticker == Constant.TATTOO) {
                canvas.save()
                canvas.concat(matrix)
                canvas.translate(
                    realBounds!!.left.toInt().toFloat(),
                    realBounds!!.top.toInt().toFloat()
                )
                canvas.drawPath(pathTattoo!!, shadowPaint!!)
                canvas.restore()
            } else {
                canvas.save()
                canvas.concat(matrix)
                canvas.drawRect(realBounds!!, shadowPaint!!)
                canvas.restore()
            }
        }

        canvas.save()
        canvas.concat(matrix)
        when (typeSticker) {
            Constant.TATTOO_PREMIUM -> bitmap?.let {
                canvas.drawBitmap(it, null, realBounds!!, paintBitmap)
            }
            Constant.TATTOO -> {
                canvas.translate(
                    realBounds!!.left.toInt().toFloat(),
                    realBounds!!.top.toInt().toFloat()
                )
                canvas.drawPath(pathTattoo!!, paintTattoo!!)
            }
        }
        canvas.restore()

        canvas.save()
        canvas.concat(matrix)
        if (typeSticker == Constant.STICKER_ICON) {
            drawable!!.setBounds(0, 0, width, height)
        } else if (typeSticker != Constant.TATTOO && !isShadow)
            if (realBounds != null)
                drawable!!.setBounds(
                    realBounds!!.left.toInt(),
                    realBounds!!.top.toInt(),
                    realBounds!!.right.toInt(),
                    realBounds!!.bottom.toInt()
                )

        drawable!!.draw(canvas)
        canvas.restore()
    }

    override fun getWidth(): Int {
        return if (typeSticker == Constant.TATTOO_PREMIUM && bitmap != null) bitmap!!.width
        else drawable!!.intrinsicWidth
    }

    override fun getHeight(): Int {
        return if (typeSticker == Constant.TATTOO_PREMIUM && bitmap != null) bitmap!!.height
        else drawable!!.intrinsicHeight
    }

    override fun setDrawable(drawable: Drawable): DrawableStickerCustom {
        this.drawable = drawable
        realBounds = RectF(
            5F,
            5F,
            (drawable.intrinsicWidth - 5).toFloat(),
            (drawable.intrinsicHeight - 5).toFloat()
        )
        return this
    }

    override fun getDrawable(): Drawable {
        return drawable!!
    }

    override fun setAlpha(@IntRange(from = 0, to = 255) alpha: Int): DrawableStickerCustom {
        if (typeSticker == Constant.TATTOO_PREMIUM) paintBitmap.alpha = alpha
        if (typeSticker == Constant.TATTOO) paintTattoo!!.alpha = alpha
        if (shadowPaint != null) shadowPaint!!.alpha = alpha
        return this
    }

    fun setColor(color: ColorModel) {
        tattooModel.colorModel = color
        UtilsAdjust.setColor(color, paintTattoo!!, width.toFloat(), height.toFloat())
    }

    fun setColorFilter(color: Float): DrawableStickerCustom {
        paintBitmap.isFilterBitmap = true
        paintBitmap.colorFilter = ColorFilterGenerator.adjustHue(color)

        return this
    }

    fun setShadowPathShape(lstPath: ArrayList<String>) {
        if (rectFShadow == null) rectFShadow = RectF()
        if (shadowPath == null) shadowPath = Path()

        shadowPath!!.reset()
        for (path in lstPath) {
            if (path == "evenOdd") shadowPath!!.fillType = Path.FillType.EVEN_ODD
            else if (path.contains("#")) shadowPaint?.let { it.color = Color.parseColor(path) }
            else shadowPath!!.addPath(PathParser.createPathFromPathData(path))
        }
        shadowPath!!.computeBounds(rectFShadow!!, true)
        if (realBounds != null) {
            val maxScreen = realBounds!!.width().coerceAtLeast(realBounds!!.height())
            val max = rectFShadow!!.width().coerceAtLeast(rectFShadow!!.height())
            val scale = maxScreen / max
            val matrix = Matrix()
            matrix.preTranslate(realBounds!!.left, realBounds!!.top)
            matrix.preScale(scale, scale)
            shadowPath!!.transform(matrix)
            shadowPath!!.computeBounds(rectFShadow!!, true)
        }
    }

    fun setShadow(shadow: ShadowModel) {
        if (shadowPaint == null) shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        isShadow = true

        if (shadow.colorBlur == 0 && shadow.blur == 0f && shadow.xPos == 0f && shadow.yPos == 0f) {
            shadowPaint!!.setShadowLayer(shadow.blur, shadow.xPos, shadow.yPos, Color.TRANSPARENT)
            shadowPaint!!.color = Color.TRANSPARENT
        } else if (shadow.colorBlur != 0) {
            shadowPaint!!.setShadowLayer(shadow.blur, shadow.xPos, shadow.yPos, shadow.colorBlur)
            shadowPaint!!.color = shadow.colorBlur
        } else {
            shadowPaint!!.setShadowLayer(shadow.blur, shadow.xPos, shadow.yPos, Color.BLACK)
            shadowPaint!!.color = Color.BLACK
        }
    }

    fun replaceTattoo() {
        initTattoo()
    }

    fun replaceTattooPremium() {
        initTattooPremium()
    }

    private fun createDrawable() {
        val drawable = GradientDrawable()
        when (typeSticker) {
            Constant.TATTOO -> {
                drawable.setSize(
                    rectFTattoo!!.width().toInt() + 2 * distance,
                    rectFTattoo!!.height().toInt() + 2 * distance
                )
                drawable.setColor(Color.TRANSPARENT)
                drawable.setBounds(
                    distance,
                    distance,
                    rectFTattoo!!.right.toInt() - distance,
                    rectFTattoo!!.bottom.toInt() - distance
                )
            }
            Constant.TATTOO_PREMIUM -> {
                drawable.setSize(bitmap!!.width + distance, bitmap!!.height + distance)
                drawable.setColor(Color.TRANSPARENT)
                drawable.setBounds(
                    distance,
                    distance,
                    bitmap!!.width + distance,
                    bitmap!!.height + distance
                )
            }
        }
        setDrawable(drawable)
    }
}