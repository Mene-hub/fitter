package com.fitterAPP.fitter

import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var listView : RecyclerView
    private lateinit var user : Athlete

    //Bottom sheet dialog
    private lateinit var menuiv : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //grab event from companion class RealTimeDBHelper
        RealTimeDBHelper.readToDoItems(getAthleteEventListener())

        //Bottom sheet dialog
        menuiv = findViewById(R.id.MenuBt)
        menuiv.setOnClickListener {
            val modalBottomSheet = profileMenu()
            modalBottomSheet.show(supportFragmentManager, profileMenu.TAG)
        }

    }


    private fun getAthleteEventListener(): ChildEventListener {
        val childEventListener = object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        return childEventListener
    }


    fun showSearch(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.searchprofileFragmentContainer, findprofile() )
        transaction.commit()
    }
}