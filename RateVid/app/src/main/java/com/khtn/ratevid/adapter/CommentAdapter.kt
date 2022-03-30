package com.khtn.ratevid.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.khtn.ratevid.R
import com.khtn.ratevid.model.commentItem

class CommentAdapter(private var context: Context, private var ListComment: ArrayList<commentItem>?, private var comicID: String, private var userid: String): RecyclerView.Adapter<CommentAdapter.HolderComment>() {

    var database: DatabaseReference?=null

    class HolderComment(itemView: View): RecyclerView.ViewHolder(itemView){

        var username: TextView = itemView.findViewById(R.id.username)
        var content: TextView = itemView.findViewById(R.id.comment)
        var delBtn: ImageView = itemView.findViewById(R.id.del_comment)
        var editBtn: ImageView = itemView.findViewById(R.id.edit_comment)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.HolderComment {
        //Khoi tao 1 row_video
        val view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false)
        return CommentAdapter.HolderComment(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HolderComment, position: Int) {
        var modelComment = ListComment!![position]
        var userid:String? = modelComment.userid
        var content:String? = modelComment.content
        var timestamp: String? = modelComment.timestamp

        val ref= FirebaseDatabase.getInstance().getReference("profile/$userid/UserName")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var username = snapshot.value.toString()
                holder.username.text = username
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        holder.content.text = content

        hideButton(modelComment, holder)

        holder.delBtn.setOnClickListener {
            val alert = AlertDialog.Builder(context)
            alert.setTitle("Delete Comment")
            alert.setMessage("Do you want to delete this comment?")
            alert.setPositiveButton("Delete") { _, _ -> deleteComment(timestamp,position) }
            alert.show()
        }

        holder.editBtn.setOnClickListener {
            val edit = AlertDialog.Builder(context)
            edit.setTitle("Edit Comment")
            val layout: View = LayoutInflater.from(context).inflate(R.layout.edit_comment, null)
            val editText: EditText = layout.findViewById(R.id.edit_cm)
            val editBtn: Button = layout.findViewById(R.id.edit_button)
            edit.setView(layout)
            val editDialog: AlertDialog = edit.create()
            editBtn.setOnClickListener {
                val newComment= editText.text.toString()
                if (TextUtils.isEmpty(newComment)){
                    editText.error = "Please enter something"
                    return@setOnClickListener
                } else {
                    editComment(timestamp, newComment, editDialog,position)
                }
            }
            editDialog.show()
        }
    }

    private fun hideButton(modelComment: commentItem, holder: HolderComment){
        if (modelComment.userid != userid){
            holder.delBtn.visibility = View.INVISIBLE
            holder.editBtn.visibility = View.INVISIBLE
        }
        if( userid=="Admin"){
            holder.delBtn.visibility = View.VISIBLE
        }
    }

    private fun editComment(time: String?, text: String, editDialog: AlertDialog,position: Int) {
        val alert = AlertDialog.Builder(context)
        alert.setTitle("Edit Comment")
        alert.setMessage("Do you want to edit this comment?")
        alert.setPositiveButton("Accept") { _, _ ->
            database = FirebaseDatabase.getInstance().getReference("comic/$comicID/comment/$time")
            database!!.child("content").setValue(text).addOnSuccessListener {
                notifyItemChanged(position)
            }
        }
        alert.setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        alert.show()
        editDialog.dismiss()
    }

    private fun deleteComment(time: String?,position:Int) {
        database = FirebaseDatabase.getInstance().getReference("comic/$comicID/comment/$time")
        database!!.removeValue()
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return ListComment!!.size
    }


}