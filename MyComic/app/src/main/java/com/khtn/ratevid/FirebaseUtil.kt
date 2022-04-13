package com.khtn.ratevid

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.khtn.ratevid.activity.MainActivity
import com.khtn.ratevid.model.comicItem
import com.khtn.ratevid.model.commentItem
import com.khtn.ratevid.model.picItem
import com.khtn.ratevid.model.userItem
import kotlinx.android.synthetic.main.activity_detail_comic.*
import kotlinx.android.synthetic.main.activity_detail_comic_admin.*
import kotlinx.android.synthetic.main.activity_forget_password.*

object FirebaseUtil {
    interface FirebaseCallbackComicItem{
        fun onCallback(arrayComicItem:ArrayList<comicItem>)
    }
    interface FirebaseCallbackUserList{
        fun onCallback(arrayUserList:ArrayList<userItem>)
    }
    interface  FirebaseCallbackUpdate{
        fun onCallback(status: String)
    }
    interface FirebaseCallbackChapterList{
        fun onCallback(arrayChapter: ArrayList<Int>)
    }
    interface FirebaseCallbackPicList{
        fun onCallback(arrayPic: ArrayList<picItem>)

    }

    interface  FirebaseCallbackUser{
        fun onCallback(user: userItem)
    }
    interface FirebaseCallbackCommentList{
        fun onCallback(arrayComment: ArrayList<commentItem>)

    }
    fun readComicData(FirebaseCallback: FirebaseCallbackComicItem){
        var comicArray= ArrayList<comicItem>()
        val ref= FirebaseDatabase.getInstance().getReference("comic")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                comicArray.clear()
                for (item in snapshot.children){
                    val modelComic = item.getValue(comicItem::class.java)
                    comicArray.add(modelComic!!)
                }
                FirebaseCallback.onCallback(comicArray)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun readUserList(FirebaseCallback: FirebaseCallbackUserList){
         var userList = ArrayList<userItem>()
        val ref= FirebaseDatabase.getInstance().getReference("profile")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (item in snapshot.children){
                    val user = item.getValue(userItem::class.java)
                    if (user != null ) {
                        if( user.Type!="Admin"){
                            userList.add(user)
                        }
                    }
                }
                FirebaseCallback.onCallback(userList)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun updatePropertyComic( comic:comicItem,property: String, updateString: String ,FirebaseCallback: FirebaseCallbackUpdate){
        val ref= FirebaseDatabase.getInstance().reference.child("comic").child(comic?.id!!).child(property).setValue(updateString).addOnSuccessListener {
            FirebaseCallback.onCallback("Success")
        }
            .addOnFailureListener {
                FirebaseCallback.onCallback("Fail")
            }
    }

    fun updateThumbnail(comic: comicItem, thumbnailData: Uri,FirebaseCallback: FirebaseCallbackUpdate){
        var storageReference= FirebaseStorage.getInstance().reference
        var databaseReference = FirebaseDatabase.getInstance().reference
        val path = "${comic.id}/thumbnail.png"
        val uploadTask = thumbnailData?.let { storageReference.child(path).putFile(it) }
        if (uploadTask != null) {
            uploadTask.addOnSuccessListener {
                val downloadURLTask = storageReference.child(path).downloadUrl
                downloadURLTask.addOnSuccessListener {
                    databaseReference!!.child("comic").child(comic?.id!!).child("thumbnail").setValue(it.toString()).addOnSuccessListener {
                        FirebaseCallback.onCallback("Success")
                    }
                        .addOnFailureListener {
                            FirebaseCallback.onCallback("Fail")
                        }
                }
            }
                .addOnFailureListener {
                    FirebaseCallback.onCallback("Fail")
                }
        }
    }

    fun readChapters(comic: comicItem,FirebaseCallback: FirebaseCallbackChapterList){
        var chapterList= ArrayList<Int>()
        val ref= FirebaseDatabase.getInstance().getReference("comic").child(comic.id.toString()).child("lastestChapter").addValueEventListener(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        chapterList.clear()
                        var number = snapshot.getValue(Int::class.java)
                        for (i in 1..number!! - 1) {
                            chapterList.add(i)

                        }
                        chapterList.reverse()
                        FirebaseCallback.onCallback(chapterList)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }

    fun readPicInChapter(comicID: String, chapterNumber:Int,FirebaseCallback: FirebaseCallbackPicList ){
        var imgsList = ArrayList<picItem>()
        val ref= FirebaseDatabase.getInstance().getReference("comic").child(comicID).child("chapter").child(chapterNumber.toString())
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                imgsList.clear()
                for (item in snapshot.children){
                    val modelComic = item.getValue(picItem::class.java)
                    if (modelComic != null) {
                        imgsList.add(modelComic)
                    }
                }
                FirebaseCallback.onCallback(imgsList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun uploadComic(name: String, author: String, description: String, thumbnailData: Uri,FirebaseCallback: FirebaseCallbackUpdate){
        var storageReference= FirebaseStorage.getInstance().reference
        var databaseReference = FirebaseDatabase.getInstance().reference
        val comicID = "" + System.currentTimeMillis()
        val path = "${comicID}/thumbnail.png"
        val uploadTask = thumbnailData?.let { storageReference.child(path).putFile(it) }
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
                            FirebaseCallback.onCallback("Success")

                        }
                        .addOnFailureListener { e ->
                            FirebaseCallback.onCallback("Fail")

                        }
                }

            }
                .addOnFailureListener {
                    FirebaseCallback.onCallback("Fail")

                }
        }
    }

    fun registerUser(email: String, password: String,name:String,FirebaseCallback: FirebaseCallbackUpdate){
        var auth: FirebaseAuth
        auth= FirebaseAuth.getInstance()
        var databaseReference= FirebaseDatabase.getInstance().reference.child("profile")
        var email=email.trim { it<= ' ' }
        var pass=password.trim{ it<= ' '}
        var name=name.trim{ it<= ' '}

        auth.createUserWithEmailAndPassword(email,pass)
            //Neu tao duoc thanh cong
            .addOnCompleteListener{
                if( it.isSuccessful){
                    val currentUser=auth.currentUser
                    //Lay instance cua user dang vao
                    val currentUserDB=databaseReference?.child(currentUser?.uid!!)
                    //Dua thong tin co ban luc resgister vao database instance
                    currentUserDB?.child("Email")?.setValue(email)
                    currentUserDB?.child("UID")?.setValue(currentUser?.uid!!)
                    currentUserDB?.child("UserName")?.setValue(name)
                    currentUserDB?.child("Type")?.setValue("User")
                    currentUserDB?.child("status")?.setValue("Active")
                    currentUserDB?.child("reason")?.setValue("None")

                    FirebaseCallback.onCallback("Success")
                }else{
                    FirebaseCallback.onCallback("Fail")

                }
            }
    }

    fun getBannedStatus(UID:String ,FirebaseCallback: FirebaseCallbackUser){
        var databaseReference: DatabaseReference =FirebaseDatabase.getInstance().reference!!.child("profile")
        databaseReference?.child(UID)?.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = userItem(
                    snapshot.child("UID").value as String?,
                    snapshot.child("Type").value as String?,
                    snapshot.child("UserName").value as String?,
                    snapshot.child("status").value as String?,
                    snapshot.child("reason").value as String?
                )
                FirebaseCallback.onCallback( user)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
    fun getFollowStatus(UID:String ,comicID:String,FirebaseCallback: FirebaseCallbackUpdate){
        FirebaseDatabase.getInstance().getReference("profile/${UID}/followComic").child("${comicID}")!!
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        FirebaseCallback.onCallback("Yes")
                    }
                    else {
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("No need")
                }
            })

    }
    fun getLikeStatus(UID:String ,comicID:String,FirebaseCallback: FirebaseCallbackUpdate){
        FirebaseDatabase.getInstance().getReference("comic/${comicID}/likePerson").child("${UID}")!!
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        FirebaseCallback.onCallback("Yes")
                    }
                    else {
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("No need")
                }
            })

    }

    fun loginUser(email: String, password: String ,FirebaseCallback: FirebaseCallbackUpdate){
        var auth: FirebaseAuth
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    FirebaseCallback.onCallback(auth?.uid!!)
                }else{
                    FirebaseCallback.onCallback("Fail")
                }
            }
    }

    fun forgetPass(email: String ,FirebaseCallback: FirebaseCallbackUpdate){
        var auth: FirebaseAuth
        auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener{ task ->
                if ( task.isSuccessful ){
                    FirebaseCallback.onCallback("Success")
                }else {
                    FirebaseCallback.onCallback("Fail")
                }
            }
    }

    fun readCommentArray(comicID:String,FirebaseCallback: FirebaseCallbackCommentList ){
        var ListComment= ArrayList<commentItem>()

        val ref = FirebaseDatabase.getInstance().getReference("comic/$comicID/comment")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ListComment.clear()
                for (com in snapshot.children){
                    val modelcomment = com.getValue(commentItem::class.java)
                    if (modelcomment != null){
                        ListComment.add(modelcomment)
                    }
                }
                FirebaseCallback.onCallback(ListComment)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun getFollowComic(UID:String,FirebaseCallback: FirebaseCallbackComicItem ){
        var comicArray= ArrayList<comicItem>()
        var followRef= FirebaseDatabase.getInstance().getReference("profile/${UID}/followComic")
        var followList= ArrayList<String>()
        followRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    followList.clear()
                    for( id in snapshot.children){
                        followList.add(id.getValue().toString())
                        val ref= FirebaseDatabase.getInstance().getReference("comic")
                        ref.addListenerForSingleValueEvent(object: ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                //Xoa list trc khi them vao moi lan vao app
                                comicArray.clear()
                                for (item in snapshot.children){
                                    val modelComic = item.getValue(comicItem::class.java)
                                    if(modelComic?.id in followList){
                                        comicArray.add(modelComic!!)
                                    }
                                }
                                FirebaseCallback.onCallback(comicArray)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }else{
                    comicArray.clear()
                    FirebaseCallback.onCallback(comicArray)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}