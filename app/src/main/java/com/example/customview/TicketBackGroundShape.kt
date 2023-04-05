package com.example.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


class TicketBackGroundShape(context: Context,attrs:AttributeSet?) :View(context,attrs) {
    private val paint = Paint()
    private var cornerRadius = 50f
    private var innerRadius = 100f

    init {
        paint.color = Color.RED
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {

            val originX = 100f
            val originY = 100f
            val width = width.toFloat() //335f
            val height =  height.toFloat() //166f
            val radios =  12f.Scale()
            val dashLinePointShiftFromOriginX = 103f.Scale()
            val dashLinePositionFromBottomShape = 63f.Scale()

            //float left, float top, float right, float bottom
            val rectFRight = RectF((originX+width) - radios , dashLinePointShiftFromOriginX-radios , (originX+width) + radios , dashLinePointShiftFromOriginX+radios)
            val rectFLeft  = RectF((originX),-(dashLinePositionFromBottomShape+radios) ,originX+radios,-(dashLinePositionFromBottomShape-radios))
            val path = Path().apply {
                moveTo(originX, originY)
                lineTo(originX+width, originY)

                // arch
                lineTo(originX+width, dashLinePointShiftFromOriginX-radios)
                arcTo(rectFRight , 270f , -180f , false)
                lineTo(width,dashLinePointShiftFromOriginX+radios)

                lineTo(width , height)
                lineTo(originX , height)

                //arch
                lineTo(originX, -(radios))
                arcTo(rectFLeft , 270f , 180f , false)
                lineTo(originX,-(dashLinePositionFromBottomShape+radios))

                //lineTo(-width ,-height)
               // lineTo(originX+width , originY+height)

            }

            canvas.drawPath(path , paint)


        }


    }
    fun Float.Scale():Float{
        val heightByDesign = 166f
        return height*this/heightByDesign
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