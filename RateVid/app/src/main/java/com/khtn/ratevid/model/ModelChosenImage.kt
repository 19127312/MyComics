package com.khtn.ratevid.model

import android.net.Uri
import java.io.Serializable


class ModelChosenImage : Serializable {
    var number: Int?=0
    var imgURI:Uri ?=null
    var status:String?=""
    var imgURL: String?=""

    constructor(){}
    constructor(number: Int,imgURI:Uri,status:String?){
        this.number=number
        this.imgURI=imgURI
        this.status=status

    }
    constructor(number: Int?, imgURL: String?, status:String?){
        this.number=number
        this.imgURL=imgURL
        this.status=status
    }
    fun addNumStatus(number: Int?, status: String?){
        this.number=number
        this.status=status
    }
}