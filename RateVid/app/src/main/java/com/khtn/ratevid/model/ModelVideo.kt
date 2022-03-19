package com.khtn.ratevid.model

class ModelVideo {
    //Bien su dung giong trong firebase cua review
    var ID:String?=""
    var Info:String?=""
    var Description:String?=""
    var Like:Int?=null
    var IsCensor:Int?=null
    var Name:String?=""
    var Video:String?=""
    var RateAVG:Float?=null
    var Type:String?=""
    //Xay dung ham dung constructor

    constructor(){

    }

    constructor(ID: String?,Info:String?, Description: String?, Like: Int?, Name: String?, Video: String?,Rate:Float?,Type:String?,IsCensor:Int?) {
        this.ID = ID
        this.Info=Info
        this.Description = Description
        this.Like = Like
        this.Name = Name
        this.Video = Video
        this.RateAVG=Rate
        this.Type=Type
        this.IsCensor=IsCensor
    }

}