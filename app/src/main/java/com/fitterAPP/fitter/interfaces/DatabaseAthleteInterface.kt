package com.fitterAPP.fitter.interfaces

import com.fitterAPP.fitter.classes.Athlete
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Interface that defines the method that will be used to communicate with the Athlete section of the database
 * @author Daniel Satriano
 * @since 04/06/2022
 */
interface DatabaseAthleteInterface {

    /**
     * Reference to the database
     * @author Daniel Satriano
     * @since 04/06/2022
     */
    val database : FirebaseDatabase

    /**
     * Method that will be used to get a specified athlete value listener
     * @author Daniel Satriano
     * @since 04/06/2022
     */
    fun setAthleteValueListener(databaseRef : DatabaseReference, userID : String , athleteListener : ValueEventListener)

    /**
     * Method that will overwrite a current user data stored in the database, used to change information about him like profilePicture or so.
     * @author Daniel Satriano
     * @since 04/06/2022
     */
    fun setAthleteItem(databaseRef : DatabaseReference, key : String, athlete : Athlete)

    /**
     * Method used to delete an athlete from the database
     * @author Daniel Satriano
     * @since 04/06/2022
     */
    fun removeAthlete(databaseRef : DatabaseReference, key : String)
}