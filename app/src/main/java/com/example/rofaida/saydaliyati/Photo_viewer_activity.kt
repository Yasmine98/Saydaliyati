package com.example.rofaida.saydaliyati

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_photo_viewer_activity.*

class Photo_viewer_activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_viewer_activity)
        val imageUrl = intent.getStringExtra("url")
        Glide.with(applicationContext).load(imageUrl).into(fullscreen_content)
    }
}
