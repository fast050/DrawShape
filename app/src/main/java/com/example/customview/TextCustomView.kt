package com.example.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.customview.util.dpToPx

class TextCustomView(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var text = "2"
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


        setText(text , dpToPx(50f))
    }

    init {
        textPaint.apply {
            textAlign = Paint.Align.CENTER
            color = Color.BLACK
            typeface =  ResourcesCompat.getFont(context, R.font.helvetica_regular)
        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val x = width / 2f
        val y = (height / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2)

        canvas?.drawText(text, x, y, textPaint)
    }


    private fun calculatedTextSize(text: String, maxWidth: Float, maxHeight: Float): Float {
        var size = dpToPx(1f)
        val bound = Rect()

        if (text.isBlank())
            return dpToPx(1f)


        while (true) {
            textPaint.textSize = size
            textPaint.getTextBounds(text, 0, text.length, bound)
            if (bound.width() > maxWidth || bound.height() > maxHeight)
                return size - 1

            size += dpToPx(1f)
        }

    }

    fun setText(text: String, textSize: Float? = null) {
        this.text = text
        textPaint.textSize = textSize ?:
                             calculatedTextSize(text, width.toFloat(), height.toFloat())
        invalidate()
    }

}