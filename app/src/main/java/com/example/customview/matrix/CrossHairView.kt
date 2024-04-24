package com.example.customview.matrix

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.customview.R
import com.example.customview.dpToPx
import com.example.customview.matrix.MotionEventExtensions.distanceTo

class CrossHairView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var dotColor = 0
    private var crossHairColor = 0

    private var crossHairCircleRadius = 0f
    private var crossHairDotRadius = 0f
    private var crossHairHairLength = 0f

    private var centerCrossHairX = 0f
    private var centerCrossHairY = 0f
    private var isDragged = false
    private var lastMotionPositionX = 0f
    private var lastMotionPositionY = 0f

    init {
        crossHairCircleRadius = dpToPx(CROSSHAIR_CIRCLE_RADIUS_DP)
        crossHairDotRadius = dpToPx(CROSSHAIR_DOT_RADIUS_DP)
        crossHairHairLength = dpToPx(CROSSHAIR_HAIR_LENGTH_DP)
        paint.strokeWidth = dpToPx(CROSSHAIR_LINE_SIZE_DP)
        dotColor = ContextCompat.getColor(context, R.color.red)
        crossHairColor = ContextCompat.getColor(context, R.color.primary_variant)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null)
            return false

        return if (event.action == MotionEvent.ACTION_DOWN &&
            event.distanceTo(centerCrossHairX, centerCrossHairY) <= crossHairCircleRadius
        ) {
            isDragged = true
            lastMotionPositionX = event.x
            lastMotionPositionY = event.y
            true
        } else if (isDragged && event.action == MotionEvent.ACTION_MOVE) {
            val dx = event.x - lastMotionPositionX
            val dy = event.y - lastMotionPositionY

            centerCrossHairX += dx
            centerCrossHairY += dy

            lastMotionPositionX = event.x
            lastMotionPositionY = event.y

            invalidate()
            true
        } else {
            isDragged = false
            super.onTouchEvent(event)
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        centerCrossHairX = width / 2f
        centerCrossHairY = height / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCrossHairCenterDot(canvas)
        drawCrossHairCircle(canvas)
        drawCrossHairAngleLine(canvas)
    }

    private fun drawCrossHairAngleLine(canvas: Canvas) {
        canvas.rotate(-45f, centerCrossHairX, centerCrossHairY)
        canvas.drawLine(
            centerCrossHairX + crossHairCircleRadius - crossHairHairLength / 2,
            centerCrossHairY,
            centerCrossHairX + crossHairCircleRadius + crossHairHairLength / 2,
            centerCrossHairY,
            paint
        )

        canvas.rotate(-90f, centerCrossHairX, centerCrossHairY)
        canvas.drawLine(
            centerCrossHairX + crossHairCircleRadius - crossHairHairLength / 2,
            centerCrossHairY,
            centerCrossHairX + crossHairCircleRadius + crossHairHairLength / 2,
            centerCrossHairY,
            paint
        )

        canvas.rotate(-90f, centerCrossHairX, centerCrossHairY)
        canvas.drawLine(
            centerCrossHairX + crossHairCircleRadius - crossHairHairLength / 2,
            centerCrossHairY,
            centerCrossHairX + crossHairCircleRadius + crossHairHairLength / 2,
            centerCrossHairY,
            paint
        )

        canvas.rotate(-90f, centerCrossHairX, centerCrossHairY)
        canvas.drawLine(
            centerCrossHairX + crossHairCircleRadius - crossHairHairLength / 2,
            centerCrossHairY,
            centerCrossHairX + crossHairCircleRadius + crossHairHairLength / 2,
            centerCrossHairY,
            paint
        )
    }

    private fun drawCrossHairCircle(canvas: Canvas) {
        paint.color = crossHairColor
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(centerCrossHairX, centerCrossHairY, crossHairCircleRadius, paint)
    }

    private fun drawCrossHairCenterDot(canvas: Canvas) {
        paint.color = dotColor
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerCrossHairX, centerCrossHairY, crossHairDotRadius, paint)
    }

    companion object {
        private const val CROSSHAIR_CIRCLE_RADIUS_DP = 40f
        private const val CROSSHAIR_DOT_RADIUS_DP = 4f
        private const val CROSSHAIR_HAIR_LENGTH_DP = 20f
        private const val CROSSHAIR_LINE_SIZE_DP = 2f
    }

}