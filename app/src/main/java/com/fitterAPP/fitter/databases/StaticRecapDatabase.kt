package com.fitterAPP.fitter.databases

import android.renderscript.Sampler
import com.fitterAPP.fitter.classes.ApiKeyRetriever
import com.fitterAPP.fitter.classes.DayRecap
import com.fitterAPP.fitter.interfaces.DatabaseRecapInterface
import com.google.firebase.database.*

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
         * @since 16/07/2022
         */
        override fun setRecapItem(databaseRef: DatabaseReference, userID: String, recap: DayRecap) {
            databaseRef.child(userID).child(recap.cardKey).child(recap.key).setValue(recap)
        }

        /**
         * Creates a new ListenerForSingleValueEvent for the specified node
         * @author Daniel Satriano
         * @since 30/07/2022
         * @param databaseRef The database reference
         * @param userID The user's ID
         * @param recap The recap to be added
         * @param valueListener the object that will be listening database calls
         */
        override fun setSingleListenerToCardRecap(databaseRef: DatabaseReference, userID: String, recap :DayRecap , valueListener : ValueEventListener ){
            val query : Query = databaseRef.child(userID).child(recap.cardKey).orderByChild("key").equalTo(recap.key)
            query.addListenerForSingleValueEvent(valueListener)
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