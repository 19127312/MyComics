package com.khtn.ratevid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khtn.ratevid.R
import com.khtn.ratevid.model.comicItem

class comicAdapter(var context: Context, var items:ArrayList<comicItem>?) :
    RecyclerView.Adapter<comicAdapter.HolderView>(){

    lateinit var ViewGroup: ViewGroup


    class HolderView(itemView: View,listener: onItemClickListener): RecyclerView.ViewHolder(itemView){

        var img: ImageView =itemView.findViewById(R.id.thumbnailImg)
        var name: TextView =itemView.findViewById(R.id.comicNameTV)
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): comicAdapter.HolderView {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_comic,parent,false)
        ViewGroup=parent
        return comicAdapter.HolderView(view,mListenr)
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
    }
}