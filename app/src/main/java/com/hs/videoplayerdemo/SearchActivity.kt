package com.hs.videoplayerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hs.videoplayerdemo.databinding.ActivityBannerBinding
import com.hs.videoplayerdemo.databinding.ActivitySearchBinding
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(bind.root)
        //使用默认的图片适配器
        bind.btnSwitch.setOnClickListener {
            bind.searchView.startAnimator()
        }

    }
}