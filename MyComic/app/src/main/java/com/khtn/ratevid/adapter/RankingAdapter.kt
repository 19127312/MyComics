package com.khtn.ratevid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.khtn.ratevid.R
import com.khtn.ratevid.model.comicItem

class RankingAdapter( var context:Context,var comics:ArrayList<comicItem>?) :
    RecyclerView.Adapter<RankingAdapter.HolderVideo>(){

    lateinit var ViewGroup: ViewGroup


    class HolderVideo(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){


        var number: TextView =itemView.findViewById(R.id.rankNumberTV)
        var nameComic: TextView =itemView.findViewById(R.id.nameTV)
        var layout: ConstraintLayout=itemView.findViewById(R.id.rankingBackground)

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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingAdapter.HolderVideo {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_ranking,parent,false)
        ViewGroup=parent
        return RankingAdapter.HolderVideo(view,mListenr)
    }


    override fun getItemCount(): Int {
        return comics!!.size
    }

    override fun onBindViewHolder(holder: HolderVideo, position: Int) {
        val comic=comics!![position]
        val Name:String?=comic.name
        var pos=position+1
        if(pos==1){
            holder.layout.background= ContextCompat.getDrawable(context, R.drawable.bd_rank1)
        }
        if(pos==2){
            holder.layout.background= ContextCompat.getDrawable(context, R.drawable.bd_rank2)

        }
        holder.number.text=pos.toString()
        holder.nameComic.text=Name
    }
}