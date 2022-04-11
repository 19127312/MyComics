package com.khtn.ratevid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.khtn.ratevid.FirebaseUtil
import com.khtn.ratevid.R
import kotlinx.android.synthetic.main.activity_forget_password.*

class ForgetPasswordActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        auth= FirebaseAuth.getInstance()
        forgettPassword()
    }

    private fun forgettPassword() {
        submitBT.setOnClickListener(){
            if(TextUtils.isEmpty(emailForgetET.text.toString())){
                emailForgetET.error = "Please enter email"
                return@setOnClickListener
            }

            FirebaseUtil.forgetPass(emailForgetET.text.toString(),object :FirebaseUtil.FirebaseCallbackUpdate{
                override fun onCallback(status: String) {
                    if(status=="Success"){
                        Toast.makeText(
                            this@ForgetPasswordActivity,
                            "Email sent successfully to reset your password!",
                            Toast.LENGTH_LONG
                        ).show()

                        finish()
                    }else{
                        Toast.makeText(
                            this@ForgetPasswordActivity,
                            "Error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            })

        }
    }
}