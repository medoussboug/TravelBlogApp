package com.example.travelblog

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.travelblog.databinding.ActivityBlogDetailsBinding
import com.google.android.material.snackbar.Snackbar

class BlogDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBlogDetailsBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlogDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageMain.setOnClickListener { finish() }

        intent.extras?.getParcelable<Blog>(EXTRAS_BLOG)?.let { blog ->
            showData(blog)
        }
    }

    private fun loadData() {
        BlogHttpClient.loadArticles(
            onSuccess = { data: List<Blog> ->
                runOnUiThread { showData(data[0]) }
            },
            onError = {
                runOnUiThread { showErrorSnackbar() }
            }
        )
    }

    private fun showErrorSnackbar() {
        Snackbar.make(
            binding.root,
            "Error during loading blog articles",
            Snackbar.LENGTH_INDEFINITE
        )
            .run {
                setActionTextColor(ContextCompat.getColor(context, R.color.orange500))
                setAction("Retry") {
                    loadData()
                    dismiss()
                }
            }.show()
    }

    private fun showData(blog: Blog) {
        binding.progressBar.visibility = View.GONE
        binding.textTitle.text = blog.title
        binding.textDate.text = blog.date
        binding.textAuthor.text = blog.author.name
        binding.textRating.text = blog.rating.toString()
        binding.textViews.text = String.format("(%d views)", blog.views)
        binding.textDescription.text =
            HtmlCompat.fromHtml(blog.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.ratingBar.rating = blog.rating
        binding.ratingBar.visibility = View.VISIBLE

        Glide.with(this)
            .load(blog.image)
            .into(binding.imageMain)

        Glide.with(this)
            .load(blog.author.avatar)
            .transform(CircleCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageAvatar)
    }

    companion object {
        const val EXTRAS_BLOG = "EXTRAS_BLOG"
        fun start(activity: Activity, blog: Blog) {
            val intent = Intent(activity, BlogDetailsActivity::class.java)
            intent.putExtra(EXTRAS_BLOG, blog)
            activity.startActivity(intent)
        }
    }
}

