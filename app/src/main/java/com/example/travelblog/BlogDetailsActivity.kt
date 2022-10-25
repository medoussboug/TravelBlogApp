package com.example.travelblog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelblog.databinding.ActivityBlogDetailsBinding

class BlogDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBlogDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlogDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}