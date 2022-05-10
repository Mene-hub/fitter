package com.fitterAPP.fitter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.appcompat.app.AppCompatActivity
import com.fitterAPP.fitter.Classes.Athlete
import com.fitterAPP.fitter.FragmentControlers.*
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
    private val _REFERENCE = "USERS"
    private val user = Athlete()
    private lateinit var auth : FirebaseAuth
    private lateinit var currentUser : FirebaseUser
    private lateinit var databaseHelper : RealTimeDBHelper

    //Bottom sheet dialog
    private lateinit var menuiv : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //FIREBASE ACCOUNT
        auth = Firebase.auth
        databaseHelper = RealTimeDBHelper(_REFERENCE)
        databaseHelper.readItems(getAthleteEventListener())


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

    /**
     * @author Daniel Satriano
     */
    override fun onStart() {
        super.onStart()
        auth.currentUser?.reload()
        Log.i("mainwindow", auth.currentUser?.displayName.toString())

        if(auth.currentUser != null){
            currentUser = auth.currentUser!!
            user.SetNewValue(Athlete(currentUser.uid, currentUser.displayName, currentUser.photoUrl))
            Athlete.setValues(currentUser.uid, currentUser.displayName, currentUser.photoUrl)

            //IF THE USER COMES FROM THE REGISTRATION FORM THEN IT HAS TO SAVE THE DATA TO THE DB, OOTHERWISE IF FROM LOGIN FORM  THE INFOS WILL GET GRABBED FROM
            if(intent.extras!!.getBoolean("HASTOSAVE")){
                Log.d("MainWindow-Signout", "Entro")
                databaseHelper.setAthleteItem(user.UID,user)
            }
        }
    }

    /**
     * @author Claudio Menegotto
     * METODO PER LA VISUALIZZAZIONE DEL FRAGMENT DI RICERCA
     */
    fun showSearch(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack("FindProfileFragment")
        transaction.replace(R.id.FragmentContainer, findprofile() )
        transaction.commit()
    }
    /**
     * @author Claudio Menegotto
     * METODO PER LA VISUALIZZAZIONE DEL FRAGMENT DI RECAP
     */
    fun showRecap(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack("TimeRacapFragment")
        transaction.replace(R.id.FragmentContainer, TimeRacap() )
        transaction.commit()
    }
    /**
     * @author Claudio Menegotto
     * METODO PER LA VISUALIZZAZIONE DEL FRAGMENT DEL CALENDARIO
     */
    fun showCalendar(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack("CalendarFragment")
        transaction.replace(R.id.FragmentContainer, Calendar() )
        transaction.commit()
    }
    /**
     * @author Claudio Menegotto
     * METODO PER LA VISUALIZZAZIONE DEL FRAGMENT CON LA LISTA DI SCHEDE SALVATE
     */
    fun showMyFitnessCards(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack("MyFitnessCardsFragment")
        transaction.replace(R.id.FragmentContainer, MyFitnessCards() )
        transaction.commit()
    }
    /**
     * @author Claudio Menegotto
     * @author Daniel Satriano
     * METODO PER LA DISCONNESSIONE DELL'ACCOUNT
     */
    fun logout(){
        if(auth.currentUser != null){
            Log.d("MainWindow-Signout", "Sloggato")
            auth.signOut()
        }

        val I : Intent =  Intent(this, LoginActivity::class.java)
        I.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(I)
    }

    /**
     *  @author Daniel Satriano
     *  Used to update - retrieve data from database
     */
    private fun getAthleteEventListener(): ChildEventListener {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(Athlete::class.java)
                //aggiungo nuova fitness card
                user.SetNewValue(item!!)
                Athlete.setValues(user.UID,user.username,user.profilePic)
                findViewById<TextView>(R.id.TV_Username).text = user.username
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(Athlete::class.java)
                user.SetNewValue(item!!)
                findViewById<TextView>(R.id.TV_Username).text = user.username
                Athlete.setValues(user.UID,user.username,user.profilePic)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                auth.signOut()
                val intent = Intent(this@MainActivity,LoginActivity::class.java)
                startActivity(intent)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //viene triggerato quando la locazione del child cambia
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MainActivity-Database-User", "postcomments:onCancelled", error.toException())
                Toast.makeText(this@MainActivity, "Failed to load comment.", Toast.LENGTH_SHORT).show()
            }
        }
        return childEventListener
    }
}