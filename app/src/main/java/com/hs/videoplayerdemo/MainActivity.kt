package com.hs.videoplayerdemo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_list).setOnClickListener {
            startActivity(Intent(this,ListActivity::class.java))
        }

        findViewById<Button>(R.id.btn_sample).setOnClickListener {
            startActivity(Intent(this,SampleActivity::class.java))
        }

        findViewById<Button>(R.id.btn_spanner).setOnClickListener {
            startActivity(Intent(this,SpannerActivity::class.java))
        }
    }


}