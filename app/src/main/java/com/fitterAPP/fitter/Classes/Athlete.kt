package com.fitterAPP.fitter.Classes

import android.net.Uri

/**
 * @author Daniel Satriano
 */
data class Athlete(var UID : String , var username : String?, var profilePic : String?, var profileBio:String?, var spotifyplayList:String){
    constructor() : this("", "", "", "", "")

    fun SetNewValue(user : Athlete){
        this.UID = user.UID
        this.username = user.username
        this.profilePic = user.profilePic
    }

    fun SetNewValue(uid : String, user : String, pic : String = ""){
        this.UID = uid
        this.username = user
        this.profilePic = pic
    }

    fun changeUsername(username : String){
        this.username = username
    }

    companion object{
        var UID : String = ""
        var username : String = ""
        var profilePic : String = ""

        fun setValues(UID: String, username: String?, profilePic: String?){
            this.UID = UID
            this.username = username ?: "DEFAULT USERNAME"
            this.profilePic = profilePic ?: ""
        }
    }
}