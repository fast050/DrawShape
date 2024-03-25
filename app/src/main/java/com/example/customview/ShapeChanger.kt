package com.example.customview

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import kotlin.math.cos
import kotlin.math.sin

class ShapeChanger(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {
    private val paint = Paint()
    private var screenWidth = 0f
    private var screenHeight = 0f
    private var sides = 3
    private val polygon = Path()
    private val polygonAnimatedPath = Path()
    private var valueAnimator:ValueAnimator? = null


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

       canvas?.drawPath(polygonAnimatedPath , paint)

    }

    fun setSides(sides: Int) {
        //this.sides = sides
        polygon.reset()
        endAnimatePolygon()

        createPathShape(
            sides,
            dpToPx(width / 6f)
        )
        startAnimatePolygon()
        invalidate()
    }


    private fun createPathShape(sides: Int, radius: Float): Path {
        val cx = screenWidth / 2
        val cy = screenHeight / 2
        val angle = 2 * Math.PI / sides

        polygon.moveTo(
            cx + radius * cos(0.0).toFloat(),
            cy + radius * sin(0.0).toFloat()
        )

        for (i in 1 until sides)
            polygon.lineTo(
                cx + radius * cos(angle * i).toFloat(),
                cy + radius * sin(angle * i).toFloat()
            )

        polygon.close()
        return polygon
    }

    private fun startAnimatePolygon() {
        valueAnimator = ValueAnimator.ofFloat(0f, 2f).apply {
            duration = 4000
            repeatCount = ValueAnimator.INFINITE

            addUpdateListener {
                updatePolygon(it.animatedValue as Float)
            }

            doOnEnd {
                it.cancel()
            }

            start()
        }

    }

    private fun endAnimatePolygon(){
        valueAnimator?.cancel()
    }

    private fun updatePolygon(factor: Float) {
        val pathMeasure = PathMeasure(polygon, false)
        polygonAnimatedPath.reset()

        if (factor <= 1){
            pathMeasure.getSegment(
                0f,
                factor * pathMeasure.length,
                polygonAnimatedPath,
                true)

            if (factor == 1f)
                polygonAnimatedPath.close()
        }else{

            val startFactor= factor - 1
            pathMeasure.getSegment(
                startFactor * pathMeasure.length,
                pathMeasure.length,
                polygonAnimatedPath,
                true)

        }


        invalidate()
    }

}