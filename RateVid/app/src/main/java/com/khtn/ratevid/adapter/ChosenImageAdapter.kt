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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.khtn.ratevid.R
import com.khtn.ratevid.model.ModelChosenImage

class ChosenImageAdapter(var imgs : ArrayList<ModelChosenImage>, var comicID:String, var chapterNumber: String) :
    RecyclerView.Adapter<ChosenImageAdapter.ViewHolder>() {
    private  val storageReference= FirebaseStorage.getInstance().reference
    private val databaseReference = FirebaseDatabase.getInstance().reference

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {

        val number = listItemView.findViewById<TextView>(R.id.numPic)
        val status = listItemView.findViewById<TextView>(R.id.status)
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
        val selectImage  = imgs[position]

        var textView = holder.number
        var img = holder.image
        var status=holder.status

        status.text=selectImage.status
        img.setImageURI(selectImage.img)
        textView.setText("pic "+selectImage.number.toString())


        holder.delete.setOnClickListener {
            for (i in position..imgs.size-1){
                imgs[i].number-=1
            }
            imgs.removeAt(position)
            notifyDataSetChanged()
        }

        holder.upload.setOnClickListener {
            val path= "${comicID}/${chapterNumber}/pic${selectImage.number}.png"
            val uploadTask=storageReference.child(path).putFile(selectImage.img)
            uploadTask.addOnSuccessListener {
                val downloadURLTask=storageReference.child(path).downloadUrl
                downloadURLTask.addOnSuccessListener {
                    var hashMap: HashMap<String, String> = HashMap()
                    hashMap.put("imgURL", it.toString())
                    databaseReference.child("comic").child(comicID).child("chapter").child(chapterNumber).child("pic${selectImage.number}").setValue(hashMap)
                    imgs[position].status="Uploaded"
                    notifyItemChanged(position)
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return imgs.size
    }


}