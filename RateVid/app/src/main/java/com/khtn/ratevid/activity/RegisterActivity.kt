package com.khtn.ratevid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.khtn.ratevid.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerBT.setOnClickListener {
            if (TextUtils.isEmpty(passwordET.text.toString())){
                passwordET.setError("Please enter password")
                return@setOnClickListener
            }else if(TextUtils.isEmpty(emailRegisterET.text.toString())){
                emailRegisterET.setError("Please enter email")
                return@setOnClickListener
            }

            register(emailRegisterET.text.toString(),passwordET.text.toString())
        }
    }

    fun register( _email:String,_password:String){
        auth= FirebaseAuth.getInstance()
        databaseReference= FirebaseDatabase.getInstance().reference.child("profile")
        var email=_email.trim { it<= ' ' }
        var pass=_password.trim{ it<= ' '}
        auth.createUserWithEmailAndPassword(email,pass)
            //Neu tao duoc thanh cong
            .addOnCompleteListener{
                if( it.isSuccessful){
                    val currentUser=auth.currentUser
                    //Lay instance cua user dang vao
                    val currentUserDB=databaseReference?.child(currentUser?.uid!!)
                    //Dua thong tin co ban luc resgister vao database instance
                    currentUserDB?.child("Email")?.setValue(email)
                    currentUserDB?.child("UID")?.setValue(currentUser?.uid!!)
                    currentUserDB?.child("UserName")?.setValue("Tuong")

                    //Thong bao tao thanh cong
                    Toast.makeText(parent,"Registration succesfully!", Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    //Neu khong tao duoc thi tao bang thong bao
                    Toast.makeText(parent,"Registration failed, please try again!",
                        Toast.LENGTH_LONG).show()
                }
            }
    }
}