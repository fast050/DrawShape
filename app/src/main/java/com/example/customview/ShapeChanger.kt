package com.example.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class ShapeChanger(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {
    private val paint = Paint()
    private var screenWidth = 0f
    private var screenHeight = 0f
    private var sides = 2


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        screenHeight = h.toFloat()
        screenWidth = w.toFloat()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.apply {
            color = Color.BLACK
            strokeWidth = dpToPx(5f)
            style = Paint.Style.STROKE
        }

        canvas?.drawPath(
            createPathShape(sides, dpToPx(width/6f)),
            paint
        )

    }

    fun setSides(sides: Int){
        this.sides = sides
        invalidate()
    }


    private fun createPathShape(sides: Int, radius: Float): Path {
        val cx = screenWidth / 2
        val cy = screenHeight / 2
        val path = Path()
        val angle = 2 * Math.PI / sides

        path.moveTo(
            cx + radius * Math.cos(0.0).toFloat(),
            cy + radius * Math.sin(0.0).toFloat()
        )

        for (i in 1 until sides)
            path.lineTo(
                cx + radius * cos(angle * i).toFloat(),
                cy + radius * sin(angle * i).toFloat()
            )

        path.close()
        return path
    }

}