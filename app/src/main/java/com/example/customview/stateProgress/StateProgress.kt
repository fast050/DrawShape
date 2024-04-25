package com.example.customview.stateProgress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.customview.R
import com.example.customview.util.dpToPx
import kotlin.math.min

class StateProgress(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textBoundsRect = Rect()
    private var colorActive = 0
    private var colorInactive = 0

    private val states = mutableListOf<State>()
    private var currentState: State? = null


    private val statesTest = listOf(
        State("state 1"),
        State("state 2"),
        State("state 3 - longest name"),
        State("state 4"),
    )

    init {
        setBackgroundColor(ContextCompat.getColor(context, R.color.gray_dark))
        colorActive = ContextCompat.getColor(context, R.color.green)
        colorInactive = ContextCompat.getColor(context, R.color.semi_transparent)

        paint.apply {
            color = colorInactive
            style = Paint.Style.FILL
            textSize = dpToPx(TEXT_SIZE_DP)
        }
    }

    fun bindStates(states: List<State>) {
        this.states.clear()
        this.states.addAll(states)
        requestLayout()
    }

    fun bindCurrentState(state: State?) {
        this.currentState = state
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desireWidth = MeasureSpec.getSize(widthMeasureSpec)
        val desireHeight = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val selfWidth = dpToPx(MARGIN_HORIZONTAL_DP) +
                        dpToPx(CIRCLE_RADIUS_DP) +
                        dpToPx(CIRCLE_TO_TEXT_SPACING_HORIZONTAL_DP) +
                        getLongestStateNameLength()

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> desireWidth
            MeasureSpec.AT_MOST-> min(desireWidth, selfWidth.toInt())
            else -> getLongestStateNameLength()
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY ->  desireHeight
            MeasureSpec.AT_MOST -> min(desireHeight , 500)
            else -> 500
        }

        setMeasuredDimension(width , height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawTextWithBoltPoint(states)
        canvas.drawTextWithBoltPoint(statesTest)

    }

    private fun Canvas.drawTextWithBoltPoint(states: List<State>) {

        var stateLineHeight = dpToPx(MARGIN_VERTICAL_DP)
        val indexState = states.indexOf(currentState)

        states.forEachIndexed { index, it ->

            if (index <= indexState)
                paint.color = colorActive
            else
                paint.color = colorInactive

            drawCircle(
                dpToPx(MARGIN_HORIZONTAL_DP),
                stateLineHeight,
                dpToPx(CIRCLE_RADIUS_DP),
                paint
            )
            drawText(
                it.name,
                dpToPx(MARGIN_HORIZONTAL_DP) + dpToPx(CIRCLE_TO_TEXT_SPACING_HORIZONTAL_DP),
                stateLineHeight - ((paint.ascent() + paint.descent()) / 2),
                paint
            )

            stateLineHeight += dpToPx(STATES_SPACING_VERTICAL_DP)
        }
    }

    private fun getLongestStateNameLength(): Int {
        var longestStateNameLength = 0
        states.forEach { state ->
            paint.getTextBounds(state.name, 0, state.name.length, textBoundsRect)
            if (textBoundsRect.width() > longestStateNameLength) {
                longestStateNameLength = textBoundsRect.width()
            }
        }
        return longestStateNameLength
    }

    companion object {
        private const val MARGIN_HORIZONTAL_DP = 25f
        private const val MARGIN_VERTICAL_DP = 25f
        private const val STATES_SPACING_VERTICAL_DP = 40f
        private const val CIRCLE_RADIUS_DP = 8f
        private const val CIRCLE_TO_TEXT_SPACING_HORIZONTAL_DP = 20f
        private const val TEXT_SIZE_DP = 20f
    }
}