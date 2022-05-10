package com.fitterAPP.fitter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * @author Daniel Satriano
 * @author Claudio Menegotto
 * Main activity for the android app, in this activity you'll be able to access all your data and your fitness cards.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var listView : RecyclerView
    private lateinit var user : Athlete
    private lateinit var auth : FirebaseAuth
    private lateinit var currentUser : FirebaseUser

    //Bottom sheet dialog
    private lateinit var ttprofile : LinearLayout
    private lateinit var menuiv : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //FIREBASE ACCOUNT
        auth = Firebase.auth
        currentUser = auth.currentUser!!
        Log.d("TAG", auth.currentUser?.displayName.toString())

        //grab event from companion class RealTimeDBHelper
        RealTimeDBHelper.readToDoItems(getAthleteEventListener())

        //Bottom sheet dialog
        menuiv = findViewById(R.id.MenuBt)
        menuiv.setOnClickListener {
            val modalBottomSheet = profileMenu()
            modalBottomSheet.show(supportFragmentManager, profileMenu.TAG)
        }

        //default fragment is the fitness cards
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


    //METODO PER LA VISUALIZZAZIONE DEL FRAGMENT DI RICERCA
    fun showSearch(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack("FindProfileFragment")
        transaction.replace(R.id.FragmentContainer, findprofile() )
        transaction.commit()
    }

    //METODO PER LA VISUALIZZAZIONE DEL FRAGMENT DI RECAP
    fun showRecap(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack("TimeRacapFragment")
        transaction.replace(R.id.FragmentContainer, TimeRacap() )
        transaction.commit()
    }

    //METODO PER LA VISUALIZZAZIONE DEL FRAGMENT DEL CALENDARIO
    fun showCalendar(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack("CalendarFragment")
        transaction.replace(R.id.FragmentContainer, Calendar() )
        transaction.commit()
    }

    //METODO PER LA VISUALIZZAZIONE DEL FRAGMENT CON LA LISTA DI SCHEDE SALVATE
    fun showMyFitnessCards(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack("MyFitnessCardsFragment")
        transaction.replace(R.id.FragmentContainer, MyFitnessCards() )
        transaction.commit()
    }

    //METODO PER LA DISCONNESSIONE DELL'ACCOUNT
    fun logout(){

        Log.d("MainWindow-Signout", auth.toString())
        if(auth != null){
            Log.d("MainWindow-Signout", "Sloggato")
            auth.signOut()
        }

        val I : Intent =  Intent(this, LoginActivity::class.java)
        I.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(I)
    }

}