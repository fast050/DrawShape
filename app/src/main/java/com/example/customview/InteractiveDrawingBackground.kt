package com.example.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class InteractiveDrawingBackground(
    context: Context,
    attributes: AttributeSet?,
) :
    View(context, attributes) {

    private var cy: Float = 100f
    private var cx: Float = 100f
    var currentX: Float = 100f
    var currentY: Float = 100f
    var point1x = 0f
    var point1y = 0f
    var point2x = 0f
    var point2y = 0f
    private var counter = 0
    private val paint = Paint()

    init {
        paint.apply {
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeWidth = 40f
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //canvas?.drawCircle(currentX,currentY , 100f , paint)
        val paintLine = Paint().apply {
            color = Color.RED
            strokeWidth = 20f
        }

        Log.d("TAG", "onDraw :  $counter")

          if (counter==2){
              canvas?.drawLine(point1x , point1y , point2x , point2y ,paintLine)
              counter=0
          }

        /*canvas?.drawRect(
            cx,
            cy,
            cx + 100f,
            cy + 100f,
            paint
        )*/
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        currentX = event?.x ?: 0f
        currentY = event?.y ?: 0f
        counter++
        if (counter == 1) {
            point1x = event?.x ?: 0f
            point1y = event?.y ?: 0f
        } else if (counter >= 2) {
            point2x = event?.x ?: 0f
            point2y = event?.y ?: 0f
        }

        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {

                cx = event.x
                cy = event.y

                /*val dx = (x - cx).pow(2)
                val dy = (y - cy).pow(2)*/

                /*if (dx + dy < (radius).pow(2)) {
                    cx = x
                    cy = y
                    // circle touched
                    postInvalidate()

                    return true
                }*/

                postInvalidate()
            }
        }


        invalidate();
        return super.onTouchEvent(event)
    }
}