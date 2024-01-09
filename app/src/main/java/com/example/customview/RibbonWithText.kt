package com.example.customview

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.sqrt

/**
 *  support both direction for language , arabic and english , RTL , LTR
 *
 */
class CustomRibbon (context: Context?, attributeSet: AttributeSet?): View(context , attributeSet){

    var widthOfScreen = 0f
    var heightOfScreen = 0f
    var originX = 0f
    var originY = 0f


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        widthOfScreen = width.toFloat()
        heightOfScreen = height.toFloat()

        val isRtl = layoutDirection == LAYOUT_DIRECTION_RTL

        //define the shape of background
        val path = Path().apply {

            if (isRtl){
                moveTo(widthOfScreen , originY)
                lineTo(widthOfScreen / 2 , originY)
                lineTo(  originX , heightOfScreen/2)
                lineTo(originX , heightOfScreen)
            }else{
                moveTo(originX , originY)
                lineTo(widthOfScreen / 2 , originY)
                lineTo(widthOfScreen , heightOfScreen/2)
                lineTo(widthOfScreen , heightOfScreen)
            }

            close()
        }

        //define the paint of the shape of the background
        val paint = Paint().apply {
            color = Color.parseColor("#FFD600")
            style = Paint.Style.FILL
        }

        val textPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL
            typeface = Typeface.DEFAULT_BOLD
            textSize = textRatio()
        }

        //define the path of where the text should be draw
        val textPath = Path().apply {
            if(isRtl){
                moveTo(originX , heightOfScreen)
                lineTo(widthOfScreen , originY)
            }
            else{
                moveTo(widthOfScreen /6 ,originY)
                lineTo(widthOfScreen , heightOfScreen * 5 / 6 )
            }

            close()
        }

        val text = context.getString(R.string.popular_label)
        val textWidth = textPaint.measureText(text).dp().px()
        // Calculate the horizontal offset to center the text
        // Adjust this calculation based on your specific requirements
        val hOffset = if (isRtl) {
            (textPathLength() / 2)  - textWidth  // Adjust for RTL if needed
        } else {
            (textPathLength() / 2) - textWidth*2/3
        }

        canvas.drawPath(path, paint)
        canvas.drawTextOnPath(context.getString(R.string.popular_label),textPath ,
            hOffset.dp().px() ,
            - 0f ,
            textPaint)

    }

    private fun textRatio():Float {
        val lengthOfDiagonal = textPathLength()
        return  lengthOfDiagonal /(10.dp().px())
    }

    //to get the diagonal path length
    private fun textPathLength(): Float {
        val lengthOfDiagonal =
            //square
            if (widthOfScreen == heightOfScreen) {
                (widthOfScreen * sqrt(2.0)).toFloat()
            }
            //rectangle
            else {
                sqrt((widthOfScreen * widthOfScreen) + (heightOfScreen * heightOfScreen))
            }
        return lengthOfDiagonal
    }

    //to get length between the two line the shape (diagonal , the line vertical up diagonal)
    private fun pathLengthVertical() :Float {
        val path = Path().apply {
            moveTo(widthOfScreen , originY)
            lineTo(widthOfScreen / 2 , originY)
        }

        val pathMeasure = PathMeasure(path, false)
        return pathMeasure.length.dp().px()
    }

    private fun getTextHeight(textPaint:Paint , text:String) :Float {
        val bounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, bounds)
        return bounds.height().toFloat()
    }



    fun Float.dp() : Float = this / Resources.getSystem().displayMetrics.density
    fun Float.px() : Float = this * Resources.getSystem().displayMetrics.density
    fun Int.dp() : Float = this / Resources.getSystem().displayMetrics.density
    fun Int.px() : Float = this * Resources.getSystem().displayMetrics.density

}