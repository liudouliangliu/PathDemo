package com.hs.videoplayerdemo

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.scwang.smart.refresh.layout.util.SmartUtil

/**
 * 请求数据返回值错误的缺省页
 */
class CommonAbnormalPage: LinearLayout {

    constructor(context: Context?, @AbnormalType abnormalType: Int, commonAbnormalClickListener: OnCommonAbnormalClickListener?) : super(context) {
        initView()
        updatePageType(abnormalType, commonAbnormalClickListener)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        gravity = Gravity.CENTER
        orientation = VERTICAL
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        setBackgroundResource(R.color.color_FFFFFF)
    }

    /**
     * 确定显示页面样式
     * @param abnormalType
     */
    fun updatePageType(@AbnormalType abnormalType: Int, onCommonAbnormalClickListener: OnCommonAbnormalClickListener?) {
        removeAllViews()
        val imageView = ImageView(context)
        val textView = TextView(context)
        textView.setTextColor(ContextCompat.getColor(context, R.color.color_999999))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13F)
        val textViewLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textViewLayoutParams.topMargin = SmartUtil.dp2px(15F)
        textView.layoutParams = textViewLayoutParams
        when (abnormalType) {
            AbnormalType.AbnormalTypeResultError -> {
                imageView.setImageResource(R.mipmap.default_image_result_error)
                textView.setText(R.string.net_error)
            }
            AbnormalType.AbnormalTypeResultEmpty -> {
                imageView.setImageResource(R.mipmap.default_image_empty)
                textView.setText(R.string.result_empty)
            }
            else -> {
                imageView.setImageResource(R.mipmap.default_image_net_error)
                textView.setText(R.string.net_error)
            }
        }
        addView(imageView)
        addView(textView)
        setOnClickListener { onCommonAbnormalClickListener?.onClick(abnormalType) }
    }

    interface OnCommonAbnormalClickListener {
        fun onClick(@AbnormalType abnormalType: Int)
    }

}