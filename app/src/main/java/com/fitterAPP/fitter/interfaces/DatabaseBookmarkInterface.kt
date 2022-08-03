package com.fitterAPP.fitter.interfaces

import com.fitterAPP.fitter.classes.BookmarkCard
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

interface DatabaseBookmarkInterface{
    /**
     * Reference to the database
     * @author Daniel Satriano
     * @since 04/06/2022
     */
    val database : FirebaseDatabase

    /**
     * Used to set the valueListener
     * @author Daniel Satriano
     * @since 04/06/2022
     */
    fun setBookmarkListener(databaseRef : DatabaseReference, currentUserUID : String, BookmarkListener : ValueEventListener)

    /**
     * Method used to update the list on the database, which means add and remove bookmarks
     * @author Daniel Satriano
     * @since 04/06/2022
     */
    fun updateBookmarkList(databaseRef : DatabaseReference, currentUserUID: String , bookmark : MutableList<BookmarkCard>)

}