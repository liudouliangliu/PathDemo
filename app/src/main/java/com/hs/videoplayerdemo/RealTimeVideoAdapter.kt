package com.hs.videoplayerdemo

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.widget.TextView
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

class RealTimeVideoAdapter: BasePagedAdapter<RecordsBean>(R.layout.item_video) {
    var mContext: Context? = null
    private var curPlayer: StandardGSYVideoPlayer? = null
    private var itemPlayer: StandardGSYVideoPlayer? = null
    protected var isPlay = false
    val TAG = "RealTimeVideoAdapter"
    protected var isFull = false
    protected var orientationUtils: OrientationUtils? = null
    override fun bindViewHolder(
        holder: ViewHolder,
        item: RecordsBean,
        position: Int
    ) {
        val player = holder.itemView.findViewById<SampleCoverVideo>(R.id.player)
        player.backButton.visibility = View.GONE

        Log.d(TAG, "bindViewHolder rtmp $position : ${item.rtmp}")
        item.rtmp?.let {
            player.setUpLazy(it, true, null, null, item.name);
        }

        holder.itemView.findViewById<TextView>(R.id.tv_history).setOnClickListener {
            mContext?.let {
                //历史视频
//                Router.startTransfer(item.companyCode)
//                VideoHistoryActivity.start(mContext as Activity,item.channelId,item.deviceId,item.name)
            }
        }
        holder.setText(R.id.tv_history,"${item.name} - ${mContext?.resources?.getString(R.string.video_history)}")
        player.fullscreenButton.setOnClickListener{
                resolveFullBtn(player);
            }

        player.isRotateViewAuto = !getListNeedAutoLand()
        player.isLockLand = !getListNeedAutoLand()
        player.setPlayTag(TAG);
        //holder.gsyVideoPlayer.c(true);
        player.isReleaseWhenLossAudio = false
        player.isAutoFullWithSize = true
        player.isShowFullAnimation = !getListNeedAutoLand()
        player.setIsTouchWiget(false)
        //循环
        //holder.gsyVideoPlayer.setLooping(true);
        player.isNeedLockFull = true


        //holder.gsyVideoPlayer.setSpeed(2);
        player.playPosition = position

        player.setVideoAllCallBack(object : GSYSampleCallBack() {
            override fun onClickStartIcon(url: String, vararg objects: Any) {
                super.onClickStartIcon(url, objects)
            }

            override fun onPrepared(url: String, vararg objects: Any) {
                super.onPrepared(url, objects)
                Debuger.printfLog("onPrepared")
                val full: Boolean =
                    player.currentPlayer.isIfCurrentIsFullscreen
                if (!player.currentPlayer.isIfCurrentIsFullscreen) {
                    GSYVideoManager.instance().isNeedMute = true
                }
                if (player.currentPlayer.isIfCurrentIsFullscreen) {
                    GSYVideoManager.instance().setLastListener(player)
                }
                curPlayer = objects[1] as StandardGSYVideoPlayer
                itemPlayer = player
                isPlay = true
                if (getListNeedAutoLand()) {
                    //重力全屏工具类
                    initOrientationUtils(player, full)
                    onPrepared()
                }
            }

            override fun onQuitFullscreen(url: String, vararg objects: Any) {
                super.onQuitFullscreen(url, *objects)
                isFull = false
                GSYVideoManager.instance().isNeedMute = true
                if (getListNeedAutoLand()) {
                    onQuitFullscreen()
                }
            }

            override fun onEnterFullscreen(url: String, vararg objects: Any) {
                super.onEnterFullscreen(url, *objects)
                GSYVideoManager.instance().isNeedMute = false
                isFull = true
                player.currentPlayer.titleTextView.text = objects[0] as String
            }

            override fun onAutoComplete(url: String, vararg objects: Any) {
                super.onAutoComplete(url, *objects)
                curPlayer = null
                itemPlayer = null
                isPlay = false
                isFull = false
                if (getListNeedAutoLand()) {
                    onAutoComplete()
                }
            }
        })

    }

    /**
     * 全屏幕按键处理
     */
    private fun resolveFullBtn(standardGSYVideoPlayer: StandardGSYVideoPlayer) {
        if (getListNeedAutoLand() && orientationUtils != null) {
            resolveFull()
        }
        standardGSYVideoPlayer.startWindowFullscreen(mContext, false, true)
    }
    /**************************支持全屏重力全屏的部分 */
    /**
     * 列表时是否需要支持重力旋转
     *
     * @return 返回true为支持列表重力全屏
     */
    fun getListNeedAutoLand(): Boolean {
        return true
    }

    private fun resolveFull() {
        if (getListNeedAutoLand() && orientationUtils != null) {
            //直接横屏
            // ------- ！！！如果不需要旋转屏幕，可以不调用！！！-------
            // 不需要屏幕旋转，还需要设置 setNeedOrientationUtils(false)
            orientationUtils!!.resolveByClick()
        }
    }

    private fun onQuitFullscreen() {
        // ------- ！！！如果不需要旋转屏幕，可以不调用！！！-------
        // 不需要屏幕旋转，还需要设置 setNeedOrientationUtils(false)
        if (orientationUtils != null) {
            orientationUtils!!.backToProtVideo()
        }
    }

    fun onAutoComplete() {
        if (orientationUtils != null) {
            orientationUtils!!.isEnable = false
            orientationUtils!!.releaseListener()
            orientationUtils = null
        }
        isPlay = false
    }

    private fun initOrientationUtils(
        standardGSYVideoPlayer: StandardGSYVideoPlayer,
        full: Boolean
    ) {
        orientationUtils = OrientationUtils(mContext as Activity?, standardGSYVideoPlayer)
        //是否需要跟随系统旋转设置
        //orientationUtils.setRotateWithSystem(false);
        orientationUtils!!.isEnable = false
        orientationUtils!!.isLand = if (full) 1 else 0
    }

    fun onPrepared() {
        if (orientationUtils == null) {
            return
        }
        //开始播放了才能旋转和全屏
        orientationUtils!!.isEnable = true
    }

    /**
     * orientationUtils 和  detailPlayer.onConfigurationChanged 方法是用于触发屏幕旋转的
     */
    fun onConfigurationChanged(activity: Activity?, newConfig: Configuration?) {
        //如果旋转了就全屏
        if (isPlay && itemPlayer != null && orientationUtils != null) {
            itemPlayer?.onConfigurationChanged(activity, newConfig, orientationUtils, false, true)
        }
    }


    fun onBackPressed() {
        // ------- ！！！如果不需要旋转屏幕，可以不调用！！！-------
        // 不需要屏幕旋转，还需要设置 setNeedOrientationUtils(false)
        if (orientationUtils != null) {
            orientationUtils!!.backToProtVideo()
        }
    }

    fun onDestroy() {
        if (isPlay && curPlayer != null) {
            curPlayer?.currentPlayer?.release()
        }
        if (orientationUtils != null) {
            orientationUtils!!.releaseListener()
            orientationUtils = null
        }
    }
}