package com.hs.videoplayerdemo

import android.os.Bundle
import androidx.annotation.LayoutRes
/**
 * @author Yang Hao
 * @date 2020/11/18
 */
abstract class BaseActivity: CommonBaseActivity() {

    override fun commonCreate(savedInstanceState: Bundle?) {
        setContentView(getLayoutId())
    }

    @LayoutRes abstract fun getLayoutId(): Int
}