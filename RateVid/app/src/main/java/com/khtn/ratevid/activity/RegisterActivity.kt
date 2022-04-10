package com.khtn.ratevid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.khtn.ratevid.FirebaseUtil
import com.khtn.ratevid.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerBT.setOnClickListener {

            if(TextUtils.isEmpty(userNameET.text.toString())){
                userNameET.setError("Please enter username")
                return@setOnClickListener
            }else if(TextUtils.isEmpty(emailRegisterET.text.toString())){
                emailRegisterET.setError("Please enter email")
                return@setOnClickListener
            }else if (TextUtils.isEmpty(passwordET.text.toString())){
                passwordET.setError("Please enter password")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(repeatPasswordET.text.toString())){
                repeatPasswordET.setError("Please enter repeat password")
                return@setOnClickListener
            }else if(passwordET.text.toString() != repeatPasswordET.text.toString()){
                repeatPasswordET.setError("Password and repeat password are not the same")
                return@setOnClickListener
            }

            register(emailRegisterET.text.toString(),passwordET.text.toString(),userNameET.text.toString())
        }
    }

    fun register( _email:String,_password:String,_name:String){
        FirebaseUtil.registerUser(_email, _password,_name, object :FirebaseUtil.FirebaseCallbackUpdate{
            override fun onCallback(status: String) {
                if(status=="Success"){
                    Toast.makeText(this@RegisterActivity, "Register successfully",Toast.LENGTH_SHORT).show()
                    finish()
                }else if(status=="Fail"){
                    Toast.makeText(this@RegisterActivity, "Register Fail!",Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}