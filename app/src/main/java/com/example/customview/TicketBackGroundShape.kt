package com.example.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView


class TicketBackGroundShape(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint()
    private var cornerRadius = 50f
    private var innerRadius = 6f
    private var scaleFactor = 0f

    init {
        paint.color = Color.GRAY
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {

            val originX = 0f.Scale() + scaleFactor.Scale()
            val originY = 0f.Scale() + scaleFactor.Scale()
            val width = width.toFloat() - scaleFactor.Scale() //335f
            val height = height.toFloat() - scaleFactor.Scale() //166f
            val radios = innerRadius.Scale()
            val dashLinePointShiftFromOriginX = 103f.Scale()
            val dashLinePositionFromBottomShape = 63f.Scale()

            val path = Path().apply {
                moveTo(originX, originY)
                lineTo(width, originY)

                // arch
                lineTo(width, dashLinePointShiftFromOriginX - radios)
                arcTo(
                    width - radios,
                    dashLinePointShiftFromOriginX - radios,
                    width + radios,
                    dashLinePointShiftFromOriginX + radios,
                    270f,
                    -180f,
                    false
                )

                lineTo(width, height)
                lineTo(originX, height)

                //arch
                lineTo(originX, dashLinePositionFromBottomShape + radios)
                arcTo(
                    originX - radios,
                    dashLinePointShiftFromOriginX - radios,
                    originX + radios,
                    dashLinePointShiftFromOriginX + radios,
                    90f,
                    -180f,
                    false
                )
                close()
            }

            canvas.drawPath(path, paint)

            val paints = Paint().apply {
                color = Color.BLACK
                strokeWidth = 2f.Scale()
                style = Paint.Style.STROKE
                pathEffect = DashPathEffect(floatArrayOf(30f, 20f), 15f)
            }

            canvas.drawLine(
                originX + radios,
                dashLinePointShiftFromOriginX,
                width - radios,
                dashLinePointShiftFromOriginX,
                paints
            )

        }


    }

    fun Float.Scale(): Float {
        val heightByDesign = 166f
        return height * this / heightByDesign
    }

    private fun drawArchWithRectangle(canvas: Canvas) {
        val path = Path().apply {
            // Draws triangle
            /*moveTo(100f, 100f)
                lineTo(300f, 300f)
                lineTo(100f, 300f)
                close()*/

            // Draws bridge
            moveTo(100f, 100f)
            lineTo(600f, 100f)
            lineTo(600f, 400f)
            lineTo(600f, 700f)
            lineTo(400f, 700f)
            // bottom is 900 because the system draws the arc inside an
            // imaginary rectangle
            arcTo(100f, 500f, 400f, 900f, 0f, -180f, false)
            lineTo(100f, 700f)
            close()
        }

        canvas.drawPath(path, paint)
    }


}