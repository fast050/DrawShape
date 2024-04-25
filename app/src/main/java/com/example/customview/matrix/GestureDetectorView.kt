package com.example.customview.matrix

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.customview.R
import com.example.customview.dpToPx
import com.example.customview.matrix.MotionEventExtensions.distanceTo

private const val TAG = "GestureDetectorView"

class GestureDetectorView(
    context: Context,
    attributeSet: AttributeSet?
) : View(context, attributeSet) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var crossHairCircleRadius = 0f

    private var centerCrossHairX = 0f
    private var centerCrossHairY = 0f
    private var isDragged = false

    private val onGestureListener = object : GestureDetector.OnGestureListener {
        override fun onDown(e: MotionEvent): Boolean {

            isDragged =
                if (e.distanceTo(centerCrossHairX, centerCrossHairY) <= crossHairCircleRadius) {
                    true
                } else
                    false

            return true
        }

        override fun onShowPress(e: MotionEvent) {
            Log.d(TAG, "onShowPress: ")
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            Log.d(TAG, "onSingleTapUp: ")
            return true
        }

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {

            return if (isDragged) {
                centerCrossHairX -= distanceX
                centerCrossHairY -= distanceY

                invalidate()

                true
            } else
                false
        }

        override fun onLongPress(e: MotionEvent) {
            Log.d(TAG, "onLongPress: ")
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            Log.d(TAG, "onFling: ")
            return true
        }

    }

    private val doubleTapListener = object : GestureDetector.OnDoubleTapListener {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            Log.d(TAG, "onSingleTapConfirmed  even:$e")
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            Log.d(TAG, "onDoubleTap  even:$e")
            return true
        }

        override fun onDoubleTapEvent(e: MotionEvent): Boolean {
            Log.d(TAG, "onDoubleTapEvent  even:$e")
            return true
        }
    }

    private val gestureDetector: GestureDetector =
        GestureDetector(context, onGestureListener).apply {
            setIsLongpressEnabled(false)
            setOnDoubleTapListener(doubleTapListener)
        }


    init {
        crossHairCircleRadius = dpToPx(CROSSHAIR_CIRCLE_RADIUS_DP)

        paint.textSize = dpToPx(20f)
        paint.color = ContextCompat.getColor(context, R.color.primary_variant)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null)
            return super.onTouchEvent(null)

        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        centerCrossHairX = width / 2f
        centerCrossHairY = height / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(
            centerCrossHairX,
            centerCrossHairY,
            crossHairCircleRadius,
            paint
        )
    }

    companion object {
        private const val CROSSHAIR_CIRCLE_RADIUS_DP = 40f
    }
}