package com.khtn.ratevid.adminScreen

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.khtn.ratevid.R
import kotlinx.android.synthetic.main.activity_add_comic.*

class AddComic : AppCompatActivity() {
    var thumbnail: Uri? =null
    private  val storageReference= FirebaseStorage.getInstance().reference
    private val databaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comic)
        upComicBtn.setOnClickListener {
            if(TextUtils.isEmpty(mangaNameET.text.toString())){
                mangaNameET.setError("Please enter name for this manga")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(authorNameET.text.toString())){
                authorNameET.setError("Please enter author name for this manga")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(desET.text.toString())){
                desET.setError("Please enter description for this manga")
                return@setOnClickListener
            }
            else if (thumbnail==null){
                Toast.makeText(this,"You must pick thumbnail from gallery first!", Toast.LENGTH_LONG).show()
            }else{
                uploadComic(mangaNameET.text.toString(),authorNameET.text.toString(),desET.text.toString())
            }
        }
        thumbnailBtn.setOnClickListener {
            startFileChooser()
        }
    }
    fun startFileChooser() {
        var i= Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(i,1111)
    }
    private fun uploadComic(name: String, author: String, description: String) {
        val comicID = "" + System.currentTimeMillis()

        val view = findViewById<View>(R.id.root)
        val snackbar: Snackbar = Snackbar.make(view, "Uploading", Snackbar.LENGTH_INDEFINITE)
        snackbar.show()
        val path = "${comicID}/thumbnail.png"
        val uploadTask = thumbnail?.let { storageReference.child(path).putFile(it) }
        if (uploadTask != null) {
            uploadTask.addOnSuccessListener {
                val downloadURLTask = storageReference.child(path).downloadUrl
                downloadURLTask.addOnSuccessListener {
                    var hashMap: HashMap<String, Any> = HashMap()
                    hashMap.put("thumbnail", it.toString())
                    hashMap.put("id", comicID)
                    hashMap.put("name", name)
                    hashMap.put("author", author)
                    hashMap.put("description", description)
                    hashMap.put("lastestChapter", 1)
                    hashMap.put("updatedTime", System.currentTimeMillis())

                    databaseReference.child("comic").child(comicID).setValue(hashMap)
                        .addOnSuccessListener { taskSnapshot ->
                            Toast.makeText(this, "Manga uploaded successfully", Toast.LENGTH_LONG)
                                .show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            snackbar.dismiss()
                            Toast.makeText(this, "${e.message}", Toast.LENGTH_LONG).show()
                        }
                }

            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1111 &&resultCode== Activity.RESULT_OK&& data!=null){
            thumbnail=data.data!!
            thumbnailView.setImageURI(thumbnail)
        }

    }
}