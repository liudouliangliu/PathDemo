package com.hs.videoplayerdemo

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import androidx.annotation.NonNull
/** 旋转动画 */
class ProgressDrawable: PaintDrawable(), Animatable, AnimatorUpdateListener {

    private var lastWidth = 0
    private var lastHeight = 0
    private var progressDegree = 0
    private var valueAnimator: ValueAnimator = ValueAnimator.ofInt(30, 3600)
    private var path = Path()

    override fun onAnimationUpdate(animation: ValueAnimator) {
        val value = animation.animatedValue as Int
        progressDegree = 30 * (value / 30)
        val drawable: Drawable = this@ProgressDrawable
        drawable.invalidateSelf()
    }

    override fun draw(@NonNull canvas: Canvas) {
        val drawable: Drawable = this@ProgressDrawable
        val bounds = drawable.bounds
        val width = bounds.width()
        val height = bounds.height()
        val pointY = height / 2F
        val pointX = width / 2F
        if (lastWidth != width || lastHeight != height) {
            val round = 1F.coerceAtLeast(width / 22F)
            path.reset()
            path.addCircle(width - round, pointY, round, Path.Direction.CW)
            path.addRect(width - 5 * round, pointY - round, width - round, pointY + round, Path.Direction.CW)
            path.addCircle(width - 5 * round, pointY, round, Path.Direction.CW)
            lastWidth = width
            lastHeight = height
        }
        canvas.save()
        canvas.rotate(progressDegree.toFloat(), pointX, pointY)
        for (i in 0..11) {
            paint.alpha = (i + 5) * 0x11
            canvas.rotate(30F, pointX, pointY)
            canvas.drawPath(path, paint)
        }
        canvas.restore()
    }

    override fun start() {
        if (!valueAnimator.isRunning) {
            valueAnimator.addUpdateListener(this)
            valueAnimator.start()
        }
    }

    override fun stop() {
        if (valueAnimator.isRunning) {
            val animator: Animator = valueAnimator
            animator.removeAllListeners()
            valueAnimator.removeAllUpdateListeners()
            valueAnimator.cancel()
        }
    }

    override fun isRunning(): Boolean = valueAnimator.isRunning

    init {
        valueAnimator.duration = 10000
        valueAnimator.interpolator = null
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.repeatMode = ValueAnimator.RESTART
    }
}