package com.tattoo.tattoomaker.on.myphoto.viewcustom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.PathParser
import androidx.core.graphics.toColorInt
import com.tattoo.tattoomaker.on.myphoto.utils.UtilsBitmap

class CustomDrawPathData : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var path: Path? = null
    private var paint: Paint? = null
    private var rectF: RectF? = null

    init {
        path = Path()
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
        rectF = RectF()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        path!!.computeBounds(rectF!!, true)
        val x = (height * 0.5f * rectF!!.width() / rectF!!.height()).toInt()
        val y = (width * 0.5f * rectF!!.height() / rectF!!.width()).toInt()
        if (rectF!!.width() >= rectF!!.height())
            UtilsBitmap.drawIconWithPath(canvas, path!!, paint, width / 2f, width / 4, (height - y) / 2)
        else
            UtilsBitmap.drawIconWithPath(canvas, path!!, paint, width / 2f, (width - x) / 2, height  / 4)
    }

    fun setDataPath(lstPath: List<String>) {
        path!!.reset()
        for (pathData in lstPath) {
            if (pathData == "evenOdd") path!!.fillType = Path.FillType.EVEN_ODD
            else if (pathData.contains("#"))
                paint!!.color = pathData.toColorInt()
            else path!!.addPath(PathParser.createPathFromPathData(pathData))
        }
        paint!!.color = Color.WHITE

        invalidate()
    }
}