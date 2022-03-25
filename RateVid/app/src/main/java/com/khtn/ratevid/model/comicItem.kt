package com.khtn.ratevid.model

 class comicItem{
     var thumbnail: String?=""
     var name: String?=""
     constructor(){}
     constructor(name: String?, thumbnail: String?){
         this.name=name
         this.thumbnail=thumbnail
     }
 }