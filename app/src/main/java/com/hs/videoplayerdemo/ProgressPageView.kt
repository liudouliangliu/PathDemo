package com.hs.videoplayerdemo

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import com.scwang.smart.refresh.layout.util.SmartUtil

/**
 * 加载页面（LoadingView）
 */
class ProgressPageView: LinearLayout {

    private lateinit var imageView: ImageView
    private lateinit var progressDrawable: ProgressDrawable

    constructor(context: Context?): super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private fun initView(){
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        gravity = Gravity.CENTER
        imageView = ImageView(context)
        imageView.layoutParams = LayoutParams(SmartUtil.dp2px(30F), SmartUtil.dp2px(30F))
        addView(imageView)
        progressDrawable = ProgressDrawable()
        progressDrawable.setColor(Color.WHITE)
        progressDrawable.start()
        imageView.setImageDrawable(progressDrawable)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        progressDrawable.stop()
        imageView.clearAnimation()
        imageView.setImageDrawable(null)
    }
}