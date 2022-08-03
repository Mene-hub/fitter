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

        override fun setBookmarkListener(databaseRef: DatabaseReference, currentUserUID: String, BookmarkListener: ValueEventListener) {
            databaseRef.child(currentUserUID).addValueEventListener(BookmarkListener)
        }

        override fun updateBookmarkList(databaseRef: DatabaseReference, currentUserUID: String, bookmark:  MutableList<BookmarkCard>) {
            databaseRef.child(currentUserUID).setValue(bookmark)
        }


    }
}