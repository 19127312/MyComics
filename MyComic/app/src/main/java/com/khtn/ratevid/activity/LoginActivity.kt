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
import com.khtn.ratevid.FirebaseUtil
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

            FirebaseUtil.getBannedStatus(currentUser.uid,object: FirebaseUtil.FirebaseCallbackUser{
                override fun onCallback(user: userItem) {
                    if(user.status=="Banned"){
                        Toast.makeText(this@LoginActivity,"You have been banned. Reason: ${user.reason}",Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                    else if(user.status=="Active"){
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                        finish()
                        dialog.dismiss()
                    }
                }

            })


        }
    }
    fun login(_email:String,_pass:String){

        var email=_email.trim { it<= ' ' }
        var pass=_pass.trim{ it<= ' '}

        showLoadingDialog()
        FirebaseUtil.loginUser(email,pass,object :FirebaseUtil.FirebaseCallbackUpdate{
            override fun onCallback(status: String) {
                if(status!="Fail"){
                    FirebaseUtil.getBannedStatus(status, object :FirebaseUtil.FirebaseCallbackUser{
                        override fun onCallback(user: userItem) {
                            if(user.status=="Banned"){
                                Toast.makeText(this@LoginActivity,"You have been banned. Reason: ${user.reason}",Toast.LENGTH_LONG).show()
                                dialog.dismiss()
                            }
                            else if(user.status=="Active"){
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.putExtra("user", user)
                                startActivity(intent)
                                finish()
                                dialog.dismiss()
                            }
                        }
                    })
                }else{
                    dialog.dismiss()
                    Toast.makeText(this@LoginActivity,"Login failed, please try again!", Toast.LENGTH_LONG).show()
                }
            }

        })

    }


}
