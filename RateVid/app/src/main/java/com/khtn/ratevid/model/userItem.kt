package com.khtn.ratevid.model

import java.io.Serializable

class userItem : Serializable {
    var UID: String?=""
    var Type: String?=""
    var UserName: String?=""


    constructor(){}
    constructor(UID: String?, Type: String?,UserName:String?){
        this.UID=UID
        this.Type=Type
        this.UserName=UserName
    }
}