package com.example.customview.matrix

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.customview.dpToPx

class ArrowShapeView(context: Context?, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var startArrowPathPointX: Float = 0f
    private var startArrowPathPointY: Float = 0f
    private val arrowPath = Path()
    private val referenceArrowPath = Path()
    private val arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val transformationMatrix = Matrix()
    var startArrowX = 0f
        set(value) {
            field = value
            invalidate()
        }
    var startArrowY = 0f
        set(value) {
            field = value
            invalidate()
        }

    var rotationOfShape = 0f
        set(value) {
            field = value
            invalidate()
        }

    init {
        arrowPaint.apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = dpToPx(ARROW_STROKE_SIZE)
            strokeJoin = Paint.Join.ROUND
        }

        textPaint.apply {
            color = Color.BLACK
            style = Paint.Style.FILL
            textSize = dpToPx(ARROW_STROKE_SIZE)
            isAntiAlias = true
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


        createArrowPath(width.toFloat(), height.toFloat())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        arrowPath.reset()
        arrowPath.set(referenceArrowPath)

        canvas?.drawText(
            "This just for testing" ,
            startArrowPathPointX - dpToPx(20f) ,
            startArrowPathPointY - dpToPx(20f) ,
            textPaint)

        transformationMatrix.reset()
        transformationMatrix.postRotate(
                rotationOfShape,
        startArrowPathPointX ,
        startArrowPathPointY
        )
        transformationMatrix.postTranslate(startArrowX, startArrowY)

        arrowPath.transform(transformationMatrix)

        canvas?.drawPath(arrowPath, arrowPaint)
    }

    private fun createArrowPath(width: Float, height: Float) {
        startArrowPathPointX = width / 3f
        startArrowPathPointY = height / 2f
        val endX = width * 2 / 3f
        val endY = height / 2f
        val headSize = dpToPx(ARROW_HEAD_SIZE)
        referenceArrowPath.apply {
            moveTo(startArrowPathPointX, startArrowPathPointY)
            lineTo(endX, endY)
            lineTo(endX - headSize, endY - headSize)
            moveTo(endX, endY)
            lineTo(endX - headSize, endY + headSize)
        }
    }

    companion object {
        const val ARROW_HEAD_SIZE = 10f
        const val ARROW_STROKE_SIZE = 5f
    }

}