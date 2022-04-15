package com.khtn.ratevid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.khtn.ratevid.FirebaseUtil
import com.khtn.ratevid.R
import com.khtn.ratevid.adapter.PicAdapter
import com.khtn.ratevid.adapter.PicListAdapter
import com.khtn.ratevid.model.picItem
import kotlinx.android.synthetic.main.activity_pic_list.*
import kotlin.properties.Delegates

class PicListActivity : AppCompatActivity() {
    var comicID=""
    var chapterNumber by Delegates.notNull<Int>()
    var lastChapter=0
    lateinit var imgsList : ArrayList<picItem>
    lateinit var adapter: PicListAdapter
    lateinit var customListView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pic_list)

        val intent=intent
        comicID= intent.getStringExtra("comicID").toString()
        chapterNumber= intent.getIntExtra("chapterNumber",0)
        lastChapter=intent.getIntExtra("lastChapter",0)
        imgsList= ArrayList<picItem>()
        nextClick()
        previousClick()
        getPic()
        setupRecyler()
        hideBtn(chapterNumber)
        setupView(chapterNumber)
    }

    private fun setupView(chapterNumber: Int) {
        chapterNumberTV.text="Chapter ${chapterNumber}"
    }

    private fun hideBtn(chapterNumber: Int) {
        if(chapterNumber==1){
            preBtn.visibility= View.INVISIBLE
        }else{
            preBtn.visibility= View.VISIBLE

        }

        if(chapterNumber==lastChapter-1){
            nextBtn.visibility= View.INVISIBLE
        }else{
            nextBtn.visibility= View.VISIBLE

        }
    }

    private fun nextClick() {
        Log.d("MyScreen",chapterNumber.toString())

        nextBtn.setOnClickListener {
            chapterNumber+=1
            FirebaseUtil.readPicInChapter(comicID,chapterNumber,object: FirebaseUtil.FirebaseCallbackPicList{
                override fun onCallback(arrayPic: ArrayList<picItem>) {
                    imgsList.clear()
                    imgsList.addAll(arrayPic)
                    adapter.notifyDataSetChanged()

                }

            })
            hideBtn(chapterNumber)
            setupView(chapterNumber)
        }

    }

    private fun previousClick() {
        preBtn.setOnClickListener {
            chapterNumber-=1
            FirebaseUtil.readPicInChapter(comicID,chapterNumber,object: FirebaseUtil.FirebaseCallbackPicList{
                override fun onCallback(arrayPic: ArrayList<picItem>) {
                    imgsList.clear()
                    imgsList.addAll(arrayPic)
                    adapter.notifyDataSetChanged()

                }

            })
            hideBtn(chapterNumber)
            setupView(chapterNumber)
        }

    }

    private fun setupRecyler() {
        adapter = PicListAdapter(this,imgsList)
        customListView = findViewById<RecyclerView>(R.id.picRV)
        customListView?.adapter = adapter
        customListView?.layoutManager = LinearLayoutManager(this)
    }

    private fun getPic() {
        FirebaseUtil.readPicInChapter(comicID,chapterNumber,object: FirebaseUtil.FirebaseCallbackPicList{
            override fun onCallback(arrayPic: ArrayList<picItem>) {
                imgsList.clear()
                imgsList.addAll(arrayPic)
                adapter.notifyDataSetChanged()

            }

        })
    }
}