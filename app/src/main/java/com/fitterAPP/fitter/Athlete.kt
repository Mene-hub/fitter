package com.fitterAPP.fitter

import android.net.Uri


data class Athlete(var firstName : String, var lastName : String, var username : String, var profilePic : String){
    constructor() : this("", "", "", "")

    fun getUri() : Uri{
        return Uri.parse(profilePic)
    }
}
