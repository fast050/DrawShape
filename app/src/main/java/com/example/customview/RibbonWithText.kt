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
    private var ribbonTextSize = 0f



    init {

        val attributeSet = context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.CustomRibbon , 0 , 0
        )

        try {
            ribbonText = attributeSet.getString(R.styleable.CustomRibbon_ribbonText) ?: ""
            backgroundColor = attributeSet.getColor(R.styleable.CustomRibbon_backgroundColor , Color.parseColor("#FFD600"))
            ribbonTextSize = attributeSet.getDimension(R.styleable.CustomRibbon_ribbonTextSize , 0f)
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
            textSize = if (ribbonTextSize == 0f) textRatio() else ribbonTextSize
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

        val textWidth = textPaint.measureText(ribbonText)

        // Calculate the horizontal offset to center the text to draw the text in middle of the path
        val hOffset = (textPathLength() - textWidth) / 2

        canvas.drawPath(backgroundShapePath, backgroundPaint)
        canvas.drawTextOnPath(ribbonText,textPath ,
            hOffset.dp().px(),
            0f ,
            textPaint)

    }

    private fun textRatio():Float {
        val lengthOfDiagonal = textPathLength()
        return  lengthOfDiagonal /(8.dp().px())
    }

    //to get the length of the diagonal text path
    private fun textPathLength(): Float {
        val widthOfTriangle = widthOfScreen - (widthOfScreen/6)
        val heightOfTriangle = heightOfScreen - (heightOfScreen/6)
        val lengthOfDiagonal =
            //square
            if (widthOfTriangle == heightOfTriangle) {
                (widthOfTriangle * sqrt(2.0)).toFloat()
            }
            //rectangle
            else {
                sqrt((widthOfTriangle * widthOfTriangle) + (heightOfTriangle * heightOfTriangle))
            }
        return lengthOfDiagonal
    }


    private fun Float.dp() : Float = this / Resources.getSystem().displayMetrics.density
    private fun Float.px() : Float = this * Resources.getSystem().displayMetrics.density
    private fun Int.dp() : Float = this / Resources.getSystem().displayMetrics.density
}