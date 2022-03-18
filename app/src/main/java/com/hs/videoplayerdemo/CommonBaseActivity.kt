package com.hs.videoplayerdemo

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

abstract class CommonBaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performingView()
        commonCreate(savedInstanceState)
        transparentStatusBar()
        initView()
        initListener()
        initData()
    }

    override fun onDestroy() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        super.onDestroy()
    }

    abstract fun commonCreate(savedInstanceState: Bundle?)

    /** 执行 setContentView 之前 **/
    open fun performingView() { }
    /** 初始化 View */
    open fun initView() { }
    /** 添加监听方法 */
    open fun initListener() { }
    /** 初始化数据 */
    open fun initData() { }

    /**
     * 设置透明状态栏
     */
    open fun transparentStatusBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory();
    }
}