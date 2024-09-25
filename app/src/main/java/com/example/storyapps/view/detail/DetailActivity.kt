package com.example.storyapps.view.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.storyapps.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_PHOTO = "extra_img"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_NAME = "extra_name"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        val photoUrl = intent.getStringExtra(EXTRA_PHOTO)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val name = intent.getStringExtra(EXTRA_NAME)

        binding.tvName.text = name
        binding.tvDesc.text = desc
        Glide.with(this).load(photoUrl).into(binding.ivStory)

        binding.ivIcBack.setOnClickListener {
            finish()
        }
    }
}