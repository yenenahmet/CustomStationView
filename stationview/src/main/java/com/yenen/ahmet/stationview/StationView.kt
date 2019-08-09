package com.yenen.ahmet.stationview

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class StationView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // Paint Object
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    //Animator Object
    private val circleAnimator = ValueAnimator()

    // Circle Color //
    var outerCircleColor = Color.GRAY
        set(value) {
            field = value
            invalidate()
        }
    var firstLineColor = Color.GRAY
        set(value) {
            field = value
            invalidate()
        }

    var secondLineColor = Color.GRAY
        set(value) {
            field = value
            invalidate()
        }
    var centerCircleSecondaryColor = Color.GREEN
        set(value) {
            field = value
            invalidate()
        }
    var centerCirclePrimaryColor = Color.RED
        set(value) {
            field = value
            invalidate()
        }
    var middleCircleColor = Color.WHITE
        set(value) {
            field = value
            invalidate()
        }

    // Circle Percent//
    var centerPercent = 26.0f
        set(value) {
            field = value
            invalidate()
        }
    var outerPercent = 50.0f
        set(value) {
            field = value
            invalidate()
        }
    var middlePercent = 34.0f
        set(value) {
            field = value
            invalidate()
        }

    // Kaç Eş Parçaya ayrılacak Default = 3
    var stationQuantity: Int = 3
        set(value) {
            stationWidth = resources.displayMetrics.widthPixels / value
            field = value
            requestLayout()
        }

    // Ekran Genişlik ve Yükseklik Ayarları
    private var stationWidth: Int = 0
    private var stationHeight: Float = 200f

    // Çizim parametreleri
    private val circleCx: Float
    private var centerCircleRadius: Float = 0f

    private var circleCy: Float = 0f
    private var outerCircleRadius: Float = 0f
    private var middleCircleRadius: Float = 0f
    var centerCircleColor = centerCirclePrimaryColor

    // Animation
    private var lastAnimationValue: Float = 0f
    private var animationDuration: Long = 800L
    private var animationRepeatCount: Int = 5
    private var centerCircleRadiusAnimatorValue: Float = 0f


    init {
        // setWillNotDraw(false)
        stationWidth = resources.displayMetrics.widthPixels / stationQuantity
        setupAttributes(attrs)
        circleCx = stationWidth / 2f
        initCircleAnimator()
    }

    private fun initCircleAnimator() {
        circleAnimator.apply {
            duration = animationDuration
            interpolator = LinearInterpolator()
            repeatCount = animationRepeatCount
            repeatMode = ValueAnimator.REVERSE
        }.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                animation?.let {
                    onAnimationUpdateCircle(it)
                }
            }
        })
        circleAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                changeCenterCircleColor(centerCircleSecondaryColor)
            }

        })
    }

    private fun onAnimationUpdateCircle(animation: ValueAnimator) {
        centerCircleRadiusAnimatorValue = animation.animatedValue as Float
        if (centerCircleRadiusAnimatorValue > lastAnimationValue) {
            centerCircleColor = centerCirclePrimaryColor
        } else {
            centerCircleColor = centerCircleSecondaryColor
        }
        lastAnimationValue = centerCircleRadiusAnimatorValue
        invalidate()
    }

    private fun calculate() {
        circleCy = stationHeight / 2f
        val circleRadiusPercentiles = circleCy / 100f
        centerCircleRadius = circleRadiusPercentiles * centerPercent
        centerCircleRadiusAnimatorValue = centerCircleRadius
        outerCircleRadius = circleRadiusPercentiles * outerPercent
        middleCircleRadius = circleRadiusPercentiles * middlePercent
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (heightMode == MeasureSpec.EXACTLY) {
            stationHeight = heightSize.toFloat();
        } else {
            stationHeight = 200f
        }
        calculate()
        setMeasuredDimension(stationWidth, stationHeight.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            secondTrackLineDraw(canvas, outerCircleRadius - middleCircleRadius)
            firtTrackLineDraw(canvas, outerCircleRadius - middleCircleRadius)
            outerCircleDraw(canvas, circleCx, circleCy, outerCircleRadius)
            middleCircleDraw(canvas, circleCx, circleCy, middleCircleRadius)
            centerCircleDraw(canvas, circleCx, circleCy)
        }
    }

    // Draw //
    private fun centerCircleDraw(canvas: Canvas, cx: Float, cy: Float) {
        paint.color = centerCircleColor
        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, centerCircleRadiusAnimatorValue, paint)
    }

    private fun outerCircleDraw(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        paint.color = outerCircleColor
        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, radius, paint)
    }

    private fun middleCircleDraw(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        paint.color = middleCircleColor
        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, radius, paint)
    }

    private fun secondTrackLineDraw(canvas: Canvas, with: Float) {
        paint.color = secondLineColor
        paint.style = Paint.Style.FILL
        paint.strokeWidth = with
        canvas.drawLine((circleCx + outerCircleRadius)-5f, circleCy, stationWidth.toFloat() + 5f, circleCy, paint)
    }

    private fun firtTrackLineDraw(canvas: Canvas, with: Float) {
        paint.color = firstLineColor
        paint.style = Paint.Style.FILL
        paint.strokeWidth = with
        canvas.drawLine(0f, circleCy, (circleCx - outerCircleRadius)+5f, circleCy, paint)

    }
    // Draw //

    // Animator //
    fun startCircleAnimator() {
        circleAnimator.setFloatValues(0f, centerCircleRadius)
        circleAnimator.start()
    }

    fun pauseCircleAnimator() {
        circleAnimator.pause()
        centerCircleRadiusAnimatorValue = centerCircleRadius
        invalidate()
    }


    // Animator //


    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.StationView,
            0, 0
        )

        outerCircleColor = typedArray.getColor(R.styleable.StationView_outerCircleColor, Color.GRAY)
        centerCirclePrimaryColor = typedArray.getColor(R.styleable.StationView_centerCirclePrimaryColor, Color.RED)
        centerCircleSecondaryColor =
            typedArray.getColor(R.styleable.StationView_centerCircleSecondaryColor, Color.GREEN)

        middleCircleColor = typedArray.getColor(R.styleable.StationView_middleCircleColor, Color.WHITE)

        firstLineColor = typedArray.getColor(R.styleable.StationView_firstLineColor,Color.GRAY)
        secondLineColor = typedArray.getColor(R.styleable.StationView_secondLineColor,Color.GRAY)

        centerPercent = typedArray.getFloat(R.styleable.StationView_centerPercent, 26.0f)
        middlePercent = typedArray.getFloat(R.styleable.StationView_middlePercent, 34.0f)
        outerPercent = typedArray.getFloat(R.styleable.StationView_outerPercent, 50.0f)

        animationDuration = typedArray.getInteger(R.styleable.StationView_duration, 800).toLong()
        animationRepeatCount = typedArray.getInteger(R.styleable.StationView_animationRepeatCount, 5)

        stationQuantity = typedArray.getInt(R.styleable.StationView_stationQuantity, 3)

        typedArray.recycle()
    }

    fun changeCenterCircleColor(color: Int) {
        centerCircleRadiusAnimatorValue = centerCircleRadius
        centerCircleColor = color
        invalidate()
    }


    // clear to let GC do its work
    fun unBind() {
        circleAnimator.cancel()
        circleAnimator.removeAllUpdateListeners()
        circleAnimator.removeAllListeners()
    }
}