package com.khtn.ratevid.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.khtn.ratevid.R
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment (type: String): Fragment() {
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    lateinit var user: FirebaseUser
    var typeUser=type
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AddImage()
    }
    fun AddImage(){
        AddButton.setOnClickListener {
            //val intent= Intent(context, AddChapter::class.java)
            //startActivity(intent)
        }
    }

}