package com.fitterAPP.fitter

import android.net.Uri


data class Athlete(var UID : String , var username : String?, var profilePic : Uri?){
    constructor() : this("", "", Uri.EMPTY)

}
