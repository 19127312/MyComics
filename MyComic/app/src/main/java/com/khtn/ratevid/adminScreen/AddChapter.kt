package com.khtn.ratevid.adminScreen

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.khtn.ratevid.R
import com.khtn.ratevid.adapter.PicAdapter
import com.khtn.ratevid.model.picItem
import kotlinx.android.synthetic.main.activity_add_chapter.*

class AddChapter : AppCompatActivity() {
    private val INCREASE_CHAPTER=200

    lateinit var imgsList : ArrayList<picItem>
    lateinit var adapter: PicAdapter
    var comicID=""
    var chapterNumber=0
    var isUpload=false
    var isUpdate=false
    lateinit var view :View
    private  val storageReference= FirebaseStorage.getInstance().reference
    private val databaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_chapter)
        var customListView = findViewById<RecyclerView>(R.id.recycler)
        view=findViewById<View>(R.id.rootChapter)
        val intent=intent
        comicID= intent.getStringExtra("comicID").toString()
        chapterNumber= intent.getIntExtra("chapterNumber",0)
        isUpdate=intent.getBooleanExtra("isUpdate",false)

        //If update , get data from firebase
        if(isUpdate){
            getChapterData()
        }

        //setup RecycleView
        imgsList= ArrayList<picItem>()
        adapter = PicAdapter(view,this,imgsList)
        customListView?.adapter = adapter
        customListView?.layoutManager = LinearLayoutManager(this)
        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL)
        customListView.addItemDecoration(itemDecoration)

        // Choose file from gallery when click add button
        addImgBtn.setOnClickListener {
            startFileChooser()
        }

        //Upload chapter to firebase with current chapter list
        uploadBtn.setOnClickListener {
            uploadChapter()
        }
    }

    private fun getChapterData() {
        val ref= FirebaseDatabase.getInstance().getReference("comic").child(comicID).child("chapter").child(chapterNumber.toString())
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var i=1
                for (item in snapshot.children){
                    val modelComic = item.getValue(picItem::class.java)
                    modelComic?.addNumStatus(i,"Waiting to upload")
                    i++
                    if (modelComic != null) {
                        imgsList.add(modelComic)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun uploadChapter() {
        var snackbar = Snackbar.make(view, "Uploading", Snackbar.LENGTH_INDEFINITE)
        snackbar.show()
        //Delete all current pic in this chapter for easier to modify pic
        databaseReference.child("comic").child(comicID).child("chapter").child(chapterNumber.toString()).removeValue()
        for(i in 0..imgsList.size-1){
            //If this pic is in gallery or modify(add/change/delete), then it should have uri to upload
            if(imgsList[i].imgURI !=null){
                val path= "${comicID}/${chapterNumber}/pic${imgsList[i].number}.png"
                val uploadTask= imgsList[i].imgURI?.let { storageReference.child(path).putFile(it) }

                //Get url download when it is uploaded to firebase
                if (uploadTask != null) {
                    uploadTask.addOnSuccessListener {
                        val downloadURLTask=storageReference.child(path).downloadUrl
                        downloadURLTask.addOnSuccessListener {

                            var hashMap: HashMap<String, String> = HashMap()
                            hashMap.put("imgURL", it.toString())
                            databaseReference.child("comic").child(comicID).child("chapter").child(chapterNumber.toString()).child("pic${imgsList[i].number}").setValue(hashMap).addOnSuccessListener {
                                snackbar= Snackbar.make(view, "Upload pic${imgsList[i].number} successfully", Snackbar.LENGTH_SHORT)
                                snackbar.show()
                                if(!isUpdate){
                                    isUpload=true
                                }
                            }
                            imgsList[i].status="Uploaded"
                            adapter.notifyItemChanged(i)
                        }

                    }
            }
                //If this pic is already in firebase, don't have to upload it's uri again, just change their url to correct position
            }else{
                var hashMap: HashMap<String, String> = HashMap()
                hashMap.put("imgURL", imgsList[i]?.imgURL!!)
                databaseReference.child("comic").child(comicID).child("chapter").child(chapterNumber.toString()).child("pic${imgsList[i].number}").setValue(hashMap).addOnSuccessListener {
                    snackbar= Snackbar.make(view, "Upload pic${imgsList[i].number} successfully", Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
                imgsList[i].status="Uploaded"
                adapter.notifyItemChanged(i)
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

        //Add function, if pic is successfully gotten from gallery
        if(requestCode==1111 &&resultCode==Activity.RESULT_OK&& data!=null){
            var filepath=data.data!!
            imgsList.add(picItem(imgsList.size+1,filepath,"Waiting to upload"))
            adapter.notifyItemInserted(imgsList.size)
        }

        //Change function, callback to adapter to change that item
        if(requestCode==2222 &&resultCode==Activity.RESULT_OK&& data!=null){
            adapter.OnActivityResult(data)
        }
    }


    override fun onBackPressed() {
        //Change latest chapter when use adding chapter function
        if(isUpload){
            chapterNumber+=1
            databaseReference.child("comic").child(comicID).child("lastestChapter").setValue(chapterNumber)
            databaseReference.child("comic").child(comicID).child("updatedTime").setValue(System.currentTimeMillis())

            val replyIntent = Intent()
            setResult(Activity.RESULT_OK, replyIntent)

        }
        finish()
    }

}