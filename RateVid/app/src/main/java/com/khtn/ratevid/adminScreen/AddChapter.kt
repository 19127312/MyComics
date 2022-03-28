package com.khtn.ratevid.adminScreen

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.khtn.ratevid.R
import com.khtn.ratevid.adapter.ChosenImageAdapter
import com.khtn.ratevid.model.ModelChosenImage
import kotlinx.android.synthetic.main.activity_add_chapter.*

class AddChapter : AppCompatActivity() {
    private val INCREASE_CHAPTER=200

    lateinit var imgsList : ArrayList<ModelChosenImage>
    lateinit var adapter: ChosenImageAdapter
    var comicID=""
    var chapterNumber=0
    var isUpload=false
    var isUpdate=false
    private  val storageReference= FirebaseStorage.getInstance().reference
    private val databaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_chapter)
        var customListView = findViewById<RecyclerView>(R.id.recycler)

        val intent=intent
        comicID= intent.getStringExtra("comicID").toString()
        chapterNumber= intent.getIntExtra("chapterNumber",0)
        isUpdate=intent.getBooleanExtra("isUpdate",false)
        Log.d("MyScreen",comicID)
        Log.d("MyScreen",chapterNumber.toString())
        Log.d("MyScreen",isUpdate.toString())
        if(isUpdate){
            getChapterData()
        }

        imgsList= ArrayList<ModelChosenImage>()
        adapter = ChosenImageAdapter(this,imgsList)
        customListView?.adapter = adapter
        customListView?.layoutManager = LinearLayoutManager(this)
        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL)
        customListView.addItemDecoration(itemDecoration)
        addImgBtn.setOnClickListener {
            startFileChooser()
        }
        uploadBtn.setOnClickListener {
            uploadChapter()
        }
    }

    private fun getChapterData() {

    }

    private fun uploadChapter() {
        val view = findViewById<View>(R.id.rootChapter)
        var snackbar = Snackbar.make(view, "Uploading", Snackbar.LENGTH_INDEFINITE)
        snackbar.show()
        databaseReference.child("comic").child(comicID).child("chapter").child(chapterNumber.toString()).removeValue()
        for(i in 0..imgsList.size-1){
            val path= "${comicID}/${chapterNumber}/pic${imgsList[i].number}.png"
            val uploadTask= imgsList[i].imgURI?.let { storageReference.child(path).putFile(it) }
            if (uploadTask != null) {
                uploadTask.addOnSuccessListener {
                    val downloadURLTask=storageReference.child(path).downloadUrl
                    downloadURLTask.addOnSuccessListener {

                        var hashMap: HashMap<String, String> = HashMap()
                        hashMap.put("imgURL", it.toString())
                        databaseReference.child("comic").child(comicID).child("chapter").child(chapterNumber.toString()).child("pic${imgsList[i].number}").setValue(hashMap).addOnSuccessListener {
                            snackbar= Snackbar.make(view, "Upload pic${imgsList[i].number} successfully", Snackbar.LENGTH_SHORT)
                            snackbar.show()
                            isUpload=true
                        }
                        imgsList[i].status="Uploaded"
                        adapter.notifyItemChanged(i)
                    }

                }
            }

        }

    }

    fun startFileChooser() {
        var i= Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(i,1111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1111 &&resultCode==Activity.RESULT_OK&& data!=null){
            var filepath=data.data!!
            imgsList.add(ModelChosenImage(imgsList.size+1,filepath,"Waiting to upload"))
            adapter.notifyItemInserted(imgsList.size)
        }
        if(requestCode==2222 &&resultCode==Activity.RESULT_OK&& data!=null){
            adapter.OnActivityResult(data)
        }
    }

    override fun onBackPressed() {
        if(isUpload){
            chapterNumber+=1
            databaseReference.child("comic").child(comicID).child("lastestChapter").setValue(chapterNumber)
            val replyIntent = Intent()
            setResult(Activity.RESULT_OK, replyIntent)

        }
        finish()
    }

}