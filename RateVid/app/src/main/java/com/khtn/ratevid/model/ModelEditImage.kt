package com.khtn.ratevid.model

import android.net.Uri
import java.io.Serializable

class ModelEditImage : Serializable {
    var number: Int?=0
    var imgURL: String?=""
    var status:String?=""
    var imgURI: Uri?=null

    constructor(){}
    constructor(number: Int?, imgURL: String?, status:String?){
        this.number=number
        this.imgURL=imgURL
        this.status=status
    }
    constructor(number: Int?, imgURI: Uri?, status:String?){
        this.status=status
        this.number=number
        this.imgURI=imgURI
    }
    constructor(imgURI:Uri?){

    }
    fun addNumStatus(number: Int?, status: String?){
        this.number=number
        this.status=status
    }
}