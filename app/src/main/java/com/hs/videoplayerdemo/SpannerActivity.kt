package com.hs.videoplayerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hs.videoplayerdemo.databinding.ActivityBannerBinding
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator

class SpannerActivity : AppCompatActivity() {
    var imageUrls = listOf(
        "https://img.zcool.cn/community/01b72057a7e0790000018c1bf4fce0.png",
        "https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg",
        "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg",
        "https://img.zcool.cn/community/01700557a7f42f0000018c1bd6eb23.jpg"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityBannerBinding.inflate(layoutInflater)
        setContentView(bind.root)
        //使用默认的图片适配器
        var banner = (bind.banner as Banner<String, BannerImageAdapter<String>>)
        banner.apply {
            addBannerLifecycleObserver(this@SpannerActivity)
            indicator = CircleIndicator(this@SpannerActivity)
            setAdapter(object : BannerImageAdapter<String>(imageUrls) {
                override fun onBindView(holder: BannerImageHolder, data: String, position: Int, size: Int) {
                    Glide.with(this@SpannerActivity)
                        .load(data)
                        .into(holder.imageView)
                }
            })
        }
        bind.btnSwitch.setOnClickListener {
            bind.searchView.startAnimation()
        }

    }
}