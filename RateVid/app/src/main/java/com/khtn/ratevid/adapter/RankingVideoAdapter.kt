package com.khtn.ratevid.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.R
import com.khtn.ratevid.model.ModelVideo

class RankingVideoAdapter (private var videoArrayList:ArrayList<ModelVideo>?) :
    RecyclerView.Adapter<RankingVideoAdapter.HolderVideo>(){
    var database: DatabaseReference?=null
    var userReference: DatabaseReference?=null
    var checkExist: DatabaseReference?=null
    lateinit var ViewGroup: ViewGroup


    class HolderVideo(itemView: View): RecyclerView.ViewHolder(itemView){

        //Khoi tao cac thuoc tinh tu trong row_video

//        var videoView: VideoView =itemView.findViewById(R.id.videoView)
//        var titleVideo: TextView =itemView.findViewById(R.id.titleVideo)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingVideoAdapter.HolderVideo {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.custom_dialog,parent,false)
        //val view= LayoutInflater.from(parent.context).inflate(R.layout.item_ranking,parent,false)
        ViewGroup=parent
        return RankingVideoAdapter.HolderVideo(view)
    }

//    private fun setVideoUrl(modelVideo: ModelVideo, holder: RankingVideoAdapter.HolderVideo) {
//
//
//        val VideoURL:String?=modelVideo.Video
//
//        //Hien thi thanh dieu khien video
//        val medialController= MediaController(ViewGroup.context)
//        medialController.setAnchorView(holder.videoView)
//
//        val videoUri= Uri.parse(VideoURL)
//        //Cho cac thuoc tinh video vao de chay
//        holder.videoView.setMediaController(medialController)
//        holder.videoView.setVideoURI(videoUri)
//        holder.videoView.requestFocus()
//
//    }
//
    override fun getItemCount(): Int {
        return videoArrayList!!.size
    }
//
    override fun onBindViewHolder(holder: HolderVideo, position: Int) {
//        val modelVideo=videoArrayList!![position]
//        //Truyen du lieu vao bien
//        val Name:String?=modelVideo.Name
//        //Truyen du lieu ra Video View
//        var pos=position+1
//        holder.titleVideo.text="Number "+pos.toString()+":  "+Name
//
//        //Truyen link cho holder de chieu video
//        setVideoUrl(modelVideo, holder)
    }
}