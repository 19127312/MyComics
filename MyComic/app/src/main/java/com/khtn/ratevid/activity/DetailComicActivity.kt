package com.khtn.ratevid.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.khtn.ratevid.FirebaseUtil
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
        descriptionTV.movementMethod = ScrollingMovementMethod()

    }

    private fun commentClick(comic: comicItem) {
        commentBtn.setOnClickListener {
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("comicID", comic.id)
            intent.putExtra("userUID", user.UID)
            startActivity(intent)
        }
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
        descriptionTV.isEnabled=false
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
                var pos= chapters.size-position;
                intent.putExtra("chapterNumber", pos)
                startActivity(intent)
            }
        })
        customListView.layoutManager = LinearLayoutManager(this)
    }
    fun loadStatusLike(){
        FirebaseUtil.getLikeStatus(user?.UID!!,comic?.id!!,object :FirebaseUtil.FirebaseCallbackUpdate{
            override fun onCallback(status: String) {
                if(status=="Yes"){
                    likeBtn.setImageResource(R.drawable.ic_favorite_fill)
                }
            }
        })

    }
    fun loadStatusFollow(){
        FirebaseUtil.getFollowStatus(user?.UID!!,comic?.id!!,object :FirebaseUtil.FirebaseCallbackUpdate{
            override fun onCallback(status: String) {
                if(status=="Yes"){
                    FollowBtn.text = "UNFOLLOW"
                }
            }
        })

    }
    private fun loadChapter() {
        FirebaseUtil.readChapters(comic, object : FirebaseUtil.FirebaseCallbackChapterList{
            override fun onCallback(arrayChapter: ArrayList<Int>) {
                chapters.clear()
                chapters.addAll(arrayChapter)
                adapter.notifyDataSetChanged()
            }
        })
    }
}