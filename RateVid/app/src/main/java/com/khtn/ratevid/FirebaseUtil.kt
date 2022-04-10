package com.khtn.ratevid

import android.net.Uri
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.khtn.ratevid.model.comicItem
import com.khtn.ratevid.model.picItem
import com.khtn.ratevid.model.userItem
import kotlinx.android.synthetic.main.activity_detail_comic_admin.*

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
}