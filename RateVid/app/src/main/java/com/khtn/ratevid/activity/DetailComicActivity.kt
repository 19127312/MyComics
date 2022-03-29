package com.khtn.ratevid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.khtn.ratevid.R
import com.khtn.ratevid.model.comicItem
import kotlinx.android.synthetic.main.activity_detail_comic.*
import kotlinx.android.synthetic.main.activity_detail_comic_admin.*

class DetailComicActivity : AppCompatActivity() {
     lateinit var comic: comicItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_comic)
        val intent = intent
        comic= intent.getSerializableExtra("item") as comicItem


        setupLayout()
    }

    private fun setupLayout() {
        Glide.with(this).load(comic.thumbnail).into(imageView)
        //Name
        nameComicTV.setText(comic.name)
        //Author
        authorTV.setText(comic.author)

        //Description
        descriptionTV.setText(comic.description)
    }
}