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
class CustomRibbon (context: Context, attributeSet: AttributeSet?): View(context , attributeSet){

    private var widthOfScreen = 0f
    private var heightOfScreen = 0f
    private var originX = 0f
    private var originY = 0f
    private var ribbonText = ""
    private var backgroundColor = 0


    init {

        val attributeSet = context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.CustomRibbon , 0 , 0
        )

        try {
            ribbonText = attributeSet.getString(R.styleable.CustomRibbon_ribbonText) ?: "Popular"
            backgroundColor = attributeSet.getColor(R.styleable.CustomRibbon_backgroundColor , Color.parseColor("#FFD600"))
        } finally {
            attributeSet.recycle()
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        widthOfScreen = width.toFloat()
        heightOfScreen = height.toFloat()

        val isRtl = layoutDirection == LAYOUT_DIRECTION_RTL

        //define the shape of background
        val backgroundShapePath = Path().apply {

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
        val backgroundPaint = Paint().apply {
            color = backgroundColor
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
                moveTo(originX , heightOfScreen * 5/6)
                lineTo(widthOfScreen * 5/6 , originY)
            }
            else{
                moveTo(widthOfScreen /6 ,originY)
                lineTo(widthOfScreen , heightOfScreen * 5 / 6 )
            }

            close()
        }

        val text = ribbonText
        val textWidth = textPaint.measureText(text).dp().px()
        // Calculate the horizontal offset to center the text
        // Adjust this calculation based on your specific requirements
        val hOffset = if (isRtl) {
            (textPathLength() / 2) - textWidth  // Adjust for RTL if needed
        } else {
            (textPathLength() / 2) - textWidth * 2/3
        }

        canvas.drawPath(backgroundShapePath, backgroundPaint)
        canvas.drawTextOnPath(ribbonText,textPath ,
            hOffset ,
            0f ,
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


    private fun Float.dp() : Float = this / Resources.getSystem().displayMetrics.density
    private fun Float.px() : Float = this * Resources.getSystem().displayMetrics.density
    private fun Int.dp() : Float = this / Resources.getSystem().displayMetrics.density
    fun Int.px() : Float = this * Resources.getSystem().displayMetrics.density

}