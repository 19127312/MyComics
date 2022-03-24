package com.khtn.ratevid.adapter

import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.khtn.ratevid.R
import com.khtn.ratevid.model.ModelChosenImage

class ChosenImageAdapter(var imgs : ArrayList<ModelChosenImage>) :
    RecyclerView.Adapter<ChosenImageAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {

        val number = listItemView.findViewById<TextView>(R.id.numPic)
        val image=  listItemView.findViewById<ImageView>(R.id.chosenPic)
        val delete = listItemView.findViewById<Button>(R.id.deleteBtn)
        val upload = listItemView.findViewById<Button>(R.id.uploadBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.item_chosen, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val a  = imgs[position]

        var textView = holder.number
        var img = holder.image

        img.setImageURI(a.img)
        textView.setText("pic "+a.number.toString())


        holder.delete.setOnClickListener {

        }

    }

    override fun getItemCount(): Int {
        return imgs.size
    }


}