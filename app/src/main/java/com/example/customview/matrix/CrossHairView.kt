package com.example.customview.matrix

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.withSave
import com.example.customview.R
import com.example.customview.util.dpToPx
import com.example.customview.util.MotionEventExtensions.distanceTo

private const val TAG = "CrossHairView"
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

    private var crossHairXFraction = 0.5f
    private var crossHairYFraction = 0.5f


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

            crossHairXFraction = centerCrossHairX / width
            crossHairYFraction = centerCrossHairY / height

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

        centerCrossHairX = width * crossHairXFraction
        centerCrossHairY = height * crossHairYFraction
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCrossHairCenterDot(canvas)
        drawCrossHairCircle(canvas)
        drawCrossHairAngleLine(canvas)
    }

    private fun drawCrossHairAngleLine(canvas: Canvas) {
        canvas.withSave{
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

    override fun onSaveInstanceState(): Parcelable {
        val superSavedState = super.onSaveInstanceState()
        return MySavedState(superSavedState, crossHairXFraction, crossHairYFraction)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        Log.d(TAG, "onRestoreInstanceState: ")
        if (state is MySavedState) {
            super.onRestoreInstanceState(state.superSavedState)
            crossHairXFraction = state.crosshairXFraction
            crossHairYFraction = state.crosshairYFraction

        } else {
            super.onRestoreInstanceState(state)
        }
    }

    companion object {
        private const val CROSSHAIR_CIRCLE_RADIUS_DP = 40f
        private const val CROSSHAIR_DOT_RADIUS_DP = 4f
        private const val CROSSHAIR_HAIR_LENGTH_DP = 20f
        private const val CROSSHAIR_LINE_SIZE_DP = 2f
    }

    private class MySavedState: BaseSavedState {

        val superSavedState: Parcelable?
        val crosshairXFraction: Float
        val crosshairYFraction: Float

        constructor(
            superSavedState: Parcelable?,
            crosshairXFraction: Float,
            crosshairYFraction: Float,
        ): super(superSavedState) {
            this.superSavedState = superSavedState
            this.crosshairXFraction = crosshairXFraction
            this.crosshairYFraction = crosshairYFraction
        }

        constructor(parcel: Parcel) : super(parcel) {
            this.superSavedState = parcel.readParcelable(null)
            this.crosshairXFraction = parcel.readFloat()
            this.crosshairYFraction = parcel.readFloat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeParcelable(superSavedState, flags)
            out.writeFloat(crosshairXFraction)
            out.writeFloat(crosshairYFraction)
        }

        companion object CREATOR : Parcelable.Creator<MySavedState> {
            override fun createFromParcel(parcel: Parcel): MySavedState {
                return MySavedState(parcel)
            }

            override fun newArray(size: Int): Array<MySavedState?> {
                return arrayOfNulls(size)
            }
        }
    }

}