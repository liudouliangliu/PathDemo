package com.hs.videoplayerdemo

import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
/** 画笔 Drawable */
abstract class PaintDrawable: Drawable() {

    protected var paint = Paint()

    fun setColor(color: Int) {
        paint.color = color
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    init {
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.color = Color.WHITE
    }
}