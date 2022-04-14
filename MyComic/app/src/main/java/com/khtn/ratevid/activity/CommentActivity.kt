package com.khtn.ratevid.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.khtn.ratevid.FirebaseUtil
import com.khtn.ratevid.R
import com.khtn.ratevid.adapter.CommentAdapter
import com.khtn.ratevid.model.commentItem
import kotlinx.android.synthetic.main.activity_comment.*

class CommentActivity : AppCompatActivity() {
    lateinit var comicID: String
    lateinit var userID: String

    var ListComment= ArrayList<commentItem>()
    private lateinit var adapterComment: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        var intent: Intent = getIntent()
        comicID = intent.getStringExtra("comicID").toString()
        userID = intent.getStringExtra("userUID").toString()
        loadComment()
        addClick()
        setupLayout()
    }

    private fun setupLayout() {
        adapterComment = CommentAdapter(this@CommentActivity, ListComment, comicID, userID)
        if(!recycle_comment.isComputingLayout){
         recycle_comment.adapter = adapterComment
            recycle_comment.layoutManager=LinearLayoutManager(this)
        }
    }

    private fun addClick() {
        post.setOnClickListener {
            if (TextUtils.isEmpty(add_comment.text.toString())){
                add_comment.error = "Please enter something"
                return@setOnClickListener
            } else {
                addComment()
            }
        }
    }

    private fun addComment() {
        var reference = FirebaseDatabase.getInstance().getReference("comic/$comicID/comment")
        val time = System.currentTimeMillis().toString()
        val dic = HashMap<String,Any>()
        dic["userid"] = userID
        dic["content"] = add_comment.text.toString()
        dic["timestamp"] = time
        reference.child(time).setValue(dic)
        add_comment.text.clear()
        adapterComment.notifyItemInserted(ListComment.size)

    }

    private fun loadComment() {
        FirebaseUtil.readCommentArray(comicID,object: FirebaseUtil.FirebaseCallbackCommentList{
            override fun onCallback(arrayComment: ArrayList<commentItem>) {
                ListComment.clear()
                ListComment.addAll(arrayComment)
                adapterComment.notifyDataSetChanged()

            }
        })
    }
}