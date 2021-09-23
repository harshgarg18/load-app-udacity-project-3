package com.udacity.loadapp.ui

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.withStyledAttributes
import com.udacity.loadapp.R

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val cornerRadius = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        16.toFloat(),
        Resources.getSystem().displayMetrics
    )
    private val ovalSize = 44
    private val ovalStrokeWidth = 12f
    private val ovalSpace = RectF()
    private var buttonText = resources.getString(R.string.download_cta)

    private var valueAnimator: ValueAnimator? = null
    private var progress: Float = 0f

    var buttonState = ButtonState.IDLE
        set(value) {
            if (field == value) {
                return
            }

            if (value == ButtonState.IDLE) {
                buttonText = resources.getString(R.string.download_cta)
                valueAnimator?.cancel()
                valueAnimator = null
                progress = 0f
            } else {
                buttonText = resources.getString(R.string.downloading_cta)
                valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
                    repeatMode = ValueAnimator.REVERSE
                    repeatCount = ValueAnimator.INFINITE
                    duration = 3000
                    interpolator = AccelerateDecelerateInterpolator()

                    addUpdateListener {
                        progress = animatedValue as Float
                        invalidate()
                    }

                    start()
                }
            }

            field = value
            invalidate()
        }

    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
    }

    private val animationLoadingPaint = Paint().apply {
        isAntiAlias = true
    }

    // Referred this link for arc animation
    // https://medium.com/@paulnunezm/canvas-animations-simple-circle-progress-view-on-android-8309900ab8ed
    private val parentArcPaint = Paint().apply {
        isAntiAlias = true
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = ovalStrokeWidth
    }

    private val fillArcPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = ovalStrokeWidth
    }


    private val textPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)
    }


    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            backgroundPaint.color =
                getColor(R.styleable.LoadingButton_buttonBackgroundColor, Color.BLUE)
            animationLoadingPaint.color =
                getColor(R.styleable.LoadingButton_animationLoadingColor, Color.WHITE)
            fillArcPaint.color = getColor(R.styleable.LoadingButton_animationArcColor, Color.WHITE)
            textPaint.color = getColor(R.styleable.LoadingButton_android_textColor, Color.WHITE)
            textPaint.textSize = getDimension(R.styleable.LoadingButton_android_textSize, 22f)
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val width = measuredWidth.toFloat()
        val height = measuredHeight.toFloat()

        val textHeight = textPaint.descent() - textPaint.ascent()
        val textOffset = textHeight / 2 - textPaint.descent()
        val textX = width / 2
        val textY = height / 2 + textOffset

        canvas?.apply {
            drawRoundRect(0f, 0f, width, height, cornerRadius, cornerRadius, backgroundPaint)
            drawText(buttonText, textX, textY, textPaint)

            if (buttonState != ButtonState.IDLE) {
                val progressWidth = progress * width
                drawRoundRect(
                    0f,
                    0f,
                    progressWidth,
                    height,
                    cornerRadius,
                    cornerRadius,
                    animationLoadingPaint
                )

                val horizontalCenter = paddingLeft.toFloat() + 3 * ovalSize
                val verticalCenter = height.div(2)
                val progressAngle = progress * 360
                ovalSpace.set(
                    horizontalCenter - ovalSize,
                    verticalCenter - ovalSize,
                    horizontalCenter + ovalSize,
                    verticalCenter + ovalSize
                )
                drawArc(ovalSpace, 0f, 360f, false, parentArcPaint)
                drawArc(ovalSpace, 270f, progressAngle, false, fillArcPaint)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        setMeasuredDimension(w, h)
    }

}
