package com.khtn.ratevid.activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.MediaController
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.khtn.ratevid.R
import kotlinx.android.synthetic.main.activity_add_video.*

class AddVideoActivity : AppCompatActivity() {
    lateinit var typeUser: String

    private val VIDEO_PICK_GALLARY_CODE=100

    private var videoUri: Uri?=null // Uri cua video duoc chon

    private var title:String=""

    private var desciption:String=""
    private var type:String="Review"

    //Thanh trang thai upload video
    private lateinit var progessDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_video)
        progessDialog= ProgressDialog(this)
        progessDialog.setTitle("Please wait")
        progessDialog.setMessage("Uploading Video...")
        progessDialog.setCanceledOnTouchOutside(false)
        runAdd()
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId==R.id.radio1){
                type=radio1.text.toString()
            }
            if(checkedId==R.id.radio2){
                type=radio2.text.toString()
            }
        }
    }
    private fun runAdd(){
        PickVideoButton.setOnClickListener{
            videoPickGallery()
        }

        UploadButton.setOnClickListener {
            //Lay ten video tu o titleEt
            title = titleEt.text.toString().trim()
            //Kiem tra xem co trong khong
            if(TextUtils.isEmpty(titleEt.text.toString())){
                titleEt.setError("Please enter username")
                return@setOnClickListener
            }
            //Kiem tra xem co description khong

            else if (videoUri==null){
                //Chua chon video
                Toast.makeText(this,"You must pick the video from gallery first!", Toast.LENGTH_LONG).show()

            }else{
                upLoadVideoToFireBase()
            }
        }
    }
    private fun upLoadVideoToFireBase() {
        progessDialog.show()

        //Lay dia chi va ten
        val time=""+System.currentTimeMillis()

        var intent: Intent = getIntent()
        typeUser = intent.getStringExtra("type").toString()
        var filePath="Videos/video_$time"

        //Dua tham chieu den storage
        val storageReference= FirebaseStorage.getInstance().getReference(filePath)
        storageReference.putFile(videoUri!!)
            .addOnSuccessListener{taskSnapshot->
                val uriTask=taskSnapshot.storage.downloadUrl
                while(!uriTask.isSuccessful);

                //Lay dia chi cua video
                val downloadUri=uriTask.result
                if(uriTask.isSuccessful){
                    //Da tai len video thanh cong vao trong storgare
                    //Bo video vao database realtime
                    val dic=HashMap<String,Any>()
                    dic["Type"]=type
                    dic["Info"]="Not updated"
                    dic["ID"]="$time"
                    dic["Name"]="$title"
                    dic["Video"]="$downloadUri"
                    dic["Like"]=0
                    dic["RateAVG"]=0
                    dic["IsCensor"]=0
                    //Tro tham chieu den reviews den bo vao
                    var DatabaseReference= FirebaseDatabase.getInstance().getReference("videos")
                    if( typeUser!="Admin"){
                        DatabaseReference= FirebaseDatabase.getInstance().getReference("UserUpload")
                    }
                    DatabaseReference.child(time).setValue(dic)//Them mot dictionary vao trong cay reviews
                        //Them vao thanh cong
                        .addOnSuccessListener {taskSnapshot->
                            progessDialog.dismiss()
                            Toast.makeText(this,"Video uploaded successfully",Toast.LENGTH_LONG).show()
                        }
                        //Them that bai
                        .addOnFailureListener {e->
                            progessDialog.dismiss()
                            Toast.makeText(this,"${e.message}",Toast.LENGTH_LONG).show()
                        }

                }
            }
            .addOnFailureListener { e ->
                progessDialog.dismiss()
                Toast.makeText(this, "${e.message}", Toast.LENGTH_LONG).show()
            }
    }
    private fun videoPickGallery(){
        val intent=Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
//        intent.type="video/*"
//        intent.action=Intent.ACTION_GET_CONTENT
        intent.putExtra("return-data", true)

        startActivityForResult(
            Intent.createChooser(intent,"Choose Video"),VIDEO_PICK_GALLARY_CODE
        )
    }

    //Chinh nut dieu huong back ve review activity
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()//Di ve ReviewActivity
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode== RESULT_OK){
            if(requestCode ==VIDEO_PICK_GALLARY_CODE)
                videoUri=data!!.data
                setVideotoVideoView()

        }else{

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //Dua video vao view trong add Activity
    private fun setVideotoVideoView(){
        Log.d("MyScreen","Load Video")
        // hien thi video controler cho video dc pick
        val mediaControler= MediaController(this)
        mediaControler.setAnchorView(videoView)
        videoView.setMediaController((mediaControler))
        //Chon dia chi cho video
        videoView.setVideoURI(videoUri)
        //Tai video len videoView
        videoView.requestFocus()
        videoView.setOnPreparedListener{

            //Neu ma video da tai xog, thi khong duoc chay tu dong
            videoView.pause()
        }

    }
}