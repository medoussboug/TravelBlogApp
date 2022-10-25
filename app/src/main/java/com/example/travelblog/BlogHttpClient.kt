package com.example.travelblog

import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.Executors

object BlogHttpClient {
    private const val BASE_URL =
        "https://bitbucket.org/dmytrodanylyk/travel-blog-resources/raw/"
    private const val BLOG_ARTICLES_URL =
        BASE_URL + "8550ef2064bf14fcf3b9ff322287a2e056c7e153/blog_articles.json"
    private val executor = Executors.newFixedThreadPool(4)
    private val client = OkHttpClient()
    private val gson = Gson()
    fun loadArticles(onSuccess: (List<Blog>) -> Unit, onError: () -> Unit) {
        val request = Request.Builder()
            .get()
            .url(BLOG_ARTICLES_URL)
            .build()
        executor.execute {
            runCatching {
                val response: Response = client.newCall(request).execute()
                response.body?.string()?.let { json ->
                    gson.fromJson(json, BlogData::class.java)?.let { blogData ->
                        return@runCatching blogData.data
                    }
                }
            }.onFailure { e: Throwable ->
                Log.e(this.javaClass.name, "request didn't go through", e)
                onError()

            }.onSuccess { value: List<Blog>? ->
                onSuccess(value ?: emptyList())
            }
        }
    }
}
