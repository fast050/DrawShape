package com.example.customview.matrix

import android.view.MotionEvent
import kotlin.math.sqrt

object MotionEventExtensions {
    fun MotionEvent.distanceTo(x: Float, y: Float): Float {
        return pointsDistance(this.x, this.y, x, y)
    }

    private fun pointsDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val dx = x1 - x2
        val dy = y1 - y2
        return sqrt(dx * dx + dy * dy)
    }
}