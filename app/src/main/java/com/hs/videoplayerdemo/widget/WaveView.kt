package com.hs.videoplayerdemo.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.hs.videoplayerdemo.R


class WaveView(context: Context?, attributeSet: AttributeSet? = null) :
    View(context, attributeSet) {
    private val TAG = WaveView::class.java.simpleName
    private var mPaint = Paint()
    private var mPath = Path()

    private var mBitmap: Bitmap? = null
    private var mPathMeasure: PathMeasure? = null
    private var mMatrix: Matrix? = null
    private var faction: Float = 0f
    private var list: ArrayList<Point> = arrayListOf()
    private var mPaintDst: Paint = Paint()
    private var dst: Path? = null
    private var mWidth = 0
    private var mHeight = 0

    init {
        init()
    }

    private fun init() {
        //初始化
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
        mPaint.isAntiAlias = true

        mPaintDst.set(mPaint)
        mPaintDst.strokeWidth = 3f
        mPaintDst.color = Color.BLUE

        mPathMeasure = PathMeasure()
        mPath = Path()
        dst = Path()
        mMatrix = Matrix()

        val options = BitmapFactory.Options()
        options.inSampleSize = 2
        mBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher, options)


    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        //随机生成一堆坐标点
        list.clear()
        for (index in 0..20) {
            val point = Point()
            point.x = (Math.random() * w).toInt()
            point.y = (Math.random() * h).toInt()
            list.add(point)
        }
        Log.d(TAG, "onSizeChanged list.size : ${list.size}")
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //重置path，matrix
        mPath.reset()
        dst?.reset()
        mMatrix?.reset()

        //绘制轨道
        drawQuadPath(canvas)

        //测量path
        mPathMeasure?.setPath(mPath, false)
        //获取轨道长度
        val length = mPathMeasure?.length
        //获取matrix
//        val matrix = mPathMeasure?.getMatrix(
//            length!! * faction,
//            mMatrix,
//            PathMeasure.TANGENT_MATRIX_FLAG
//        )
        //获取图片移动过的轨迹
        mPathMeasure?.getSegment(0f, length!! * faction, dst, true)
//        if (matrix == true) {
//            val pos = FloatArray(2)
//            mPathMeasure?.getPosTan(length!! * faction,pos,null)
//
//            mMatrix?.preTranslate(
//                pos[0]- mBitmap!!.width / 2,pos[1] - mBitmap!!.height
//            )
//            canvas?.drawBitmap(mBitmap!!, mMatrix!!, null)
//        }
        val pos = FloatArray(2)
        val tan = FloatArray(2)
                val posTan = mPathMeasure?.getPosTan(length!!*faction,pos,tan)
        if(posTan==true){
            // 将tan值通过反正切函数得到对应的弧度，在转化成对应的角度度数
            val degrees = (Math.atan2(tan[1].toDouble(),tan[0].toDouble())*180f / Math.PI);
            mMatrix?.postRotate(degrees.toFloat(), (mBitmap!!.width /2).toFloat(),
                (mBitmap!!.height / 2).toFloat()
            )
            mMatrix?.postTranslate(pos[0]- mBitmap!!.width / 2,pos[1] - mBitmap!!.height);
            canvas?.drawBitmap(mBitmap!!,mMatrix!!,mPaint)
        }

        //绘制图片移动过的轨迹
        canvas?.drawPath(dst!!, mPaintDst)
    }

    private fun drawQuadPath(canvas: Canvas?) {
        var i = 0
        while (i < list.size) {
            if (i + 1 < list.size) {
                val p1 = list[i]
                val p2 = list[i + 1]
                mPath.quadTo(p1.x.toFloat(), p1.y.toFloat(), p2.x.toFloat(), p2.y.toFloat())
            }
            i += 2
        }
//        mPath.lineTo(mWidth.toFloat(), mHeight.toFloat())
//        mPath.lineTo(0f, height.toFloat())
//        mPath.close()
        //绘制贝塞尔曲线
        canvas?.drawPath(mPath, mPaint)
    }

    fun startAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 100000
        animator.interpolator = LinearInterpolator()
//        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener { animation ->
            faction = animation.animatedValue as Float
            postInvalidate()
        }
        animator.start()
    }

}