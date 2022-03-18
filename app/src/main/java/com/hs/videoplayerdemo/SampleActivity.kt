package com.hs.videoplayerdemo

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer

class SampleActivity : AppCompatActivity() {
    //播放器
    private var player: SampleCoverVideo? = null
    //屏幕控制工具，用于全屏播放
    var orientationUtils: OrientationUtils? = null
    private val isPlay = false
    private var isPause = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_video)
        player = findViewById(R.id.player)
        player?.backButton?.visibility = View.GONE
        //是否可以滑动调整
        player?.setIsTouchWiget(true)
        ///不需要屏幕旋转
        player?.setNeedOrientationUtils(false)
//        binding.player.setUp(url, true, intent.getStringExtra("name"))
        //外部辅助的旋转，帮助全屏
        orientationUtils = OrientationUtils(this, player)
        //初始化不打开外部的旋转
        orientationUtils!!.isEnable = false
        val url = "https://stream7.iqilu.com/10339/article/202002/17/778c5884fa97f460dac8d90493c451de.mp4"
        player?.setUpLazy(url, true, null, null, "测试视频");
        player?.fullscreenButton?.setOnClickListener(View.OnClickListener { //直接横屏
            // ------- ！！！如果不需要旋转屏幕，可以不调用！！！-------
            // 不需要屏幕旋转，还需要设置 setNeedOrientationUtils(false)
            orientationUtils!!.resolveByClick()

            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            player?.startWindowFullscreen(this, true, true)
        })
        player?.setVideoAllCallBack(object : GSYSampleCallBack() {
            override fun onQuitFullscreen(url: String, vararg objects: Any) {
                if (orientationUtils != null) {
                    orientationUtils!!.backToProtVideo()
                }
            }
        })

    }
    override fun onBackPressed() {
        // ------- ！！！如果不需要旋转屏幕，可以不调用！！！-------
        // 不需要屏幕旋转，还需要设置 setNeedOrientationUtils(false)
        if (orientationUtils != null) {
            orientationUtils!!.backToProtVideo()
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        getCurPlay()?.onVideoPause()
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        getCurPlay()?.onVideoResume(false)
        super.onResume()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlay) {
            getCurPlay()?.release()
        }
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null) orientationUtils!!.releaseListener()
    }
    private fun getCurPlay(): GSYVideoPlayer? {
        return if (player?.fullWindowPlayer != null) {
            player?.fullWindowPlayer
        } else player
    }

    /**
     * orientationUtils 和  binding.detailPlayer.onConfigurationChanged 方法是用于触发屏幕旋转的
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            player?.onConfigurationChanged(
                this,
                newConfig,
                orientationUtils,
                true,
                true
            )
        }
    }

}