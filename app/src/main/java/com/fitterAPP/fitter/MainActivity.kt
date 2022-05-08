package com.fitterAPP.fitter

import android.content.Intent
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
    private lateinit var ttprofile : LinearLayout
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

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.FragmentContainer, MyFitnessCards() )
        transaction.commit()
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
        transaction.addToBackStack("FindProfileFragment")
        transaction.replace(R.id.FragmentContainer, findprofile() )
        transaction.commit()
    }

    fun showRecap(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack("TimeRacapFragment")
        transaction.replace(R.id.FragmentContainer, TimeRacap() )
        transaction.commit()
    }

    fun showCalendar(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack("CalendarFragment")
        transaction.replace(R.id.FragmentContainer, Calendar() )
        transaction.commit()
    }

    fun showMyFitnessCards(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack("MyFitnessCardsFragment")
        transaction.replace(R.id.FragmentContainer, MyFitnessCards() )
        transaction.commit()
    }

    fun logout(){
        val I : Intent =  Intent(this, LoginActivity::class.java)
        I.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(I)
    }

}