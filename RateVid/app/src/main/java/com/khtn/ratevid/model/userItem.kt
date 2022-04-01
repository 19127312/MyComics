package com.khtn.ratevid.model

import java.io.Serializable

class userItem : Serializable {
    var UID: String?=""
    var Type: String?=""
    var UserName: String?=""
    var status: String?=""
    var reason: String?=""


    constructor(){}
    constructor(UID: String?, Type: String?,UserName:String?,status:String?,reason:String?){
        this.status=status
        this.reason=reason
        this.UID=UID
        this.Type=Type
        this.UserName=UserName
    }
}