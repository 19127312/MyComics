package com.khtn.ratevid.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.khtn.ratevid.R
import com.khtn.ratevid.model.comicItem
import com.khtn.ratevid.model.picItem
import com.khtn.ratevid.model.userItem
import kotlinx.android.synthetic.main.activity_detail_comic.*

class ComicFollowAdapter(var user:userItem,var context: Context, var items:ArrayList<comicItem>?) :
    RecyclerView.Adapter<ComicFollowAdapter.HolderView>(){

    lateinit var ViewGroup: ViewGroup


    class HolderView(itemView: View,listener: onItemClickListener): RecyclerView.ViewHolder(itemView){

        var img: ImageView =itemView.findViewById(R.id.thumbnailImgFollow)
        var name: TextView =itemView.findViewById(R.id.comicNameFollowTV)
        var delete: ImageView=itemView.findViewById(R.id.deleteFollowBtn)
        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }

    }
    private lateinit var mListenr: onItemClickListener

    interface  onItemClickListener{
        fun onItemClick(position: Int){

        }
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListenr=listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicFollowAdapter.HolderView {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.item_comic_follow, parent, false)
        return HolderView(contactView,mListenr)
    }



    override fun getItemCount(): Int {
        return items!!.size
    }

    override fun onBindViewHolder(holder: HolderView, position: Int) {
        val item=items!![position]

        var name:String? = item.name
        var img: String? =item.thumbnail

        Glide.with(context).load(img).into(holder.img)
        holder.name.text=name

        holder.delete.setOnClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Confirm")
            builder.setMessage("Are you sure to unfollow this manga?")

            builder.setPositiveButton("YES") { dialog, which -> // remove the the manga and close the dialog

                var followComic = FirebaseDatabase.getInstance().getReference("profile/${user.UID}/followComic")
                followComic!!.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        // If the followComic is existed
                        if (snapshot.exists()) {
                            followComic.child("${item.id}")!!
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    //If this user has already follow this manga --> delete this manga from followed manga
                                    override fun onDataChange(snapshot1: DataSnapshot) {
                                        if (snapshot1.exists()) {
                                            followComic.child("${item.id}")!!.removeValue()
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("No need")
                                    }
                                })
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("No need")
                    }
                })
                dialog.dismiss()

            }


            builder.setNegativeButton("NO") { dialog, which -> // Do nothing
                dialog.dismiss()
            }

            val alert = builder.create()
            alert.show()

        }
    }
}