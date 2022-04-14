package com.khtn.ratevid.model

class commentItem {
    var content:String?=""
    var userid:String?=""
    var timestamp:String?=""
    var DeleteTimestamp:String ?=""
    constructor(){}

    constructor(comment:String?, user:String?, time:String?,DeleteTimestamp:String?){
        this.content = comment
        this.userid = user
        this.timestamp = time
        this.DeleteTimestamp=DeleteTimestamp
    }
}