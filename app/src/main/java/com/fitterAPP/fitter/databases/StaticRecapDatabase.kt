package com.fitterAPP.fitter.databases

import com.fitterAPP.fitter.classes.ApiKeyRetriever
import com.fitterAPP.fitter.classes.DayRecap
import com.fitterAPP.fitter.interfaces.DatabaseRecapInterface
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StaticRecapDatabase {
    companion object : DatabaseRecapInterface{

        override val database: FirebaseDatabase = FirebaseDatabase.getInstance(ApiKeyRetriever.getDatabase())

        override fun setRecapChildListener(databaseRef: DatabaseReference, userID: String, recapListener: ChildEventListener) {
            databaseRef.child(userID).addChildEventListener(recapListener)
        }

        /**
         * Adds a new recap to the database
         * @author Daniel Satriano
         * @param userID The user's ID
         * @param recap The recap to be added
         * @param databaseRef The database reference
         * @param cardID The fitness card ID
         * @since 16/07/2022
         */
        override fun setRecapItem(databaseRef: DatabaseReference, userID: String, recap: DayRecap) {
            databaseRef.child(userID).child(recap.cardKey).child(recap.key).setValue(recap)
        }




        /**
         * Using the recap ID, removes the given recap from the database
         * @author Daniel Satriano
         * @param userID The user's ID
         * @param recapID The recap ID
         * @param databaseRef The database reference
         * @param cardID The fitness card ID
         * @since 16/07/2022
         */
        override fun removeRecap(databaseRef: DatabaseReference, userID: String, cardID: String, recapID: String) {
            databaseRef.child(userID).child(cardID).child(recapID).removeValue()
        }
    }
}