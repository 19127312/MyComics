package com.khtn.ratevid.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.khtn.ratevid.R
import com.khtn.ratevid.model.picItem

class PicListAdapter(var context:Activity,var imgs : ArrayList<picItem>) :
    RecyclerView.Adapter<PicListAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val image=  listItemView.findViewById<ImageView>(R.id.picItemIV)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.item_pic, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectImage  = imgs[position]
        Glide.with(context).load(selectImage.imgURL).into(holder.image)

    }

    override fun getItemCount(): Int {
        return imgs.size
    }


}