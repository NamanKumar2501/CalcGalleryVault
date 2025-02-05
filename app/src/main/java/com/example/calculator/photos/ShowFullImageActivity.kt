package com.example.calculator.photos

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calculator.R
import com.squareup.picasso.Picasso

import com.github.chrisbanes.photoview.PhotoView

class ShowFullImageActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_full_image)

        val currentTouchImaage = intent.getStringExtra("image")
        val image = findViewById<PhotoView>(R.id.full_image_view)

        Picasso
            .get()
            .load(currentTouchImaage)
            .into(image)
    }
}
