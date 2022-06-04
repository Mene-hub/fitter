package com.fitterAPP.fitter.databases

import com.fitterAPP.fitter.BuildConfig
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.interfaces.DatabaseFitnessCardsInterface
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Static database helper for managing fitnessCards, utilizing [DatabaseFitnessCardsInterface]
 * @author Daniel Satriano
 * @since 04/06/2022
 */
class StaticFitnessCardDatabase {
    companion object : DatabaseFitnessCardsInterface{
        override val database: FirebaseDatabase = FirebaseDatabase.getInstance(BuildConfig.database)

        override fun getFitnessCardChildListener(databaseRef : DatabaseReference, userID : String, fitnessCardListener: ChildEventListener){
            databaseRef.child(userID).addChildEventListener(fitnessCardListener)
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