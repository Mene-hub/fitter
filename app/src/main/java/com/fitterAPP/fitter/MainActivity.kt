package com.fitterAPP.fitter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.fitterAPP.fitter.Classes.Athlete
import com.fitterAPP.fitter.FragmentControlers.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
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
    private var dbReference : DatabaseReference = FirebaseDatabase.getInstance(RealTimeDBHelper.getDbURL()).getReference(_REFERENCE)
    //Bottom sheet dialog
    private lateinit var menuiv : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //FIREBASE ACCOUNT
        auth = Firebase.auth
        databaseHelper = RealTimeDBHelper(dbReference)

        //Bottom sheet dialog
        menuiv = findViewById(R.id.MenuBt)
        menuiv.setOnClickListener {
            val modalBottomSheet = profileMenu()
            modalBottomSheet.show(supportFragmentManager, profileMenu.TAG)
        }

    }

    /**
     * @author Daniel Satriano
     */
    override fun onStart() {
        super.onStart()
        auth.currentUser?.reload()

        if(auth.currentUser != null){
            currentUser = auth.currentUser!!
            user.SetNewValue(Athlete(currentUser.uid, currentUser.displayName, currentUser.photoUrl.toString()))
            Athlete.setValues(currentUser.uid, currentUser.displayName, currentUser.photoUrl.toString())
            Log.d("USERUIDFROMFIREBASE", Athlete.UID)

            //IF THE USER COMES FROM THE REGISTRATION FORM THEN IT HAS TO SAVE THE DATA TO THE DB, OOTHERWISE IF FROM LOGIN FORM  THE INFOS WILL GET GRABBED FROM
            if(intent.extras!!.getBoolean("HASTOSAVE")){
                Log.d("MainWindow-Signout", "Entro")
                databaseHelper.setAthleteItem(user.UID,user)
            }

            databaseHelper = RealTimeDBHelper(dbReference.child(user.UID))      //Changing reference so that the db doesn't give me the whole node, but only the current logged user
            databaseHelper.readItems(getAthleteEventListener())                 //Applying listener for the "on update" call
            findViewById<TextView>(R.id.TV_Username).text = user.username       //Changing textview username text
        }

        //default fragment is the fitness cards
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.FragmentContainer, MyFitnessCards() )
        transaction.commit()
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
     *  Used to update username from the database.
     */
    private fun getAthleteEventListener(): ChildEventListener {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                //NOT NEEDED FOR THE SCOPE OF THIS LISTENER
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(String::class.java)
                user.changeUsername(item!!)
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