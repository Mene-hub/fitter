package com.fitterAPP.fitter

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.indexOf
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
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
    private val TAG : String = "MainWindow-"
    private lateinit var auth : FirebaseAuth
    private lateinit var currentUser : FirebaseUser

    //firebase database
    private lateinit var user : Athlete
    private val fitCardData : MutableList<FitnessCard> = ArrayList()

    //Bottom sheet dialog
    private lateinit var menuiv : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //FIREBASE ACCOUNT
        auth = Firebase.auth
        currentUser = auth.currentUser!!
        //User update
        user.username = currentUser.displayName
        user.profilePic = currentUser.photoUrl
        user.UID = currentUser.uid


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

    fun generate_random_cards(){

    }

    fun populate_atlhete(){

    }

    private fun getAthleteEventListener(): ChildEventListener {
        val childEventListener = object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(FitnessCard::class.java)
                //aggiungo nuova fitness card
                fitCardData.add((item!!))
                //adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(FitnessCard::class.java)
                //cerco fitness card modificata
                val index = fitCardData.indexOf(item)
                fitCardData[index].set(item)
                //adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val item = snapshot.getValue(FitnessCard::class.java)
                fitCardData.remove(item)
               //adapter.notifyDataSetChanged()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //viene triggerato quando la locazione del child cambia
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "postcomments:onCancelled", error.toException())
                Toast.makeText(this@MainActivity, "Failed to load comment.",Toast.LENGTH_SHORT).show()
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