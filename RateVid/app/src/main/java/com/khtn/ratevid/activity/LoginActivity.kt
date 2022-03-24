package com.khtn.ratevid.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.khtn.ratevid.R
import com.khtn.ratevid.adapter.ChosenImageAdapter
import com.khtn.ratevid.model.ModelChosenImage
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_profile.*

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

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

    fun checkIfAlreadyLogin(){
        auth = FirebaseAuth.getInstance()

        val currentUser=auth.currentUser
        if (currentUser!=null){
            var databaseReference: DatabaseReference=FirebaseDatabase.getInstance().reference!!.child("profile")
            val userReference= databaseReference?.child(currentUser?.uid!!)?.child("Type")?.get().addOnSuccessListener {
                val intent= Intent(this, MainActivity::class.java)
                intent.putExtra("typeuser",it.value.toString())
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                Log.e("MyScreen", "Error getting data", it)
            }


        }
    }
    fun login(_email:String,_pass:String){

        var email=_email.trim { it<= ' ' }
        var pass=_pass.trim{ it<= ' '}
        auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    //Neu thanh cong --> Chuyen sang man hinh profie

                    var databaseReference: DatabaseReference=FirebaseDatabase.getInstance().reference!!.child("profile")
                    val userReference= databaseReference?.child(auth?.uid!!)?.child("Type")?.get().addOnSuccessListener {
                        val intent= Intent(this, MainActivity::class.java)
                        intent.putExtra("typeuser",it.value.toString())
                        startActivity(intent)
                        finish()
                    }.addOnFailureListener{
                        Log.e("MyScreen", "Error getting data", it)
                    }

                    //Ket thuc
                }else{
                    //Neu dang nhap that bai
                    Toast.makeText(this@LoginActivity,"Login failed, please try again!", Toast.LENGTH_LONG).show()
                }
            }
    }


}
