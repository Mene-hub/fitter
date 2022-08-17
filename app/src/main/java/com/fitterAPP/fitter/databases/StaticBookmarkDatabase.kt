package com.fitterAPP.fitter.databases

import com.fitterAPP.fitter.classes.ApiKeyRetriever
import com.fitterAPP.fitter.classes.BookmarkCard
import com.fitterAPP.fitter.interfaces.DatabaseBookmarkInterface
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StaticBookmarkDatabase {
    companion object : DatabaseBookmarkInterface{
        override val database: FirebaseDatabase = FirebaseDatabase.getInstance(ApiKeyRetriever.getDatabase())

        /**
         * Allows you to create a value listener to the database
         * @author Daniel Satriano
         * @since 3/08/2022
         */
        override fun setBookmarkListener(databaseRef: DatabaseReference, currentUserUID: String, BookmarkListener: ValueEventListener) {
            databaseRef.child(currentUserUID).addListenerForSingleValueEvent(BookmarkListener)
        }

        /**
         * Allows you to update the mutable list inside the database of the bookmarks
         * @author Daniel Satriano
         * @since 3/08/2022
         */
        override fun updateBookmarkList(databaseRef: DatabaseReference, currentUserUID: String, bookmark:  MutableList<BookmarkCard>) {
            databaseRef.child(currentUserUID).setValue(bookmark)
        }

        override fun removeAllUserBookmark(databaseRef: DatabaseReference, currentUserUID: String) {
            databaseRef.child(currentUserUID).removeValue()
        }


    }
}