package com.example.travelblog

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat

data class BlogData(val data: List<Blog>)
private val dateFormat = SimpleDateFormat("MMMM dd, yyyy")
@Parcelize
data class Blog(
    val id: String,
    var author: Author,
    val title: String,
    val date: String,
    val image: String,
    val description: String,
    val views: Int,
    val rating: Float
) : Parcelable {

    fun getDateMillis() = dateFormat.parse(date).time
    fun getImageUrl() = BlogHttpClient.BASE_URL + BlogHttpClient.PATH + image
}

@Parcelize
data class Author(val name: String, val avatar: String) : Parcelable {
    fun getAvatarUrl() = BlogHttpClient.BASE_URL + BlogHttpClient.PATH + avatar
}