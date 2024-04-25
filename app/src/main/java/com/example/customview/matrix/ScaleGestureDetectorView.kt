package com.example.customview.matrix

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import androidx.core.content.ContextCompat
import com.example.customview.R
import com.example.customview.util.dpToPx

class ScaleGestureDetectorView(context: Context , attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var startX = 0f
    private var startY = 0f
    private var scaleFactor = 0f
    private var scaleText = "0.000"
    private val bound = Rect()




    private val scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(context
        , object : OnScaleGestureListener{
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor
                scaleText = scaleFactor.toString()

                invalidate()
                return true
            }

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                scaleFactor = 1f
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {
                scaleFactor = 1f
            }
        })

    init {
        paint.textSize = dpToPx(20f)
        paint.color = ContextCompat.getColor(context, R.color.primary_variant)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null)
            return super.onTouchEvent(null)

        return scaleGestureDetector.onTouchEvent(event) || super.onTouchEvent(event)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        paint.getTextBounds(scaleText , 0 , scaleText.length , bound)

        startX = width / 2f  - bound.width()/2
        startY = height / 2f - bound.height()/2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawText(
            "%.3f".format(scaleFactor),
            startX,
            startY,
            paint
        )
    }

    companion object {
        private const val CROSSHAIR_CIRCLE_RADIUS_DP = 40f
    }
}