package com.fitterAPP.fitter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.fitterAPP.fitter.Classes.Athlete
import com.fitterAPP.fitter.FragmentControlers.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

/**
 * @author Daniel Satriano
 * @author Claudio Menegotto
 * Main activity for the android app, in this activity you'll be able to access all your data and your fitness cards.
 */
class MainActivity : AppCompatActivity() {

    private val _reference = "USERS"
    private val user = Athlete()
    private lateinit var auth : FirebaseAuth
    private lateinit var currentUser : FirebaseUser
    private lateinit var databaseHelper : RealTimeDBHelper
    private var dbReference : DatabaseReference = FirebaseDatabase.getInstance(RealTimeDBHelper.getDbURL()).getReference(_reference)
    //Bottom sheet dialog
    private lateinit var menuiv : CardView

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

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnClickListener(bottomNavigationListener())
    }

    /**
     * @author Daniel Satriano
     */
    override fun onStart() {
        super.onStart()

        if(auth.currentUser != null){
            currentUser = auth.currentUser!!

            //INITIAL INIT, WILL BE CHANGED AS SOON AS THE VALUE LISTENER IS CALLED
            user.SetNewValue(Athlete(currentUser.uid, currentUser.displayName, currentUser.photoUrl.toString(), "", ""))

            //IF THE USER COMES FROM THE REGISTRATION FORM (and Google Login & Facebook Login) THEN IT HAS TO SAVE THE DATA TO THE DB,
            // OTHERWISE IF FROM LOGIN FORM  THE INFO'S WILL GET GRABBED FROM
            if(intent.extras!!.getBoolean("HASTOSAVE")){
                Log.d("MainWindow-Signout", "Entro")
                databaseHelper.setAthleteItem(user.UID,user)
                //downloadImagesFromURL(user.profilePic)
            }

            databaseHelper = RealTimeDBHelper(dbReference.child(user.UID))    //Changing reference so that the db doesn't give me the whole node, but only the current logged user
            databaseHelper.readItem(getAthleteEventListener())                //Applying listener for the "on update" call

        }
        findViewById<TextView>(R.id.TV_Username).text = user.username       //SET USERNAME IN TEXTVIEW
        Athlete.setValues(user)         //SET NEW VALUES FOR THE STATIC USER PART, USED IN Fragment_MyFitnessCards.kt

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.FragmentContainer, MyFitnessCards() )
        transaction.commit()




    }

    private fun bottomNavigationListener(): View.OnClickListener {
        val listener = View.OnClickListener { item ->
            when(item.id) {
                R.id.page_1 -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.page_2 -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.page_3 -> {
                    // Respond to navigation item 3 click
                    true
                }
                else -> false
            }
        }
        return listener
    }

    //region roba claudio
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
    //endregion

    fun logout(){
        if(auth.currentUser != null){
            Log.d("MainWindow-Signout", "Sloggato")
            auth.signOut()
        }

        val i =  Intent(this, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }

    /**
     *  @author Daniel Satriano
     *  Used to update username from the database.
     */
    private fun getAthleteEventListener(): ValueEventListener {
        val childEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(Athlete::class.java)                   //GRAB USER ITEM
                user.SetNewValue(item!!)

                if(user.profilePic != "null") {
                    val imageProfile: ImageView = findViewById(R.id.profilepic_IV)
                    val imageURI: String = user.profilePic!!
                    Picasso.get()
                        .load(imageURI)
                        .resize(100, 100)
                        .centerCrop()
                        .into(imageProfile)
                }

                //SET NEW USER ITEM
                findViewById<TextView>(R.id.TV_Username).text = user.username       //SET USERNAME IN TEXTVIEW
                Athlete.setValues(user)         //SET NEW VALUES FOR THE STATIC USER PART, USED IN Fragment_MyFitnessCards.kt
            }

            override fun onCancelled(error: DatabaseError) {                        //(THIS EVENT IS CALLED ONLY WHEN THE USER IS ONLINE AND ITS ACCOUNT GETS DELETED)
                Log.w("MainActivity-Database-User",
                    "postcomments:onCancelled", error.toException())

                logout()                                                            //LOGOUT
            }
        }
        return childEventListener
    }
}