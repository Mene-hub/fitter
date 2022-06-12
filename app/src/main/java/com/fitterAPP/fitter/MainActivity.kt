package com.fitterAPP.fitter

import com.fitterAPP.fitter.databases.StaticAthleteDatabase
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.facebook.login.LoginManager
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.fragmentControllers.*
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

    private val user = Athlete()
    private lateinit var auth : FirebaseAuth
    private lateinit var currentUser : FirebaseUser
    private lateinit var dbReference : DatabaseReference
    //navController
    private lateinit var navController: NavController
    //Bottom sheet dialog
    private lateinit var menuiv : CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbReference = StaticAthleteDatabase.database.getReference(getString(R.string.AthleteReference))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.FragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setupWithNavController(navController)

        //FIREBASE ACCOUNT
        auth = Firebase.auth

        if(auth.currentUser != null){
            currentUser = auth.currentUser!!
            user.SetNewValue(Athlete(currentUser.uid, currentUser.displayName, currentUser.photoUrl.toString(), "", ""))
            Athlete.setValues(user)
        }

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

        if(auth.currentUser != null){
            currentUser = auth.currentUser!!
            //INITIAL INIT, WILL BE CHANGED AS SOON AS THE VALUE LISTENER IS CALLED
            user.SetNewValue(Athlete(currentUser.uid, currentUser.displayName, currentUser.photoUrl.toString(), "", ""))
            //IF THE USER COMES FROM THE REGISTRATION FORM (and Google Login & Facebook Login) THEN IT HAS TO SAVE THE DATA TO THE DB,
            // OTHERWISE IF FROM LOGIN FORM  THE INFO'S WILL GET GRABBED FROM
            if(intent.extras!!.getBoolean("HASTOSAVE")){
                Log.d("MainWindow-Signout", "Entro")
                StaticAthleteDatabase.setAthleteItem(dbReference, user.UID, user)
            }
            //Applying listener for the "on update" call
            StaticAthleteDatabase.setAthleteValueListener(dbReference, user.UID, getAthleteEventListener())

        }
        findViewById<TextView>(R.id.TV_Username).text = user.username       //SET USERNAME IN TEXTVIEW
        Athlete.setValues(user)         //SET NEW VALUES FOR THE STATIC USER PART, USED IN Fragment_MyFitnessCards.kt
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
            LoginManager.getInstance().logOut()
        }

        val i =  Intent(this, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }

    /**
     *  Used to update username from the database.
     *  @author Daniel Satriano
     */
    private fun getAthleteEventListener(): ValueEventListener {
        val childEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(Athlete::class.java) //GRAB USER ITEM
                user.SetNewValue(item!!)

                if(user.profilePic != "") {
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