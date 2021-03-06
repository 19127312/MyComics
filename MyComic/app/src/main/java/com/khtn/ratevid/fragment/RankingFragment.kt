package com.khtn.ratevid.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khtn.ratevid.FirebaseUtil
import com.khtn.ratevid.R
import com.khtn.ratevid.activity.DetailComicActivity
import com.khtn.ratevid.adapter.RankingAdapter
import com.khtn.ratevid.adminScreen.DetailComicAdminActivity
import com.khtn.ratevid.model.comicItem
import com.khtn.ratevid.model.userItem


class RankingFragment (user: userItem): Fragment() {
    var curUser=user

    var comicArray= ArrayList<comicItem>()

    private lateinit var adapter: RankingAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_ranking, container, false)
        initRecyclerView(view)
        return view
    }

    private fun initRecyclerView(view: View?) {
        val recyclerView= view?.findViewById<RecyclerView>(R.id.rankingRV)
        if (recyclerView != null) {
            recyclerView.layoutManager= LinearLayoutManager(context)
        }

        adapter= context?.let { RankingAdapter(it,comicArray) }!!
        if (recyclerView != null) {
            recyclerView.adapter=adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDataComic()
        itemComicClick()
    }
    private fun loadDataComic() {
        FirebaseUtil.readComicData(object: FirebaseUtil.FirebaseCallbackComicItem{
            override fun onCallback(arrayComicItem: ArrayList<comicItem>) {
                comicArray.clear()
                comicArray.addAll(arrayComicItem)
                heap_sort(comicArray)
                if(comicArray.size>10){
                    var temp= ArrayList<comicItem>(comicArray.subList(0, 9))
                    comicArray.clear()
                    comicArray.addAll(temp)
                }
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun itemComicClick() {
        var intent: Intent
        adapter.setOnItemClickListener(object: RankingAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                if(curUser.Type=="Admin"){
                    intent= Intent(context, DetailComicAdminActivity::class.java)
                    intent.putExtra("item",comicArray[position])
                }
                else{
                    intent= Intent(context, DetailComicActivity::class.java)
                    intent.putExtra("item",comicArray[position])
                    intent.putExtra("user",curUser)

                }
                startActivity(intent)
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

        if ((l <= heapSize - 1) && (A?.get(l)?.likeNumber!! < A?.get(i).likeNumber!!)) {
            largest = l;
        } else
            largest = i

        if ((r <= heapSize - 1) && (A?.get(r)?.likeNumber!! < A?.get(l)?.likeNumber!!)) {
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

}