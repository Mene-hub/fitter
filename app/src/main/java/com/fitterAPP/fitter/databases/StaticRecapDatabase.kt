package com.fitterAPP.fitter.databases

import com.fitterAPP.fitter.classes.ApiKeyRetriever
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.MonthlyRecap
import com.fitterAPP.fitter.interfaces.DatabaseRecapInterface
import com.google.firebase.database.*

class StaticRecapDatabase {
    companion object : DatabaseRecapInterface{

        override val database: FirebaseDatabase = FirebaseDatabase.getInstance(ApiKeyRetriever.getDatabase())

        override fun setRecapChildListener(databaseRef: DatabaseReference, userID: String, cardID : String, recapListener: ChildEventListener) {
            databaseRef.child(userID).child(cardID).addChildEventListener(recapListener)
        }

        /**
         * Adds a new recap to the database
         * @author Daniel Satriano
         * @param userID The user's ID
         * @param recap The recap to be added
         * @param databaseRef The database reference
         * @since 16/07/2022
         */
        override fun setRecapItem(databaseRef: DatabaseReference, userID: String, recap: MonthlyRecap) {
            databaseRef.child(userID).child(recap.cardKey).child(recap.month).setValue(recap)
        }

        /**
         * Creates a new ListenerForSingleValueEvent for the specified node
         * @author Daniel Satriano
         * @since 30/07/2022
         * @param databaseRef The database reference
         * @param userID The user's ID
         * @param valueListener the object that will be listening database calls
         * @param cardID The key/id of the card
         */
        override fun setSingleListenerToCardRecap(databaseRef: DatabaseReference, userID: String,cardID : String,  valueListener : ValueEventListener ){
            val ref = databaseRef.child(userID).child(cardID)
            ref.addListenerForSingleValueEvent(valueListener)
        }


        /**
         * Is used to remove ALL the recaps from the given user node + the node itself
         * @author Daniel Satriano
         * @since 17/08/2022
         * @param databaseRef The database reference
         * @param userID The user's ID
         */
        override fun removeAllRecap(databaseRef: DatabaseReference, userID: String) {
            databaseRef.child(userID).removeValue()
        }

        /**
         * Using the recap ID, removes the given recap from the database
         * @author Daniel Satriano
         * @param userID The user's ID
         * @param databaseRef The database reference
         * @param cardID The fitness card ID
         * @param month Month to check
         * @param valueListener the object that will be listening database calls
         * @since 16/07/2022
         */
        fun setSingleListenerForMonth(databaseRef: DatabaseReference, userID: String, cardID: String, month : String, valueListener: ValueEventListener){
            databaseRef.child(userID).child(cardID).child(month).addListenerForSingleValueEvent(valueListener)
        }


        /**
         * Using the recap ID, removes the given recap from the database
         * @author Daniel Satriano
         * @param userID The user's ID
         * @param databaseRef The database reference
         * @param cardID The fitness card ID
         * @since 16/07/2022
         */
        override fun removeRecap(databaseRef: DatabaseReference, userID: String, cardID: String) {
            databaseRef.child(userID).child(cardID).removeValue()
        }
    }
}