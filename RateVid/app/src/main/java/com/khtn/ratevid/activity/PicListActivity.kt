package com.khtn.ratevid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.khtn.ratevid.R
import com.khtn.ratevid.adapter.PicAdapter
import com.khtn.ratevid.adapter.PicListAdapter
import com.khtn.ratevid.model.picItem

class PicListActivity : AppCompatActivity() {
    var comicID=""
    var chapterNumber=0
    lateinit var imgsList : ArrayList<picItem>
    lateinit var adapter: PicListAdapter
    lateinit var customListView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pic_list)

        val intent=intent
        comicID= intent.getStringExtra("comicID").toString()
        chapterNumber= intent.getIntExtra("chapterNumber",0)
        imgsList= ArrayList<picItem>()

        getPic()
        setupRecyler()
    }

    private fun setupRecyler() {
        adapter = PicListAdapter(this,imgsList)
        customListView = findViewById<RecyclerView>(R.id.picRV)
        customListView?.adapter = adapter
        customListView?.layoutManager = LinearLayoutManager(this)
    }

    private fun getPic() {
        val ref= FirebaseDatabase.getInstance().getReference("comic").child(comicID).child("chapter").child(chapterNumber.toString())
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children){
                    val modelComic = item.getValue(picItem::class.java)
                    if (modelComic != null) {
                        imgsList.add(modelComic)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}