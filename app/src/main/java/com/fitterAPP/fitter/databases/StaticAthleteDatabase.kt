package com.fitterAPP.fitter.databases

import com.fitterAPP.fitter.BuildConfig
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.interfaces.DatabaseAthleteInterface
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Static database helper for managing fitnessCards, utilizing [DatabaseAthleteInterface]
 * @author Daniel Satriano
 * @since 04/06/2022
 */
class StaticAthleteDatabase{
    companion object : DatabaseAthleteInterface{
        override val database: FirebaseDatabase = FirebaseDatabase.getInstance(BuildConfig.database)

        override fun getAthleteValueListener(databaseRef : DatabaseReference, userID : String , athleteListener : ValueEventListener){
            databaseRef.child(userID).addValueEventListener(athleteListener)
        }

        override fun setAthleteItem(databaseRef : DatabaseReference, key : String, athlete : Athlete){
            databaseRef.child(key).setValue(athlete)
        }

        override fun removeAthlete(databaseRef : DatabaseReference, key : String){
            databaseRef.child(key).removeValue()
        }

    }
}