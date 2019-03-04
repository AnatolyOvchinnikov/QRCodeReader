package com.app.qrcodeapplication.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class OverlayWithRect(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var radius: Float = 0f
    var overlayColor: Int = 0
    var drawRect: RectF = RectF()
        get() = field
        set(value) {
            field = value
            postInvalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!drawRect.isEmpty) {
            paint.xfermode = null
            paint.color = overlayColor
            paint.style = Paint.Style.FILL
            canvas.drawPaint(paint)

            paint.xfermode = xfermode
            canvas.drawRoundRect(drawRect, radius, radius, paint)
        }
    }
}