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
import com.facebook.login.LoginManager
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.BookmarkCard
import com.fitterAPP.fitter.databases.StaticBookmarkDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
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
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener(bottomNavSelectedItem())

        //FIREBASE ACCOUNT
        auth = Firebase.auth

        //Adds a value listener to the database
        val bookmarkReference = StaticBookmarkDatabase.database.getReference(getString(R.string.BookmarkReference))

        BookmarkCard.bookmarkList.clear()
        StaticBookmarkDatabase.setBookmarkListener(bookmarkReference, auth.uid!!, bookMarkValueListener())

        if(auth.currentUser != null){
            currentUser = auth.currentUser!!
            user.SetNewValue(Athlete(currentUser.uid, currentUser.displayName, currentUser.photoUrl.toString(), "", ""))
            Athlete.setValues(user)
        }

        //Bottom sheet dialog
        menuiv = findViewById(R.id.MenuBt)
        menuiv.setOnClickListener {
            navController.navigate(R.id.bottomSheetDialog)
        }
    }

    private fun bottomNavSelectedItem(): NavigationBarView.OnItemSelectedListener {
        return NavigationBarView.OnItemSelectedListener {
            item ->
            when(item.itemId){
                R.id.myFitnessCards ->{
                    navController.navigate(R.id.myFitnessCards)
                    true
                }
                R.id.findprofile ->{
                    navController.navigate(R.id.findprofile)
                    true
                }
                else ->{
                    false
                }
            }
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
     * Method used to log out users and send them back to the LoginActivity
     * @author Claudio Menegotto
     * @author Daniel Satriano
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
                if(item!=null) {
                    user.SetNewValue(item)
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

                if(item == null){
                    logout()
                }

            }
            override fun onCancelled(error: DatabaseError) {                        //(Event called when a problem occurred with the communication to the database, either because of server problems or listener errors)
                Log.w("MainActivity-Database-User", "postcomments:onCancelled", error.toException())
                logout()                                                            //LOGOUT
            }
        }
        return childEventListener
    }


    /**
     * Value listener for the bookmarks
     * @author Daniel Satriano
     * @since 3/08/2022
     */
    private fun bookMarkValueListener(): ValueEventListener {
        return object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(item in snapshot.children){
                    if(item != null){
                        BookmarkCard.bookmarkList.add(item.getValue(BookmarkCard::class.java)!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,error.message, Toast.LENGTH_LONG).show()
            }

        }
    }
}