package com.khtn.ratevid.adminScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.khtn.ratevid.R
import com.khtn.ratevid.model.comicItem
import kotlinx.android.synthetic.main.activity_detail_comic_admin.*
import kotlinx.android.synthetic.main.activity_login.*

class DetailComicAdminActivity : AppCompatActivity() {
    lateinit var comic:comicItem
    var databaseReference: DatabaseReference?=null
    lateinit var snackbar: Snackbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_comic_admin)
        val intent = intent
        comic= intent.getSerializableExtra("item") as comicItem

        setupLayout()
        changeComicProperties()
        addChapter()
    }

    private fun addChapter() {

    }

    private fun changeComicProperties() {



        databaseReference= FirebaseDatabase.getInstance().reference!!.child("comic").child(comic?.id!!)
        updateNameComicBtn.setOnClickListener {
            if (TextUtils.isEmpty(nameComicET.text.toString())){
                emailET.setError("Please enter name")
                return@setOnClickListener
            }else{
                databaseReference!!.child("name").setValue(nameComicET.text.toString()).addOnSuccessListener {
                    showSnackBar()
                    nameComicET.clearFocus()
                }
            }
        }
        updateAuthorBtn.setOnClickListener {
            if (TextUtils.isEmpty(authorComicET.text.toString())){
                emailET.setError("Please enter author's name")
                return@setOnClickListener
            }else{
                databaseReference!!.child("author").setValue(authorComicET.text.toString()).addOnSuccessListener {
                    showSnackBar()
                    authorComicET.clearFocus()
                }

            }
        }

        updateDescriptionBtn.setOnClickListener {
            if (TextUtils.isEmpty(descriptionChangeET.text.toString())){
                emailET.setError("Please enter description")
                return@setOnClickListener
            }else{
                databaseReference!!.child("description").setValue(descriptionChangeET.text.toString()).addOnSuccessListener {
                    showSnackBar()
                    descriptionChangeET.clearFocus()
                }
            }
        }
    }
    fun showSnackBar(){
        snackbar = Snackbar.make(scrollRoot, "Updating", Snackbar.LENGTH_SHORT)
        snackbar.show()
    }
    private fun setupLayout() {
        //Image
        Glide.with(this).load(comic.thumbnail).into(thumbnailEditImageView)
        //Name
        nameComicET.setText(comic.name)
        //Author
        authorComicET.setText(comic.author)

        //Description
        descriptionChangeET.setText(comic.description)

        //Chapter List
    }
}