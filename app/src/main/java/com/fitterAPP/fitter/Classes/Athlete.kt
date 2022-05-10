package com.fitterAPP.fitter.Classes

import android.net.Uri

data class Athlete(var UID : String , var username : String?, var profilePic : Uri?){
    constructor() : this("", "", Uri.EMPTY)

    fun SetNewValue(user : Athlete){
        this.UID = user.UID
        this.username = user.username
        this.profilePic = user.profilePic
    }

    companion object{
        var UID : String = ""
        var username : String = ""
        var profilePic : Uri = Uri.EMPTY

        fun setValues(UID: String, username: String?, profilePic: Uri?){
            this.UID = UID
            this.username = username ?: "DEFAULT USERNAME"
            this.profilePic = profilePic ?: Uri.EMPTY
        }
    }
}