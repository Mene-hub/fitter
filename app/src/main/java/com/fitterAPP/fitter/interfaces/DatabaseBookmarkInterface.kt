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
     * 
     * @author Daniel Satriano
     * @since 04/06/2022
     */
    fun setBookmarkListener(databaseRef : DatabaseReference, currentUserUID : String, BookmarkListener : ValueEventListener)

    /**
     *
     * @author Daniel Satriano
     * @since 04/06/2022
     */
    fun updateBookmarkList(databaseRef : DatabaseReference, currentUserUID: String , bookmark : MutableList<BookmarkCard>)

}