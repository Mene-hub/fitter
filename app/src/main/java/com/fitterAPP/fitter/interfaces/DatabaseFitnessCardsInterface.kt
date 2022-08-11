package com.fitterAPP.fitter.interfaces

import com.fitterAPP.fitter.classes.Exercise
import com.fitterAPP.fitter.classes.FitnessCard
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Interface that defines the method that will be used to communicate with the FitnessCard section of the database
 * @author Daniel Satriano
 * @since 04/06/2022
 */
interface DatabaseFitnessCardsInterface {

    /**
     * Reference to the database
     * @author Daniel Satriano
     * @since 04/06/2022
     */
    val database : FirebaseDatabase



    /**
     * Method that will be used to get a specified card value listener
     * @author Daniel Satriano
     * @since 11/08/2022
     */
    fun setSingleValueEventListener(databaseRef: DatabaseReference, userID: String, fitnessCardListener: ValueEventListener)


    /**
     * Method that will be used to get a specified card child listener
     * @author Daniel Satriano
     * @since 04/06/2022
     */
    fun setFitnessCardChildListener(databaseRef : DatabaseReference, userID : String, fitnessCardListener: ChildEventListener)

    /**
     * Method that will create or overwrite a FitnessCard stored in the database
     * @author Daniel Satriano
     * @since 04/06/2022
     */
    fun setFitnessCardItem(databaseRef : DatabaseReference, userID : String, card : FitnessCard)

    /**
     * Method used to remove an athlete from the database, its used if the user decides to delete his account
     * @author Daniel Satriano
     * @since 04/06/2022
     */
    fun removeFitnessCard(databaseRef : DatabaseReference, userID: String, key : String)

    /**
     * Method used to remove all the FitnessCard associated with the user
     * @author Daniel Satriano
     * @since 04/06/2022
     */
    fun removeAllFitnessCard(databaseRef: DatabaseReference, userID: String)


    /**
     * Method that will create a value listener for a specific card
     * @author Daniel Satriano
     * @since 25/07/2022
     */
    fun setFitnessCardValueListener(databaseRef: DatabaseReference, userID: String, fitnessCard: FitnessCard, fitnessCardListener: ValueEventListener)


}