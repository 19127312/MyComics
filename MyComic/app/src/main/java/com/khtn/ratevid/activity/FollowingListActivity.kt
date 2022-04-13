package com.khtn.ratevid.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.khtn.ratevid.FirebaseUtil
import com.khtn.ratevid.R
import com.khtn.ratevid.adapter.ComicAdapter
import com.khtn.ratevid.adapter.ComicFollowAdapter
import com.khtn.ratevid.adapter.PicListAdapter
import com.khtn.ratevid.adminScreen.DetailComicAdminActivity
import com.khtn.ratevid.model.comicItem
import com.khtn.ratevid.model.userItem

class FollowingListActivity : AppCompatActivity() {
    var comicArray= ArrayList<comicItem>()
    private lateinit var adapter: ComicFollowAdapter
    lateinit var customListView: RecyclerView
    lateinit var curUser: userItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following_list)
        val intent = intent
        curUser=intent.getSerializableExtra("user") as userItem
        loadData()
        setupLayout()
        comicClick()
    }

    private fun comicClick() {
        var intent: Intent
        adapter.setOnItemClickListener(object: ComicFollowAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {


                intent = Intent(this@FollowingListActivity, DetailComicActivity::class.java)
                intent.putExtra("item", comicArray[position])
                intent.putExtra("user", curUser)


                startActivity(intent)
            }
        })
    }

    private fun loadData() {
        FirebaseUtil.getFollowComic(curUser?.UID!!,object :FirebaseUtil.FirebaseCallbackComicItem{
            override fun onCallback(arrayComicItem: ArrayList<comicItem>) {
                comicArray.clear()
                comicArray.addAll(arrayComicItem)
                heap_sort(comicArray)
                adapter.notifyDataSetChanged()
            }

        })

    }
    fun swap(A: ArrayList<comicItem>?, i: Int, j: Int) {
        var temp = A?.get(i)
        A?.set(i, A?.get(j))
        if (temp != null) {
            A?.set(j, temp)
        }
    }

    fun max_heapify(A: ArrayList<comicItem>?,heapSize:Int, i: Int) {
        var l = 2 * i;
        var r = 2 * i+1;
        var largest: Int;

        if ((l <= heapSize - 1) && (A?.get(l)?.updatedTime!! < A?.get(i).updatedTime!!)) {
            largest = l;
        } else
            largest = i

        if ((r <= heapSize - 1) && (A?.get(r)?.updatedTime!! < A?.get(l)?.updatedTime!!)) {
            largest = r
        }

        if (largest != i) {
            swap(A, i, largest);
            max_heapify(A,heapSize, largest);
        }
    }

    fun heap_sort(A: ArrayList<comicItem>?) {
        var heapSize = A?.size
        if (heapSize != null) {
            for (i in heapSize / 2 downTo 0) {
                if (heapSize != null) {
                    max_heapify(A,heapSize, i)
                }
            }
        }
        if (A != null) {
            for (i in A.size - 1 downTo 1) {
                swap(A, i, 0)
                if (heapSize != null) {
                    heapSize = heapSize - 1
                }
                if (heapSize != null) {
                    max_heapify(A,heapSize, 0)
                }

            }
        }
        adapter.notifyDataSetChanged()
    }
    private fun setupLayout() {
        adapter = ComicFollowAdapter(curUser,this,comicArray)
        customListView = findViewById<RecyclerView>(R.id.comicListFollowRV)
        customListView?.adapter = adapter
        customListView?.layoutManager = GridLayoutManager(this,2)
    }
}