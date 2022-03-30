package com.khtn.ratevid.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.khtn.ratevid.R
import com.khtn.ratevid.model.userItem
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var dialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        registerTV.setOnClickListener {
            val intent= Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        checkIfAlreadyLogin()
        loginBT.setOnClickListener {
            if (TextUtils.isEmpty(emailET.text.toString())){
                emailET.setError("Please enter email")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(passwordET.text.toString())){
                passwordET.setError("Please enter password")
                return@setOnClickListener
            }
            login(emailET.text.toString(),passwordET.text.toString())
        }
        forgetPassword()
    }

    private fun forgetPassword() {
        forgetPassTV.setOnClickListener {
            val intent= Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
    }
    fun showLoadingDialog(){
        dialog = ProgressDialog.show(
            this@LoginActivity, "",
            "Logging. Please wait...", true
        )

    }
    fun checkIfAlreadyLogin(){
        auth = FirebaseAuth.getInstance()

        val currentUser=auth.currentUser
        if (currentUser!=null){
            showLoadingDialog()

            var databaseReference: DatabaseReference=FirebaseDatabase.getInstance().reference!!.child("profile")
            databaseReference?.child(currentUser?.uid!!)?.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var user = userItem(
                        snapshot.child("UID").value as String?,
                        snapshot.child("Type").value as String?,
                        snapshot.child("UserName").value as String?
                    )
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                    dialog.dismiss()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }
    }
    fun login(_email:String,_pass:String){

        var email=_email.trim { it<= ' ' }
        var pass=_pass.trim{ it<= ' '}

        showLoadingDialog()
        auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener {
                if(it.isSuccessful){

                    var databaseReference: DatabaseReference=FirebaseDatabase.getInstance().reference!!.child("profile")
                    databaseReference?.child( auth?.uid!!)?.addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var user = userItem(
                                snapshot.child("UID").value as String?,
                                snapshot.child("Type").value as String?,
                                snapshot.child("UserName").value as String?
                            )
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("user", user)
                            startActivity(intent)
                            finish()
                            dialog.dismiss()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })

                    //Ket thuc
                }else{
                    //Neu dang nhap that bai
                    dialog.dismiss()
                    Toast.makeText(this@LoginActivity,"Login failed, please try again!", Toast.LENGTH_LONG).show()
                }
            }
    }


}
