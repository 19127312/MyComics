package com.khtn.ratevid.adminScreen

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.khtn.ratevid.FirebaseUtil
import com.khtn.ratevid.R
import com.khtn.ratevid.activity.CommentActivity
import com.khtn.ratevid.adapter.ChapterAdapter
import com.khtn.ratevid.model.comicItem
import kotlinx.android.synthetic.main.activity_detail_comic_admin.*
import kotlinx.android.synthetic.main.activity_login.*

class DetailComicAdminActivity : AppCompatActivity() {

    var thumbnail: Uri? =null
    private var databaseReference = FirebaseDatabase.getInstance().reference

    private val IMAGE_PICK_GALLARY_CODE=100
    private val INCREASE_CHAPTER=200
    public lateinit var comic:comicItem
    lateinit var adapter:ChapterAdapter
    lateinit var customListView: RecyclerView
    var chapters=ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_comic_admin)
        val intent = intent
        comic= intent.getSerializableExtra("item") as comicItem

        setupLayout(comic)
        changeComicProperties(comic)
        addChapter(comic)
        deleteChapter(comic)
        commentSection(comic)
    }

    private fun commentSection(comic: comicItem) {
        watchCommentBtn.setOnClickListener {
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("comicID", comic.id)
            intent.putExtra("userUID", "Admin")
            startActivity(intent)
        }
    }

    private fun deleteChapter(comic: comicItem) {
        deleteComicBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Confirm")
            builder.setMessage("Are you sure to delete this manga?")

            builder.setPositiveButton("YES") { dialog, which -> // remove the the manga and close the dialog
                finish()

                dialog.dismiss()
                databaseReference.child("comic").child(comic?.id!!).removeValue()

            }

            builder.setNegativeButton("NO") { dialog, which -> // Do nothing

                dialog.dismiss()
            }

            val alert = builder.create()
            alert.show()
        }
    }

    private fun addChapter(comic: comicItem) {
        addChapterBtn.setOnClickListener {
            val intent= Intent(this, AddChapter::class.java)
            intent.putExtra("comicID",comic.id)
            intent.putExtra("chapterNumber",comic.lastestChapter)
            startActivityForResult(intent,INCREASE_CHAPTER)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==IMAGE_PICK_GALLARY_CODE &&resultCode== Activity.RESULT_OK&& data!=null){
            thumbnail=data.data!!
            thumbnailEditImageView.setImageURI(thumbnail)
        }
        if(requestCode==INCREASE_CHAPTER &&resultCode== Activity.RESULT_OK){
            comic.lastestChapter = comic.lastestChapter?.plus(1)
        }

    }

    private fun changeComicProperties(comic: comicItem) {
        changeThumbnailBtn.setOnClickListener {
            var i= Intent()
            i.setType("image/*")
            i.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(i,IMAGE_PICK_GALLARY_CODE)
        }

        uploadThumbnailBtn.setOnClickListener {
            if(thumbnail == null){
                Toast.makeText(this,"Please choose another thumbnail to upload",Toast.LENGTH_SHORT).show()
            }else{
                uploadThumbnail(comic)
            }
        }

        updateNameComicBtn.setOnClickListener {
            if (TextUtils.isEmpty(nameComicET.text.toString())){
                emailET.setError("Please enter name")
                return@setOnClickListener
            }else{
                updateFirebase(comic,"name",nameComicET)
            }
        }
        updateAuthorBtn.setOnClickListener {
            if (TextUtils.isEmpty(authorComicET.text.toString())){
                emailET.setError("Please enter author's name")
                return@setOnClickListener
            }else{
                updateFirebase(comic,"author",authorComicET)

            }
        }

        updateDescriptionBtn.setOnClickListener {
            if (TextUtils.isEmpty(descriptionChangeET.text.toString())){
                emailET.setError("Please enter description")
                return@setOnClickListener
            }else{
                updateFirebase(comic,"description",descriptionChangeET)

            }
        }
    }

    fun updateFirebase(comic: comicItem,property:String , editText:EditText){
        var snackbar = Snackbar.make(scrollRoot, "Updating", Snackbar.LENGTH_INDEFINITE)
        snackbar.show()
        FirebaseUtil.updatePropertyComic(comic, property, editText.text.toString(), object: FirebaseUtil.FirebaseCallbackUpdate{
            override fun onCallback(status: String) {
                snackbar.dismiss()
                if(status=="Success"){
                    snackbar = Snackbar.make(scrollRoot, "Update successfully", Snackbar.LENGTH_SHORT)
                }else if( status=="Fail"){
                    snackbar = Snackbar.make(scrollRoot, "Failed. Something wrong", Snackbar.LENGTH_SHORT)
                }
                snackbar.show()
                editText.clearFocus()
            }
        })

    }

    fun uploadThumbnail(comic: comicItem){
        var snackbar = Snackbar.make(scrollRoot, "Updating", Snackbar.LENGTH_INDEFINITE)
        snackbar.show()
        FirebaseUtil.updateThumbnail(comic, thumbnail!!,object: FirebaseUtil.FirebaseCallbackUpdate{
            override fun onCallback(status: String) {
                snackbar.dismiss()
                if(status=="Success"){
                    snackbar = Snackbar.make(scrollRoot, "Update successfully", Snackbar.LENGTH_SHORT)
                }else if( status=="Fail"){
                    snackbar = Snackbar.make(scrollRoot, "Failed. Something wrong", Snackbar.LENGTH_SHORT)
                }
                snackbar.show()
            }
        })
    }
    private fun setupLayout(comic: comicItem) {
        //Image
        Glide.with(this).load(comic.thumbnail).into(thumbnailEditImageView)
        //Name
        nameComicET.setText(comic.name)
        //Author
        authorComicET.setText(comic.author)

        //Description
        descriptionChangeET.setText(comic.description)

        //Chapter List

        loadChapter()
        adapter = ChapterAdapter(chapters)

        customListView = findViewById<RecyclerView>(R.id.chapterRecycleView) as RecyclerView
        customListView!!.adapter = adapter
        adapter.setOnItemClickListener(object: ChapterAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                var pos= chapters.size-position;
                val intent= Intent(this@DetailComicAdminActivity,AddChapter::class.java)
                intent.putExtra("comicID",comic.id)
                intent.putExtra("chapterNumber",pos)
                intent.putExtra("isUpdate",true)
                startActivity(intent)

            }
        })
        customListView.layoutManager = LinearLayoutManager(this)

    }
    private fun loadChapter() {
        FirebaseUtil.readChapters(comic, object : FirebaseUtil.FirebaseCallbackChapterList{
            override fun onCallback(arrayChapter: ArrayList<Int>) {
                chapters.clear()
                chapters.addAll(arrayChapter)
                adapter.notifyDataSetChanged()
            }
        })
    }


}


