package com.fitterAPP.fitter

import com.fitterAPP.fitter.Classes.Athlete
import com.fitterAPP.fitter.Classes.FitnessCard
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Daniel Satriano
 */
class RealTimeDBHelper(val database: DatabaseReference) {

    companion object {
        fun getDbURL() : String {
            return "https://fitter-8363a-default-rtdb.europe-west1.firebasedatabase.app/"
        }
    }

    //Reading from db
    fun readItems(athleteEventListener: ChildEventListener){
        database.addChildEventListener(athleteEventListener)
    }

    //Writing
    fun setAthleteItem(key : String, athlete : Athlete){
        database.child(key).setValue(athlete)
    }
    //Writing
    fun setFitnessCardItem(UID : String, card : FitnessCard){
        val currentDate = card.key
        database.child("$UID-FITNESSCARD").child(currentDate).setValue(card)
    }

    fun setFitnessCardItem(UID : String, card : MutableList<FitnessCard>){
        database.child("$UID-FITNESSCARD").setValue(card)
    }

    //Deleting entire node
    fun removeItem(key : String){
        database.child(key).removeValue()
    }

}

