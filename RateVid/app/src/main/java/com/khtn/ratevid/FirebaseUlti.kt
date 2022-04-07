package com.khtn.ratevid

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.khtn.ratevid.model.comicItem
import com.khtn.ratevid.model.userItem

object FirebaseUlti {
    interface FirebaseCallbackComicItem{
        fun onCallback(arrayComicItem:ArrayList<comicItem>)
    }
    interface FirebaseCallbackUserList{
        fun onCallback(arrayUserList:ArrayList<userItem>)

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
}