package com.fitterAPP.fitter.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Athlete data class for the database
 * @author Daniel Satriano
 */
@Parcelize
data class Athlete(var UID : String, var username : String?, var profilePic : String?, var profileBio : String?, var spotifyPlayList : String?) : Parcelable {
    constructor() : this("", "", "", "", "")

    fun SetNewValue(user : Athlete){
        this.UID = user.UID
        this.username = user.username
        this.profilePic = user.profilePic
        this.profileBio = user.profileBio
        this.spotifyPlayList = user.spotifyPlayList
    }

    companion object{
        var UID : String = ""
        var username : String = ""
        var profilePic : String = ""
        var profileBio : String = ""
        var spotifyplayList : String = ""

        fun setValues(user : Athlete){
            this.UID = user.UID
            this.username = user.username ?: "DEFAULT USERNAME"
            this.profilePic = user.profilePic ?: ""
            this.profileBio = user.profileBio ?: ""
            this.spotifyplayList = user.spotifyPlayList ?: ""
        }
    }

    override fun toString(): String {
        return "UID: ${this.UID}\nUSERNAME: ${this.username}"
    }
}