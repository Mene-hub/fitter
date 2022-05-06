package com.fitterAPP.fitter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var listView : RecyclerView
    private lateinit var user : Athlete

    //login variable init
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //grab event from companion class RealTimeDBHelper
        RealTimeDBHelper.readToDoItems(getAthleteEventListener())

        //istantiate auth variable
        auth = Firebase.auth

        val tmpButtonLogin : Button? = null
        tmpButtonLogin?.setOnClickListener(loginEmailPSW("satrianodaniel@gmail.com","123"))

        val tmpButtonRegister : Button? = null
        tmpButtonRegister?.setOnClickListener(createUser("satrianodaniel@gmail.com","123"))
    }

    override fun onStart() {
        super.onStart()
        //GRAB CURRENT USER IF ALREADY LOGGED-IN IN THE PAST
        val currentUser = auth.currentUser
        if(currentUser != null) {
            //updateUI(currentUser) UPDATE UI ACCORDINGLY
        }else{
            //USER NOT LOGGED IN - needs to login
        }
    }

    //LOGIN VIA EMAIL AND PASSWORD
    private fun loginEmailPSW(email: String, password: String): View.OnClickListener? {
        val listener = View.OnClickListener {
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){ task ->
                    if(task.isSuccessful){
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("MainActivity",  "Login success")
                        val user = auth.currentUser
                        //updateUI(user) UPDATE UI ACCORDINGLY
                    }else {
                        // If sign in fails, display a message to the user.
                        Log.w("MainActivity", "Login failed", task.exception)
                        //USER DOESN'T LOGIN FAIL, CREATE ACCOUNT
                    }
                }
        }
        return listener
    }

    //IF USER NOT FOUND IN "loginEmailPSW" THEN CREATES ONE
    private fun createUser(email: String, password: String) : View.OnClickListener {
        val listener = View.OnClickListener {
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){ task ->
                    if(task.isSuccessful){
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("MainActivity", "User creation: OK")
                    }else {
                        // If sign in fails, display a message to the user.
                        Log.w("MainActivity", "User creation: FAILED", task.exception)
                        finish()
                    }
                }
        }
        return listener
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

}