package com.example.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.content.ContextCompat
import com.example.customview.util.RotationGestureDetector
import kotlin.math.min

/**
 *  this view can be move , scale , rotate
 */
class TransformingDrawableView(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {

    private val drawable: Drawable? = ContextCompat.getDrawable(
        context,
        R.drawable.ic_smiley
    )
    private val drawableBoundsRect = Rect()
    private var isConsumeNextGesture = false
    private var lastPositionX = 0f
    private var lastPositionY = 0f
    private var factorScale =1f
    private var rotateAngle = 0f
    private val transformationMatrix = Matrix()


    private val gestureDetectorListener = object : GestureDetector.SimpleOnGestureListener() {

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {

            if (isConsumeNextGesture){
                lastPositionX -= distanceX
                lastPositionY -= distanceY

                updateTransformation()
                invalidate()
            }

            return true
        }

    }

    private val scaleGestureDetector = ScaleGestureDetector(context , object :ScaleGestureDetector.SimpleOnScaleGestureListener(){
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            factorScale *= detector.scaleFactor
            updateTransformation()
            invalidate()
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
           return isConsumeNextGesture
        }

    })

    private val rotateGestureDetector = RotationGestureDetector().apply {
        onRotationGestureListener = object :RotationGestureDetector.OnRotationGestureListener{
            override fun onRotationBegin(): Boolean {
             return isConsumeNextGesture
            }

            override fun onRotationEnd() {}

            override fun onRotation(angleDiff: Float): Boolean {
                rotateAngle -= angleDiff
                updateTransformation()
                invalidate()
                return true
            }

        }
    }

    private val gestureDetector :GestureDetector = GestureDetector(context , gestureDetectorListener)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event==null)
            return false

        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            isConsumeNextGesture = isWithinDrawable(event)
        }

          val rotateResult = rotateGestureDetector.onTouchEvent(event)
          val scaleResult  = scaleGestureDetector.onTouchEvent(event)
          val gestureResult= gestureDetector.onTouchEvent(event)

        return  rotateResult || scaleResult || gestureResult || super.onTouchEvent(event)
    }

    private fun isWithinDrawable(event: MotionEvent): Boolean {
        // The idea here is to perform inverse transformation on the coordinates of the MotionEvent,
        // which will bring them into the coordinate system of the original Drawable's bounding
        // rectangle, and then check whether the transformed coordinates fall within that bounding
        // rectangle.

        // 1. Compute the inverse of the matrix that transforms the Drawable
        val invertedTransformationMatrix = Matrix()
        if (!transformationMatrix.invert(invertedTransformationMatrix)) {
            throw RuntimeException("failed to invert the transformation matrix")
        }

        // 2. Transform the MotionEvent's coordinates using the inverted matrix
        val eventCoordinates = floatArrayOf(event.x, event.y)
        invertedTransformationMatrix.mapPoints(eventCoordinates)

        // 3. Check whether the resulting point is within the original (before transformation) bounds of the Drawable.
        return drawableBoundsRect.contains(
            eventCoordinates[0].toInt(),
            eventCoordinates[1].toInt(),
        )
    }


    private fun updateTransformation(){
        transformationMatrix.apply {
            reset()
            postRotate(rotateAngle , width/2f , height/2f)
            postScale(factorScale , factorScale , width/2f ,  height/2f )
            postTranslate(lastPositionX , lastPositionY)
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val drawableMaxSize = min(w, h) * DRAWABLE_DEFAULT_SIZE_FRACTION
        val drawableIntrinsicRatio = 1f * drawable!!.intrinsicWidth / drawable.intrinsicHeight

        val drawableWidth = if (drawableIntrinsicRatio > 1) {
            drawableMaxSize
        } else {
            drawableMaxSize * drawableIntrinsicRatio
        }
        val drawableHeight = if (drawableIntrinsicRatio > 1) {
            drawableMaxSize / drawableIntrinsicRatio
        } else {
            drawableMaxSize
        }

        val marginHorizontal = ((w - drawableWidth) / 2).toInt()
        val marginVertical   = ((h - drawableHeight) / 2).toInt()

        drawableBoundsRect.set(
            marginHorizontal,
            marginVertical,
            w - marginHorizontal ,
            h - marginVertical
        )

        drawable.bounds = drawableBoundsRect
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.concat(transformationMatrix)
        drawable?.draw(canvas)
        canvas.restore()
    }

    companion object {
        const val DRAWABLE_DEFAULT_SIZE_FRACTION = .5f
    }

}