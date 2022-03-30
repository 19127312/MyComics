package com.khtn.ratevid.model

class commentItem {
    var content:String?=""
    var userid:String?=""
    var timestamp:String?=""
    constructor(){}

    constructor(comment:String?, user:String?, time:String?){
        this.content = comment
        this.userid = user
        this.timestamp = time
    }
}