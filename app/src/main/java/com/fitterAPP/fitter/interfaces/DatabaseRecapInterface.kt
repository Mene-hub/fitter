package com.fitterAPP.fitter.interfaces

import com.fitterAPP.fitter.classes.DayRecap
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


/**
 * Interface that defines the method that will be used to communicate with the Athlete section of the database
 * @author Daniel Satriano
 * @since 16/07/2022
 */
interface DatabaseRecapInterface {

    /**
     * Reference to the database
     * @author Daniel Satriano
     * @since 16/07/2022
     */
    val database : FirebaseDatabase

    /**
     * Sets the child event listener for the database
     * @author Daniel Satriano
     * @since 16/07/2022
     */
    fun setRecapChildListener(databaseRef: DatabaseReference, userID: String, recapListener: ChildEventListener)

    /**
     * Enables to save a DayRecap object in the database
     * @author Daniel Satriano
     * @since 16/07/2022
     */
    fun setRecapItem(databaseRef: DatabaseReference, userID: String, cardID: String, recap: DayRecap)

    /**
     * Enables to remove a DayRecap object from the database
     * @author Daniel Satriano
     * @since 16/07/2022
     */
    fun removeRecap(databaseRef: DatabaseReference, userID: String, cardID: String, recapID: String)

}