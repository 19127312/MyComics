package com.khtn.ratevid.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.khtn.ratevid.R
import com.khtn.ratevid.activity.DetailComicActivity
import com.khtn.ratevid.adminScreen.AddComic
import com.khtn.ratevid.adapter.ComicAdapter
import com.khtn.ratevid.adminScreen.DetailComicAdminActivity
import com.khtn.ratevid.model.comicItem
import com.khtn.ratevid.model.userItem
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment (user: userItem): Fragment() {
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    lateinit var user: FirebaseUser
    var curUser=user
    var comicArray= ArrayList<comicItem>()
    private lateinit var adapter: ComicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_home, container, false)
        initRecyclerView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
        AddImage()
        loadDataComic()
        itemComicClick()
    }

    private fun itemComicClick() {
        var intent: Intent
        adapter.setOnItemClickListener(object: ComicAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                if(curUser.Type=="Admin"){
                     intent= Intent(context,DetailComicAdminActivity::class.java)
                     intent.putExtra("item",comicArray[position])
                }
                else{
                    intent= Intent(context,DetailComicActivity::class.java)
                    intent.putExtra("item",comicArray[position])
                    intent.putExtra("user",curUser)

                }
                startActivity(intent)
            }
        })    }

    private fun loadDataComic() {

            val ref= FirebaseDatabase.getInstance().getReference("comic")
            ref.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //Xoa list trc khi them vao moi lan vao app
                    comicArray.clear()
                    for (item in snapshot.children){
                        val modelComic = item.getValue(comicItem::class.java)
                        comicArray.add(modelComic!!)
                    }
                    adapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    private fun setupLayout() {
        if(curUser.Type!="Admin"){
            AddButton.visibility=View.INVISIBLE
        }
    }

    private fun initRecyclerView(view: View?) {
        val recyclerView= view?.findViewById<RecyclerView>(R.id.homeRecycleView)
        if (recyclerView != null) {
            recyclerView.layoutManager= GridLayoutManager(activity,2)
        }
        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(context,
            DividerItemDecoration.VERTICAL)
        if (recyclerView != null) {
            recyclerView.addItemDecoration(itemDecoration)
        }
        adapter= context?.let { ComicAdapter(it,comicArray) }!!
        if (recyclerView != null) {
            recyclerView.adapter=adapter
        }



    }
    fun AddImage(){
        AddButton.setOnClickListener {
            val intent= Intent(context, AddComic::class.java)
            startActivity(intent)
        }
    }

}