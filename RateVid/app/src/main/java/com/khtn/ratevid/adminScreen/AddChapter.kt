package com.khtn.ratevid.adminScreen

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.khtn.ratevid.R
import com.khtn.ratevid.adapter.ChosenImageAdapter
import com.khtn.ratevid.model.ModelChosenImage
import kotlinx.android.synthetic.main.activity_add_chapter.*

class AddChapter : AppCompatActivity() {
    lateinit var imgsList : ArrayList<ModelChosenImage>
    lateinit var adapter: ChosenImageAdapter
    var comicID="truyen1"
    var chapterNumber="chap1"
    private  val storageReference= FirebaseStorage.getInstance().reference
    private val databaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_chapter)
        var customListView = findViewById<RecyclerView>(R.id.recycler)
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

    private fun uploadChapter() {
        databaseReference.child("comic").child(comicID).child("chapter").child(chapterNumber).removeValue()
        for(i in 0..imgsList.size-1){
            val path= "${comicID}/${chapterNumber}/pic${imgsList[i].number}.png"
            val uploadTask=storageReference.child(path).putFile(imgsList[i].img)
            uploadTask.addOnSuccessListener {
                val downloadURLTask=storageReference.child(path).downloadUrl
                downloadURLTask.addOnSuccessListener {
                    var hashMap: HashMap<String, String> = HashMap()
                    hashMap.put("imgURL", it.toString())
                    databaseReference.child("comic").child(comicID).child("chapter").child(chapterNumber).child("pic${imgsList[i].number}").setValue(hashMap)
                    imgsList[i].status="Uploaded"
                    adapter.notifyItemChanged(i)
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
}