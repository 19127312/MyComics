package com.khtn.ratevid.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.khtn.ratevid.R
import com.khtn.ratevid.activity.FollowingListActivity
import com.khtn.ratevid.activity.LoginActivity
import com.khtn.ratevid.adminScreen.ListUserActivity
import com.khtn.ratevid.model.userItem
import kotlinx.android.synthetic.main.change_password_dialog.view.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment(user: userItem) : Fragment() {
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    lateinit var user: FirebaseUser
    var curUser:userItem=user

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init ()
        loadProfile()
        updateUsername()
        changePassword()
        logout()
        followList()
        userList()
    }

    private fun followList() {
        FollowingListBtn.setOnClickListener {
            val intent= Intent(context, FollowingListActivity::class.java)
            intent.putExtra("user",curUser)
            startActivity(intent)
        }
    }
    private fun userList() {
        userList.setOnClickListener {
            val intent= Intent(context, ListUserActivity::class.java)
            startActivity(intent)
        }
    }


    fun init (){
        auth = FirebaseAuth.getInstance()
        databaseReference= FirebaseDatabase.getInstance().reference!!.child("profile")
        user= auth.currentUser!!
    }
    fun loadProfile(){
        infor.setText(curUser.UserName)

    }
    fun updateUsername(){
        updateButton.setOnClickListener{
            val userReference= databaseReference?.child(user?.uid!!)?.child("UserName")
            val newUsername = infor.text.toString()
            if (newUsername== "admin"){
                infor.setError("Can not set name to admin")
                return@setOnClickListener
            }
            userReference?.setValue(infor.text.toString())
            Toast.makeText(activity,"Update username successfully!",Toast.LENGTH_LONG).show()
        }

    }
    fun changePassword(){
        changePassBT.setOnClickListener{
            val dialog=LayoutInflater.from(context).inflate(R.layout.change_password_dialog,null)
            val builder= context?.let { it1 -> AlertDialog.Builder(it1).setView(dialog) }
            val myDialog= builder?.show()
            dialog.changePassBT.setOnClickListener {

                val pass=dialog.changePassET.text
                val repeatPass = dialog.changeRepeatPassET.text
                if(pass.length == 0){
                    dialog.changePassET.setError("Please enter new password")
                    return@setOnClickListener
                }
                else if(pass!=repeatPass){
                    dialog.changeRepeatPassET.setError("Password and Repeat Password are not the same")
                    return@setOnClickListener
                }

                if (myDialog != null) {
                    myDialog.dismiss()
                }

                user!!.updatePassword(pass.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context,"Change Password successfully",
                                Toast.LENGTH_LONG).show()
                        }
                    }
            }
            dialog.cancelBT.setOnClickListener {
                if (myDialog != null) {
                    myDialog.dismiss()
                }
            }

        }
    }
    fun logout(){
        logoutBT.setOnClickListener {
            auth.signOut()
            val intent= Intent(context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}