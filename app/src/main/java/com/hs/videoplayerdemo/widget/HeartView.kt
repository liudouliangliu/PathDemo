package com.hs.videoplayerdemo.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin

class HeartView(context:Context,attrs:AttributeSet):View(context,attrs) {
    private val mPaint = Paint()
    private val mPath = Path()
    private var mWidth = 0
    private var mHeight = 0
    private var mCurrentPercent = 0f
    var animation:ValueAnimator? = null
    private var mPathMeasure:PathMeasure = PathMeasure()
    private var multiple = 1
    private var benchmark = 12
    private val TAG = HeartView::class.java.simpleName

    init {
        init()
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2Px(context: Context,dpValue:Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue, context.resources.displayMetrics
        ).toInt()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        Log.d(TAG, "onSizeChanged w and h : $w -- $h")
        multiple = if (w<=h){
            w/32
        }else{
            h/32
        }
        Log.d(TAG, "multiple : $multiple -- ${multiple * 16}")
        var offset: Double
        val st1 = 3.141592653589793
        val st2 = 5.375614096142535
        var minH = multiple * (13 * Math.cos(st2)
                - 5 * Math.cos(2 * st2) -
                2 * Math.cos(3 * st2) -
                Math.cos(4 * st2))

        var maxH = multiple * (13 * Math.cos(st1) -
                5 * Math.cos(2 * st1) -
                2 * Math.cos(3 * st1) -
                Math.cos(4 * st1))
        maxH = Math.abs(maxH)
        offset = (maxH-minH)/2
        mPath.reset()
        var max = 0.0
        var out = 0.0
        for ( i in (0 + 180)..(361 + 180)) {
            val st = i * 2 * Math.PI / 360.0
            val x = multiple * 16 * sin(st).pow(3.0)
            val y = multiple * (13 * Math.cos(st) - 5 * Math.cos(2 * st) - 2 * Math.cos(3 * st) - Math.cos(4 * st))
            if (max<y){
                max = y
                out = st
            }
            if (i==180){
                mPath.moveTo((mWidth / 2 + (x)).toFloat(), (mHeight / 2 - (y)).toFloat()-offset.toInt())
            }
            mPath.lineTo((mWidth / 2 +  (x)).toFloat(), (mHeight / 2 -  (y)).toFloat()-offset.toInt())
        }
        Log.d(TAG, "onSizeChanged max out : $max -- $out")//5.375614096142535
        mPathMeasure.setPath(mPath,false)
    }

    private fun init() {
        //初始化
        mPaint.color = Color.RED
        mPaint.strokeWidth = 5F
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        initAnimation()
    }

    private fun initAnimation() {
        animation = ValueAnimator.ofFloat(0f,1f)
        animation?.duration = 5000
        animation?.addUpdateListener {
            mCurrentPercent = it.animatedValue as Float
            invalidate()
        }
    }

    fun startAnimation(){
        animation?.cancel()
        animation?.start()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.WHITE)

        mPathMeasure.length
        val path = Path()
        mPathMeasure.getSegment(0f,mPathMeasure.length*mCurrentPercent,path,true)
        canvas?.drawPath(path,mPaint)
    }



}