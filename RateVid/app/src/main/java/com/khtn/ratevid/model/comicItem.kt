package com.khtn.ratevid.model

import java.io.Serializable

class comicItem : Serializable {
     var thumbnail: String?=""
     var name: String?=""
     var id: String?=""
     var description: String?=""
     var author: String?=""
     var lastestChapter: Int?=1
    var likeNumber:Int ?=0

     constructor(){}
     constructor(name: String?, thumbnail: String?,id:String?,description: String?,author:String?,lastestChapter:Int?, likeNumber: Int?){
         this.name=name
         this.thumbnail=thumbnail
         this.id=id
         this.description=description
         this.author=author
         this.lastestChapter=lastestChapter
         this.likeNumber=likeNumber
     }
 }