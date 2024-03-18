package com.example.customview

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnRepeat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RectangleAnimatedView(context: Context, attrs: AttributeSet?) : View(context , attrs){
    private var radius = 0.0f
    private var angle = 0f

    private val job = Job()
    private val customCoroutineScope = CoroutineScope(Dispatchers.Main+job)

    private val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.CYAN
    }

    init {
        animateView()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val leftTopX = width/4f
        val leftTopY = height/4f
        val rightBotX = width*3/4f
        val rightBotY = height*3/4f

        canvas.rotate(angle, width*3/4f - width/4f, height*3/4f - height/4f);


        canvas.drawRoundRect(
            leftTopX, leftTopY, rightBotX, rightBotY, radius, radius, backgroundPaint
        )
    }

    private fun animateView(){
        val propertyRadius = PropertyValuesHolder.ofFloat(PropertyAnimated.Radius.value, 0f, 100f)
        val propertyRotate = PropertyValuesHolder.ofFloat(PropertyAnimated.Angle.value, 0f, 360f)
        val animatorStart = ValueAnimator()
        animatorStart.setValues(propertyRadius, propertyRotate)
        animatorStart.repeatWithDelay(1000 , 1000 , customCoroutineScope)

        animatorStart.addUpdateListener {
            radius = it.getAnimatedValue(PropertyAnimated.Radius.value) as Float
            angle = it.getAnimatedValue(PropertyAnimated.Angle.value) as Float
            invalidate()
        }

        animatorStart.start()
    }

    private fun ValueAnimator.repeatWithDelay(durationInMil:Long, delayInMil: Long, lifecycleScope: CoroutineScope) {
        duration = durationInMil
        repeatCount = ObjectAnimator.INFINITE
        repeatMode = ObjectAnimator.REVERSE
        doOnRepeat {
            pause() // pause animator
            lifecycleScope.launch {
                delay(delayInMil)
                resume() // resume animator
            }
        }
    }

    enum class PropertyAnimated(val value:String){
        Radius("Radius"),
        Angle("Rotate")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        job.cancel()
    }
}