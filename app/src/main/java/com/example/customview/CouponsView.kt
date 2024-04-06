package com.example.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class CouponsView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var bottomCoupons: Float = 0f
    private var topCoupons: Float = 0f
    private var leftCoupons: Float = 0f
    private var rightCoupons: Float = 0f
    private var totalCoupons = 0
    private var usedCoupons = 0
    private var couponsShapePath = Path()
    private val couponsPaint = Paint()
    private var linePosition = 0f
    private var couponsPaintText = Paint()

    fun setCoupons(totalCoupons: Int, usedCoupons: Int) {
        this.totalCoupons = totalCoupons
        this.usedCoupons = usedCoupons
        if (width > 0 && height > 0) {
            invalidate()
        }
    }

    init {
        couponsPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = dpToPx(LINE_SIZE_DP)
        }

        couponsPaintText.apply {
            style = Paint.Style.FILL
            textSize = dpToPx(12f)
        }

        usedCoupons = 5
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        totalCoupons = 10
        couponsShapePath = createCouponsContainerPath(width.toFloat())

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawPath(couponsShapePath, couponsPaint)
        drawLinesOfCoupons(canvas, width.toFloat())
        drawTextInsideCoupons(canvas, width.toFloat())

    }

    private fun createCouponsContainerPath(screenWidth: Float): Path {

        topCoupons = LINE_SIZE_DP
        leftCoupons = LINE_SIZE_DP
        rightCoupons = screenWidth - LINE_SIZE_DP
        bottomCoupons = height - LINE_SIZE_DP

        val couponsPath = Path()
        couponsPath.addRoundRect(
            RectF(
                leftCoupons,
                topCoupons,
                rightCoupons,
                bottomCoupons
            ),
            floatArrayOf(
                0f, 0f,
                0f, 0f,
                dpToPx(CORNER_RADIUS_DP), dpToPx(CORNER_RADIUS_DP),
                dpToPx(CORNER_RADIUS_DP), dpToPx(CORNER_RADIUS_DP)
            ), Path.Direction.CW
        )

        return couponsPath
    }

    private fun drawLinesOfCoupons(canvas: Canvas, screenWidth: Float) {
        for (i in 1 until totalCoupons) {
            linePosition = screenWidth * i / totalCoupons
            canvas.drawLine(linePosition, topCoupons, linePosition, bottomCoupons, couponsPaint)
        }
    }

    private fun drawTextInsideCoupons(canvas: Canvas, screenWidth: Float) {
        val wordBound = Rect()
        val y = (topCoupons + bottomCoupons) / 2 -
                (couponsPaintText.descent() + couponsPaintText.ascent()) / 2

        for (i in 1 until totalCoupons * 2) {
            if (i % 2 != 0) {
                val number = i / 2 + 1
                val numberText = number.toString()
                couponsPaintText.getTextBounds(numberText , 0 , numberText.length , wordBound)
                val x = screenWidth * i / (totalCoupons * 2).toFloat() -
                        ((wordBound.right + wordBound.left)/2)

                if (number <= usedCoupons) {
                    couponsPaintText.color = Color.GRAY
                    canvas.drawText(numberText, x, y, couponsPaintText)
                } else {
                    couponsPaintText.color = Color.BLACK
                    canvas.drawText(numberText, x, y, couponsPaintText)
                }

            }
        }
    }

    companion object {
        private const val LINE_SIZE_DP = 2f
        private const val CELL_MIN_PADDING_DP = 5f
        private const val CORNER_RADIUS_DP = 20f
    }

}