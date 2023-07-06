package com.tattoo.tattoomaker.on.myphoto.viewcustom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.tattoo.tattoomaker.on.myphoto.R
import com.remi.textonphoto.writeonphoto.addtext.customview.OnSeekbarResult

class CustomSeekbarTwoWay(context: Context) : View(context) {

    companion object {
        var w = 0F
    }

    private var paint: Paint
    private var paintProgress: Paint
    private var progress = 0
    private var max = 0
    private var value = 0
    private var sizeThumb = 0f
    private var sizeBg = 0f
    private var sizePos = 0f

    var onSeekbarResult: OnSeekbarResult? = null

    init {
        w = resources.displayMetrics.widthPixels / 100F
        sizeThumb = 4.44f * w
        sizeBg =  0.833f * w
        sizePos = 0.833f * w
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.FILL
        }
        paintProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.apply {
            clearShadowLayer()
            color = ContextCompat.getColor(context, R.color.gray)
            strokeWidth = sizeBg
        }
        canvas.drawLine(sizeThumb / 2, height / 2f, width - sizeThumb / 2, height / 2f, paint)

        paintProgress.apply {
            color = ContextCompat.getColor(context, R.color.white)
            strokeWidth = sizePos
        }
        val p = (width - sizeThumb) * progress / max + sizeThumb / 2
        canvas.drawLine(width / 2f, height / 2f, p, height / 2f, paintProgress)

        paint.apply {
            color = ContextCompat.getColor(context, R.color.white)
            setShadowLayer(sizeThumb / 8, 0f, 0f, Color.WHITE)
        }
        canvas.drawCircle(p, height / 2f, sizeThumb / 2, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        when (event.action) {
            MotionEvent.ACTION_DOWN -> onSeekbarResult?.let { it.onDown(this) }
            MotionEvent.ACTION_MOVE -> {
                progress = ((x - sizeThumb / 2) * max / (width - sizeThumb)).toInt()

                if (progress < 0) progress = 0
                else if (progress > max) progress = max
                invalidate()

                value = if (progress > max / 2) progress - max / 2
                else -(max / 2 - progress)
                onSeekbarResult?.let { it.onMove(this, value) }
            }
            MotionEvent.ACTION_UP -> onSeekbarResult?.let { it.onUp(this, value)
            }
        }
        return true
    }

    fun getProgress(): Int{
        return progress
    }

    fun setProgress(progress: Int) {
        if (progress == 0) this.progress = max / 2
        else this.progress = max / 2 + progress
        invalidate()
    }

    fun setMax(max: Int) {
        this.max = max
        progress = max / 2
        invalidate()
    }
}