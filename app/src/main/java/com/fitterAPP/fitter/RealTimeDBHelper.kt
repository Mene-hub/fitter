package com.fitterAPP.fitter

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.FirebaseDatabase

class RealTimeDBHelper {
    companion object{

        private val database = FirebaseDatabase
            .getInstance("https://fitter-8363a-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("FITNESS_CARDS")

        //Reading from db
        fun readToDoItems(athleteEventListener: ChildEventListener){
            database.addChildEventListener(athleteEventListener)
        }

        //Writing
        fun setToDoItem(key : String, athlete : Athlete){
            database.child(key).setValue(athlete)
        }

        //Deleting entire node
        fun removeToDoItem(key : String){
            database.child(key).removeValue()
        }
    }
}

