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

class PicAdapter(var view: View, var context:Activity, var imgs : ArrayList<picItem>) :
    RecyclerView.Adapter<PicAdapter.ViewHolder>() {

    var posChange=0
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {

        val number = listItemView.findViewById<TextView>(R.id.numPic)
        val status = listItemView.findViewById<TextView>(R.id.status)
        val image=  listItemView.findViewById<ImageView>(R.id.chosenPic)
        val delete = listItemView.findViewById<Button>(R.id.deleteBtn)
        val change= listItemView.findViewById<Button>(R.id.changeBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.item_chosen, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectImage  = imgs[position]

        var textView = holder.number
        var img = holder.image
        var status=holder.status
        if(selectImage.status=="Uploaded"){
            status.setTextColor(Color.parseColor("#57AF57"))
        }else if(selectImage.status=="Waiting to upload" ||selectImage.status=="Changed ! Waiting to upload"){
            status.setTextColor(Color.parseColor("#CE4F4B"))
        }
        status.text=selectImage.status

        if(selectImage.imgURI==null){
            Glide.with(context).load(selectImage.imgURL).into(holder.image)
        }else{
            img.setImageURI(selectImage.imgURI)
        }
        textView.setText("pic "+selectImage.number.toString())

        holder.delete.setOnClickListener {
            for (i in position..imgs.size-1){
                imgs[i].number = imgs[i].number?.minus(1)
                imgs[i].status="Waiting to upload"
            }
            var item=picItem(imgs[position])
            imgs.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,imgs.size)
            var snackbar = Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG)
            snackbar.show()
            snackbar.setAction("UNDO delete of pic${position+1}", View.OnClickListener() {
                imgs.add(position,item)
                for (i in position..imgs.size-1){
                    imgs[i].number = imgs[i].number?.plus(1)
                }
                notifyItemInserted(position)
                notifyItemRangeChanged(position, imgs.size)
            })
            }



        holder.change.setOnClickListener {
            startFileChooser()
            posChange=position

        }

    }

    private fun startFileChooser() {

            var i= Intent()
            i.setType("image/*")
            i.setAction(Intent.ACTION_GET_CONTENT)
            context.startActivityForResult(i,2222)
    }
    fun OnActivityResult(data: Intent?){
        if (data != null) {
            imgs[posChange].imgURI=data.data!!
            imgs[posChange].status="Changed ! Waiting to upload"
            notifyItemChanged(posChange)
        }
    }
    override fun getItemCount(): Int {
        return imgs.size
    }


}