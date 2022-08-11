package com.fitterAPP.fitter.databases

import com.fitterAPP.fitter.BuildConfig
import com.fitterAPP.fitter.classes.ApiKeyRetriever
import com.fitterAPP.fitter.classes.Exercise
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.interfaces.DatabaseFitnessCardsInterface
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Static database helper for managing fitnessCards, utilizing [DatabaseFitnessCardsInterface]
 * @author Daniel Satriano
 * @since 04/06/2022
 */
class StaticFitnessCardDatabase {
    companion object : DatabaseFitnessCardsInterface{
        override val database: FirebaseDatabase = FirebaseDatabase.getInstance(ApiKeyRetriever.getDatabase())


        override fun setSingleValueEventListener(databaseRef: DatabaseReference, userID: String, fitnessCardListener: ValueEventListener) {
            databaseRef.child(userID).addListenerForSingleValueEvent(fitnessCardListener)
        }

        override fun setFitnessCardChildListener(databaseRef : DatabaseReference, userID : String, fitnessCardListener: ChildEventListener){
            databaseRef.child(userID).addChildEventListener(fitnessCardListener)
        }

        override fun setFitnessCardValueListener(databaseRef: DatabaseReference, userID: String, fitnessCard: FitnessCard, fitnessCardListener: ValueEventListener){
            databaseRef.child(userID).child(fitnessCard.key).addValueEventListener(fitnessCardListener)
        }

        override fun setFitnessCardItem(databaseRef : DatabaseReference, userID : String, card : FitnessCard){
            val currentDate = card.key
            databaseRef.child(userID).child(currentDate).setValue(card)
        }

        override fun removeFitnessCard(databaseRef : DatabaseReference, userID: String, key : String){
            databaseRef.child(userID).child(key).removeValue()
        }

        override fun removeAllFitnessCard(databaseRef: DatabaseReference, userID: String){
            databaseRef.child(userID).removeValue()
        }

    }
}