package com.khtn.ratevid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.khtn.ratevid.R

class ChapterAdapter(var chapters:ArrayList<Int>): RecyclerView.Adapter<ChapterAdapter.ViewHolder>(){
    class ViewHolder (item: View, listener: onItemClickListener): RecyclerView.ViewHolder(item){
        val num=item.findViewById<TextView>(R.id.chapterNumberAdminTV)

        init{
            item.setOnClickListener{
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.item_chapter_admin, parent, false)
        return ViewHolder(contactView,mListenr)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chap=chapters[position]
        val number=holder.num


        number.setText("Chapter "+chap.toString())


    }

    override fun getItemCount(): Int {
        return chapters.size
    }

}
