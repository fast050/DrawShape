package com.example.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min


class TicketBackGroundShape(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint()
    private var cornerRadius = 10f
    private var innerRadius = 20f
    private var scaleFactor = 0f
    private var ticketShapeHeight = 166f
    private var ticketDividerHeight = 103f
    private var ticketCornerShape: Int = 0
    private var ticketBackgroundColor:Int = Color.GRAY
    private var ticketDividerColor:Int = Color.BLACK

    init {
        val typedArray: TypedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.TicketBackGroundShape, 0, 0
        )
        try {
            innerRadius = typedArray.getDimensionPixelSize(
                R.styleable.TicketBackGroundShape_ticketInternalRadius,
                20
            ).toFloat()
            cornerRadius = typedArray.getDimensionPixelSize(
                R.styleable.TicketBackGroundShape_ticketCornerRadius,
                10
            ).toFloat()

            ticketCornerShape =
                typedArray.getInteger(
                    R.styleable.TicketBackGroundShape_ticketCornerShape,
                    0)

            ticketShapeHeight =
                typedArray.getFloat(
                    R.styleable.TicketBackGroundShape_ticketShapeHeight,
                    200f
                )

            ticketDividerHeight=
                typedArray.getFloat(
                    R.styleable.TicketBackGroundShape_ticketDividerHeight,
                    150f
                )

            ticketBackgroundColor=
                typedArray.getColor(R.styleable.TicketBackGroundShape_ticketBackgroundColor , Color.GRAY)

            ticketDividerColor=
                typedArray.getColor(R.styleable.TicketBackGroundShape_ticketDividerColor , Color.BLACK)
        } finally {
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.color = ticketBackgroundColor
        canvas?.let {
            when (ticketCornerShape) {
                0 -> drawTicket(canvas)
                1 -> drawTicketRoundCorner(canvas)
            }
        }

    }

    private fun drawTicket(canvas: Canvas) {
        val originX = 0f.Scale() + scaleFactor.Scale()
        val originY = 0f.Scale() + scaleFactor.Scale()
        val width = width.toFloat() - scaleFactor.Scale() //335f
        val height = height.toFloat() - scaleFactor.Scale() //166f
        val radios = innerRadius.Scale()
        val dashLinePointShiftFromOriginX = ticketDividerHeight.Scale()
        val dashLinePositionFromBottomShape = (ticketShapeHeight - ticketDividerHeight).Scale()


        val path = Path().apply {
            moveTo(originX, originY)
            lineTo(width, originY)

            // arch
            lineTo(width, dashLinePointShiftFromOriginX - radios)
            arcTo(
                width - radios,
                dashLinePointShiftFromOriginX - radios,
                width + radios,
                dashLinePointShiftFromOriginX + radios,
                270f,
                -180f,
                false
            )

            lineTo(width, height)
            lineTo(originX, height)

            //arch
            lineTo(originX, dashLinePositionFromBottomShape + radios)
            arcTo(
                originX - radios,
                dashLinePointShiftFromOriginX - radios,
                originX + radios,
                dashLinePointShiftFromOriginX + radios,
                90f,
                -180f,
                false
            )
            close()
        }

        canvas.drawPath(path, paint)

        val paints = Paint().apply {
            color = ticketDividerColor
            strokeWidth = 2f.Scale()
            style = Paint.Style.STROKE
            paint.flags = Paint.ANTI_ALIAS_FLAG
            pathEffect = DashPathEffect(floatArrayOf(30f, 20f), 15f)
        }

        canvas.drawLine(
            originX + radios,
            dashLinePointShiftFromOriginX,
            width - radios,
            dashLinePointShiftFromOriginX,
            paints
        )
    }

    private fun drawTicketRoundCorner(canvas: Canvas) {
        val originX = 0f.Scale() + scaleFactor.Scale()
        val originY = 0f.Scale() + scaleFactor.Scale()
        val width = width.toFloat() - scaleFactor.Scale() //335f
        val height = height.toFloat() - scaleFactor.Scale() //166f
        val radiosInternal = innerRadius.Scale()
        val radiosCorner = cornerRadius.Scale()
        val dashLinePointShiftFromOriginX = ticketDividerHeight.Scale()
        val dashLinePositionFromBottomShape = (ticketShapeHeight - ticketDividerHeight).Scale()

        val path = Path().apply {
            moveTo(originX, originY)
            lineTo(width - (radiosCorner * 2), originY)

            //arch in top right corner
            arcTo(
                width - (radiosCorner * 2),
                originY,
                width,
                originY + (radiosCorner * 2),
                270f,
                90f,
                true
            )
            lineTo(width, originY + (radiosCorner * 2))

            // arch internal right
            lineTo(width, dashLinePointShiftFromOriginX - radiosInternal)
            arcTo(
                width - radiosInternal,
                dashLinePointShiftFromOriginX - radiosInternal,
                width + radiosInternal,
                dashLinePointShiftFromOriginX + radiosInternal,
                270f,
                -180f,
                false
            )

            //arch in bottom right corner
            arcTo(
                width - (radiosCorner * 2),
                height - (radiosCorner * 2),
                width,
                height,
                0f,
                90f,
                false
            )
            lineTo(width - (radiosCorner * 2), height)
            lineTo(originX + (radiosCorner * 2), height)

            //arch in bottom left corner
            arcTo(
                originX,
                height - (radiosCorner * 2),
                originX + (radiosCorner * 2),
                height,
                90f,
                90f,
                false
            )
            lineTo(originX, height - (radiosCorner * 2))


            //arch internal left
            lineTo(originX, dashLinePositionFromBottomShape + radiosInternal)
            arcTo(
                originX - radiosInternal,
                dashLinePointShiftFromOriginX - radiosInternal,
                originX + radiosInternal,
                dashLinePointShiftFromOriginX + radiosInternal,
                90f,
                -180f,
                false
            )

            lineTo(originX, (radiosCorner * 2))
            arcTo(
                originX,
                originY,
                originX + (radiosCorner * 2),
                originY + (radiosCorner * 2),
                180f,
                90f,
                false
            )
            close()
        }

        canvas.drawPath(path, paint)

        val paints = Paint().apply {
            color = ticketDividerColor
            strokeWidth = 2f.Scale()
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(floatArrayOf(30f, 20f), 15f)
        }

        canvas.drawLine(
            originX + radiosInternal,
            dashLinePointShiftFromOriginX,
            width - radiosInternal,
            dashLinePointShiftFromOriginX,
            paints
        )
    }

    private fun Float.Scale(): Float {
        val heightByDesign = ticketShapeHeight
        return height * this / heightByDesign
    }

    private fun drawArchWithRectangle(canvas: Canvas) {
        val path = Path().apply {
            // Draws triangle
            /*moveTo(100f, 100f)
                lineTo(300f, 300f)
                lineTo(100f, 300f)
                close()*/

            // Draws bridge
            moveTo(100f, 100f)
            lineTo(600f, 100f)
            lineTo(600f, 400f)
            lineTo(600f, 700f)
            lineTo(400f, 700f)
            // bottom is 900 because the system draws the arc inside an
            // imaginary rectangle
            arcTo(100f, 500f, 400f, 900f, 0f, -180f, false)
            lineTo(100f, 700f)
            close()
        }

        canvas.drawPath(path, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desireWidth = MeasureSpec.getSize(widthMeasureSpec)
        val desireHeight = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        
        val width = when(widthMode){
            MeasureSpec.EXACTLY -> desireWidth
            MeasureSpec.AT_MOST -> min(desireWidth , 500)
            else -> 500
            
        }

        val height = when(heightMode){
            MeasureSpec.EXACTLY -> desireHeight
            MeasureSpec.AT_MOST -> min(desireHeight , 1000)
            else -> 1000

        }
        
        setMeasuredDimension(width , height)
    }


}