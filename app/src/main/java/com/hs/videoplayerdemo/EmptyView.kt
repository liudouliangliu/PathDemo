package com.hs.videoplayerdemo

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlin.properties.Delegates

/**
 * @author Yang Hao
 * @date 2020/11/24
 */
class EmptyView(context: Context, attrs: AttributeSet?): FrameLayout(context, attrs) {

    private var progressPageView: ProgressPageView? = null
    private var commonAbnormalPage: CommonAbnormalPage? = null

    var state: Status by Delegates.observable(Status.DISMISS) { _, old, new ->
        if (old != new) {
            if (progressPageView != null) {
                removeView(progressPageView)
                progressPageView = null
            }
            if (commonAbnormalPage != null) {
                removeView(commonAbnormalPage)
                commonAbnormalPage = null
            }
            when (new) {
                Status.LOADING -> {
                    progressPageView = ProgressPageView(context)
                    addView(progressPageView)
                }
                Status.NO_DATA -> {
                    commonAbnormalPage = CommonAbnormalPage(context, AbnormalType.AbnormalTypeResultEmpty, null)
                    commonAbnormalPage?.setBackgroundResource(R.color.color_FFFFFF)
                    addView(commonAbnormalPage)
                }
                Status.LOAD_FAILED -> {
                    commonAbnormalPage = CommonAbnormalPage(context, AbnormalType.AbnormalTypeResultError, null)
                    commonAbnormalPage?.setBackgroundResource(R.color.color_FFFFFF)
                    addView(commonAbnormalPage)
                }
                Status.NETWORK_UNAVAILABLE -> {
                    commonAbnormalPage = CommonAbnormalPage(context, AbnormalType.AbnormalTypeNetError, null)
                    commonAbnormalPage?.setBackgroundResource(R.color.color_FFFFFF)
                    addView(commonAbnormalPage)
                }
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        state = Status.LOADING
    }

    enum class Status {
        DISMISS, LOADING, NO_DATA, LOAD_FAILED, NETWORK_UNAVAILABLE
    }
}