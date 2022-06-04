package com.fitterAPP.fitter.databases

import com.fitterAPP.fitter.BuildConfig
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.FitnessCard
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

/**
 * @author Daniel Satriano
 */
/*
class RealTimeDBHelper(val database: DatabaseReference) {

    companion object {
        fun getDbURL() : String {
            return BuildConfig.database
        }
    }

    //Reading from db
    fun readItems(fitnessCardListener: ChildEventListener){
        database.addChildEventListener(fitnessCardListener)
    }

    //Reading from db
    fun readItem(athleteListener : ValueEventListener){
        database.addValueEventListener(athleteListener)
    }

    //Writing
    fun setAthleteItem(key : String, athlete : Athlete){
        database.child(key).setValue(athlete)
    }
    //Writing
    fun setFitnessCardItem(card : FitnessCard){
        val currentDate = card.key
        database.child(currentDate).setValue(card)
    }

    fun overrideCurrentUser(athlete: Athlete){
        database.setValue(athlete)
    }

    //Deleting entire node
    fun removeItem(key : String){
        database.child(key).removeValue()
    }

}
*/
