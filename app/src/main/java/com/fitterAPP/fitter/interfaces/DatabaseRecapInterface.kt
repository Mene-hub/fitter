package com.fitterAPP.fitter.interfaces

import com.fitterAPP.fitter.classes.MonthlyRecap
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


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
    fun setRecapChildListener(databaseRef: DatabaseReference, userID: String, cardID : String,  recapListener: ChildEventListener)

    /**
     * Enables to save a DayRecap object in the database
     * @author Daniel Satriano
     * @since 16/07/2022
     */
    fun setRecapItem(databaseRef: DatabaseReference, userID: String, recap: MonthlyRecap)

    /**
     * Enables to remove a DayRecap object from the database
     * @author Daniel Satriano
     * @since 16/07/2022
     */
    fun removeRecap(databaseRef: DatabaseReference, userID: String, cardID: String)

    /**
     * Retrieves all the monthly recaps of the specific card
     * @author Daniel Satriano
     * @since 30/07/2022
     */
    fun setSingleListenerToCardRecap(databaseRef: DatabaseReference, userID: String,cardID : String,  valueListener : ValueEventListener)


    /**
     * Is used to remove ALL the recaps from the given user node + the node itself
     * @author Daniel Satriano
     * @since 17/08/2022
     */
    fun removeAllRecap(databaseRef: DatabaseReference, userID: String)

}