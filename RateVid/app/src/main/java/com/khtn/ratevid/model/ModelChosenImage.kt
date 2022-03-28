package com.khtn.ratevid.model

import android.net.Uri
import java.io.Serializable


class ModelChosenImage : Serializable {
    var number: Int?=0
    var imgURI:Uri ?=null
    var status:String?=""

    constructor(){}
    constructor(number: Int,imgURI:Uri,status:String?){
        this.number=number
        this.imgURI=imgURI
        this.status=status

    }
}