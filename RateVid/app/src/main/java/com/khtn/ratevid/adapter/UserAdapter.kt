package com.khtn.ratevid.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.khtn.ratevid.R
import com.khtn.ratevid.model.userItem
import kotlinx.android.synthetic.main.activity_detail_comic_admin.*

class UserAdapter(var context: Context, var users : ArrayList<userItem>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    var databaseReference = FirebaseDatabase.getInstance().reference

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val username=  listItemView.findViewById<TextView>(R.id.userNameTV)
        val status=  listItemView.findViewById<TextView>(R.id.userStatusTV)
        val banBtn= listItemView.findViewById<Button>(R.id.banBtn)
        var layout: ConstraintLayout =itemView.findViewById(R.id.userBackground)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.item_user, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user  = users[position]

        holder.username.text=user.UserName
        holder.status.text=user.reason


        if(user.status=="Banned"){
            holder.layout.background= ContextCompat.getDrawable(context, R.drawable.bd_rank1)
            holder.banBtn.text= "UNBAN"
            holder.banBtn.setOnClickListener {
                databaseReference.child("profile").child(user?.UID!!).child("status").setValue("Active")
                databaseReference.child("profile").child(user?.UID!!).child("reason").setValue("None")
                user.reason="None"
                user.status="Active"
                notifyItemChanged(position)
            }
        } else {
            holder.banBtn.text=  "BAN"
            holder.layout.background= ContextCompat.getDrawable(context, R.drawable.bd_active)
            holder.banBtn.setOnClickListener {
                val edit = AlertDialog.Builder(context)

                    edit.setTitle("Ban user")


                    edit.setTitle("Enter ban reason for this user")


                val layout: View = LayoutInflater.from(context).inflate(R.layout.edit_comment, null)
                val editText: EditText = layout.findViewById(R.id.edit_cm)
                val editBtn: Button = layout.findViewById(R.id.edit_button)
                edit.setView(layout)
                val editDialog: AlertDialog = edit.create()
                editBtn.setOnClickListener {
                    var reason= editText.text.toString()

                    if (TextUtils.isEmpty(reason)){
                        editText.error = "Please enter something"
                        return@setOnClickListener
                    } else {
                        databaseReference!!.child("profile").child(user?.UID!!).child("reason").setValue(reason)

                        databaseReference!!.child("profile").child(user?.UID!!).child("status").setValue("Banned")

                        user.reason=reason
                        user.status="Banned"
                        notifyItemChanged(position)

                        editDialog.dismiss()
                    }
                }
                editDialog.show()
            }
        }



    }

    override fun getItemCount(): Int {
        return users.size
    }


}