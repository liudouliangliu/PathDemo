package com.hs.videoplayerdemo.widget

import android.animation.Animator
import kotlin.jvm.JvmOverloads
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.View

class SearchView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private val mPaint = Paint()
    private var mViewWidth = 0
    private var mViewHeight = 0

    //当前状态
    private var mCurrentState = State.NONE

    //放大镜和外部圆
    private var pathSearch: Path? = null
    private var pathCircle: Path? = null

    //测量path 并截取部分的工具
    private var mMeasure: PathMeasure? = null

    //动画时间
    private val defaultDuration = 2000

    //控制动画
    private var mStartingAnimator: ValueAnimator? = null
    private var mSearchingAnimator: ValueAnimator? = null
    private var mEndingAnimator: ValueAnimator? = null

    //动画数值
    private var mAnimatorValue = 0f

    //动画监听
    private var mUpdateListener: AnimatorUpdateListener? = null
    private var mAnimatorListener: Animator.AnimatorListener? = null

    //动画控制
    private var mAnimatorHandler: Handler? = null

    //动画是否结束
    private var isOver = false
    private var count = 0

    enum class State {
        NONE, STARTING, SEARCHING, ENDING
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
    }

    private fun init() {
        initPaint()
        initPath()
        initListener()
        initHandler()
        initAnimator()
        //进入开始动画
        mCurrentState = State.STARTING
        mStartingAnimator!!.start()
    }

    fun startAnimator() {
//        mAnimatorHandler!!.removeCallbacks(null)
        mStartingAnimator!!.removeAllListeners()
        mStartingAnimator!!.cancel()
        mSearchingAnimator!!.removeAllListeners()
        mSearchingAnimator!!.cancel()
        mEndingAnimator!!.removeAllListeners()
        mEndingAnimator!!.cancel()
        initAnimator()
        mCurrentState = State.STARTING
        mStartingAnimator!!.start()
    }

    private fun initAnimator() {
        mStartingAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(defaultDuration.toLong())
        mSearchingAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(defaultDuration.toLong())
        mEndingAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(defaultDuration.toLong())
        mStartingAnimator?.addUpdateListener(mUpdateListener)
        mSearchingAnimator?.addUpdateListener(mUpdateListener)
        mEndingAnimator?.addUpdateListener(mUpdateListener)
        mStartingAnimator?.addListener(mAnimatorListener)
        mSearchingAnimator?.addListener(mAnimatorListener)
        mEndingAnimator?.addListener(mAnimatorListener)
    }

    private fun initHandler() {
        mAnimatorHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (mCurrentState) {
                    State.STARTING -> {
                        //从开始动画转换搜索动画
                        isOver = false
                        mCurrentState = State.SEARCHING
                        mStartingAnimator!!.removeAllListeners()
                        mSearchingAnimator!!.start()
                    }
                    State.SEARCHING -> if (!isOver) {
                        mSearchingAnimator!!.start()
                        Log.d(TAG, "handleMessage: $count")
                        count++
                        if (count > 2) {
                            isOver = true
                            count = 0
                        }
                    } else {
                        mCurrentState = State.ENDING
                        mEndingAnimator!!.start()
                    }
                    State.ENDING -> {
                        mCurrentState = State.NONE
                        invalidate()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun initListener() {
        //设置动画监听
        mUpdateListener = AnimatorUpdateListener { valueAnimator: ValueAnimator ->
            mAnimatorValue = valueAnimator.animatedValue as Float
            invalidate()
        }
        mAnimatorListener = object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                //gethandle发消息通知动画状态更新
                mAnimatorHandler!!.sendEmptyMessage(0)
            }
        }
    }

    private fun initPath() {
        pathCircle = Path()
        pathSearch = Path()
        mMeasure = PathMeasure()
        val oval1 = RectF(-50f, -50f, 50f, 50f) //放大镜外环
        pathSearch!!.addArc(oval1, 45f, 359.9f)
        val oval2 = RectF(-100f, -100f, 100f, 100f)
        pathCircle!!.addArc(oval2, 45f, 359.9f)
        val pos = FloatArray(2)
        mMeasure!!.setPath(pathCircle, false) //放大镜把手位置
        mMeasure!!.getPosTan(0f, pos, null)
        pathSearch!!.lineTo(pos[0], pos[1])
        Log.d(TAG, "initPath pos = " + pos[0] + ":" + pos[1])
    }

    private fun initPaint() {
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.WHITE
        mPaint.strokeWidth = 15f
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawSearch(canvas)
    }

    private fun drawSearch(canvas: Canvas) {
        mPaint.color = Color.WHITE
        canvas.translate((mViewWidth / 2).toFloat(), (mViewHeight / 2).toFloat())
        canvas.drawColor(Color.parseColor("#0082D7"))
        when (mCurrentState) {
            State.NONE -> canvas.drawPath(
                pathSearch!!, mPaint
            )
            State.STARTING -> {
                mMeasure!!.setPath(pathSearch, false)
                val dst = Path()
                mMeasure!!.getSegment(
                    mMeasure!!.length * mAnimatorValue,
                    mMeasure!!.length,
                    dst,
                    true
                )
                canvas.drawPath(dst, mPaint)
            }
            State.SEARCHING -> {
                mMeasure!!.setPath(pathCircle, false)
                val dst2 = Path()
                val stop = mMeasure!!.length * mAnimatorValue
                val start = (stop - (0.5 - Math.abs(mAnimatorValue - 0.5)) * 200f).toFloat()
                mMeasure!!.getSegment(start, stop, dst2, true)
                canvas.drawPath(dst2, mPaint)
            }
            State.ENDING -> {
                mMeasure!!.setPath(pathSearch, false)
                val dst3 = Path()
                mMeasure!!.getSegment(0f, mMeasure!!.length * mAnimatorValue, dst3, true)
                canvas.drawPath(dst3, mPaint)
            }
        }
    }

    companion object {
        private val TAG = SearchView::class.java.simpleName
    }

    init {
        init()
    }
}