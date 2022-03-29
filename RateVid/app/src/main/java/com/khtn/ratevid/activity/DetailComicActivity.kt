package com.khtn.ratevid.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.khtn.ratevid.R
import com.khtn.ratevid.adapter.ChapterAdapter
import com.khtn.ratevid.adminScreen.AddChapter
import com.khtn.ratevid.model.comicItem
import com.khtn.ratevid.model.userItem
import kotlinx.android.synthetic.main.activity_detail_comic.*
import kotlinx.android.synthetic.main.activity_detail_comic_admin.*

class DetailComicActivity : AppCompatActivity() {
    lateinit var comic: comicItem
    lateinit var user: userItem

    var chapters = ArrayList<Int>()
    lateinit var adapter: ChapterAdapter
    lateinit var customListView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_comic)
        val intent = intent
        comic = intent.getSerializableExtra("item") as comicItem
        user=intent.getSerializableExtra("user") as userItem
        setupLayout()
        likeClick(comic)
        followClick(comic)
        commentClick(comic)
    }

    private fun commentClick(comic: comicItem) {

    }

    private fun followClick(comic: comicItem) {
        FollowBtn.setOnClickListener {
            var followComic =
                FirebaseDatabase.getInstance().getReference("profile/${user.UID}/followComic")
            followComic!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // If the followComic is existed
                    if (snapshot.exists()) {
                        followComic.child("${comic.id}")!!
                            .addListenerForSingleValueEvent(object : ValueEventListener {

                                //If this user has already follow this manga --> delete this manga from followed manga
                                override fun onDataChange(snapshot1: DataSnapshot) {
                                    if (snapshot1.exists()) {
                                        followComic.child("${comic.id}")!!.removeValue()
                                        FollowBtn.text = "FOLLOW"
                                    }
                                    // If this user want to follow this manga
                                    else {
                                        followComic!!.child("${comic.id}").setValue("${comic.id}")

                                        FollowBtn.text = "UNFOLLOW"
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("No need")
                                }
                            })
                    }
                    //If the followComic is not existed -->Init the folder and add new manga to this
                    else {
                        //Set who like this manga
                        FirebaseDatabase.getInstance().getReference("profile/${user.UID}")
                            .child("followComic/${comic.id}").setValue("${comic.id}")
                        FollowBtn.text = "UNFOLLOW"

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("No need")
                }
            })
        }

    }

    private fun likeClick(comic: comicItem) {
        likeBtn.setOnClickListener {
            var newLike = comic.likeNumber
            var refLike = FirebaseDatabase.getInstance()
                .getReference("comic/${comic.id}/likeNumber") // LikeCount
            var refLikePerson =
                FirebaseDatabase.getInstance().getReference("comic/${comic.id}/likePerson")
            refLikePerson!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // If the path/likePerson is existed
                    if (snapshot.exists()) {

                        refLikePerson.child("${user.UID}")!!
                            .addListenerForSingleValueEvent(object : ValueEventListener {

                                //If this user has already liked this manga --> delete this user from likePerson, decrease the counter
                                override fun onDataChange(snapshot1: DataSnapshot) {
                                    if (snapshot1.exists()) {
                                        refLikePerson.child("${user.UID}")!!.removeValue()
                                        comic.likeNumber = comic.likeNumber?.minus(1)
                                        refLike.setValue(comic.likeNumber)
                                        likenumberTV.text = comic.likeNumber.toString()
                                        likeBtn.setImageResource(R.drawable.ic_favorite_unfill)

                                    }
                                    // If this user like this manga
                                    else {
                                        refLikePerson!!.child("${user.UID}").setValue(1)
                                        comic.likeNumber = comic.likeNumber?.plus(1)
                                        refLike.setValue(comic.likeNumber)
                                        likenumberTV.text = comic.likeNumber.toString()
                                        likeBtn.setImageResource(R.drawable.ic_favorite_fill)

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("No need")
                                }
                            })

                    }
                    //If the path/likePerson is not existed -->The first user like this manga
                    else {
                        //Set who like this manga
                        FirebaseDatabase.getInstance().getReference("comic/${comic.id}")
                            .child("likePerson/${user.UID}").setValue(1)
                        //Increase the counter by one
                        comic.likeNumber = comic.likeNumber?.plus(1)
                        refLike.setValue(comic.likeNumber)
                        likenumberTV.text = comic.likeNumber.toString()
                        likeBtn.setImageResource(R.drawable.ic_favorite_fill)

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("No need")
                }
            })
        }

    }

    private fun setupLayout() {
        Glide.with(this).load(comic.thumbnail).into(imageView)
        //Name
        nameComicTV.setText(comic.name)
        //Author
        authorTV.setText(comic.author)

        //Description
        descriptionTV.setText(comic.description)
        //Like
        likenumberTV.text = comic.likeNumber.toString()
        loadStatusLike()
        //Status
        loadStatusFollow()

        //RecycleView
        loadChapter()
        adapter = ChapterAdapter(chapters)

        customListView = findViewById<RecyclerView>(R.id.chapterRV) as RecyclerView
        customListView!!.adapter = adapter
        adapter.setOnItemClickListener(object : ChapterAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@DetailComicActivity, PicListActivity::class.java)
                intent.putExtra("comicID", comic.id)
                intent.putExtra("chapterNumber", position + 1)
                startActivity(intent)

            }
        })
        customListView.layoutManager = LinearLayoutManager(this)
    }
    fun loadStatusLike(){
        var refLikePerson = FirebaseDatabase.getInstance().getReference("comic/${comic.id}/likePerson")
        refLikePerson.child("${user.UID}")!!
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // If this user like this manga
                    if (snapshot.exists()) {
                        likeBtn.setImageResource(R.drawable.ic_favorite_fill)
                    }
                    // If this user did not like this manga
                    else {
                     //Do nothing
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("No need")
                }
            })
    }
    fun loadStatusFollow(){
        var refLikePerson = FirebaseDatabase.getInstance().getReference("profile/${user.UID}/followComic")
        refLikePerson.child("${comic.id}")!!
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        FollowBtn.text = "UNFOLLOW"
                    }
                    else {
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("No need")
                }
            })
    }
    private fun loadChapter() {
        FirebaseDatabase.getInstance().getReference("comic").child(comic.id.toString())
            .child("lastestChapter").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        chapters.clear()
                        adapter.notifyDataSetChanged()
                        var number = snapshot.getValue(Int::class.java)
                        for (i in 1..number!! - 1) {
                            chapters.add(i)

                        }
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }
}