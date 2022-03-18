package com.hs.videoplayerdemo

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.shuyu.gsyvideoplayer.GSYVideoManager

class ListActivity : BaseListActivity<RecordsBean,VideoViewModel>() {

    private val adapter: RealTimeVideoAdapter = RealTimeVideoAdapter()
    private var recyclerview: RecyclerView? = null
    private var isPause = false

    override fun getLayoutId(): Int {
        return R.layout.activity_list
    }

    override fun initView() {
        super.initView()
        initVideo()
    }
    private fun initVideo() {
//        TODO("Not yet implemented")
        adapter.mContext = this
        recyclerview = findViewById(R.id.baseRecyclerView)
        recyclerview?.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(view: RecyclerView, scrollState: Int) {}
            override fun onScrolled(
                view: RecyclerView,
                firstVisibleItem: Int,
                visibleItemCount: Int
            ) {
                val lastVisibleItem = firstVisibleItem + visibleItemCount
                //大于0说明有播放
                if (GSYVideoManager.instance().playPosition >= 0) {
                    //当前播放的位置
                    val position = GSYVideoManager.instance().playPosition
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().playTag == adapter.TAG && (position < firstVisibleItem || position > lastVisibleItem)) {
                        if (GSYVideoManager.isFullState(this@ListActivity)) {
                            return
                        }
                        //如果滑出去了上面和下面就是否，和今日头条一样
                        GSYVideoManager.releaseAllVideos()
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
        isPause = true
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
        isPause = false
    }
    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
        if (adapter != null) {
            adapter.onDestroy()
        }
    }

    override fun onBackPressed() {
        //为了支持重力旋转
        onBackPressAdapter()
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    /********************************为了支持重力旋转 */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (adapter != null && adapter.getListNeedAutoLand() && !isPause) {
            adapter.onConfigurationChanged(this, newConfig)
        }
    }
    private fun onBackPressAdapter() {
        //为了支持重力旋转
        if (adapter.getListNeedAutoLand()) {
            adapter.onBackPressed()
        }
    }

    override fun adapter(): BasePagedAdapter<RecordsBean> {
        return adapter
    }

}