package com.fitterAPP.fitter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val TAG_login : String = "MainActivity-Login"

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

        //TMP BUTTONS
        val tmpButtonLogin : Button = findViewById(R.id.btn_login)
        tmpButtonLogin.setOnClickListener(loginEmailPSW())

        val tmpButtonRegister : Button = findViewById(R.id.btn_register)
        tmpButtonRegister.setOnClickListener(createUser())

        val tmpButtonSignOut : Button = findViewById(R.id.btn_signout)
        tmpButtonSignOut.setOnClickListener{
            auth.signOut()
            findViewById<TextView>(R.id.status).text =  "USER NOT LOGGED IN"
        }
    }

    override fun onStart() {
        super.onStart()
        //GRAB CURRENT USER IF ALREADY LOGGED-IN IN THE PAST
        val currentUser = auth.currentUser
        if(currentUser != null) {
            //updateUI(currentUser) UPDATE UI ACCORDINGLY
            findViewById<TextView>(R.id.status).text =  auth.currentUser?.displayName.toString()
            Log.w(TAG_login, auth.currentUser?.email.toString())
            Log.w(TAG_login, auth.currentUser?.uid.toString())

        }else{
            //USER NOT LOGGED IN - needs to login
        }
    }

    //LOGIN VIA EMAIL AND PASSWORD
    private fun loginEmailPSW(): View.OnClickListener {
        val listener = View.OnClickListener {
            val email : String = findViewById<EditText>(R.id.et_email).text.toString()
            val password : String = findViewById<EditText>(R.id.et_password).text.toString()

            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){ task ->
                    if(task.isSuccessful){
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG_login,  "Login success")

                        //val user = auth.currentUser
                        //updateUI(user) UPDATE UI ACCORDINGLY
                        findViewById<TextView>(R.id.status).text = auth.currentUser?.displayName.toString()
                    }else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG_login, "Login failed", task.exception)
                        //USER DOESN'T LOGIN FAIL, CREATE ACCOUNT
                        findViewById<TextView>(R.id.status).text = "ACCOUNT NOT FOUND, CREATE NEW ONE"
                    }
                }
        }
        return listener
    }

    //IF USER NOT FOUND IN "loginEmailPSW" THEN CREATES ONE
    private fun createUser() : View.OnClickListener {
        val listener = View.OnClickListener {
            val email : String = findViewById<EditText>(R.id.et_email).text.toString()
            val password : String = findViewById<EditText>(R.id.et_password).text.toString()

            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){ task ->
                    if(task.isSuccessful){
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG_login, "User creation: OK")

                        /*
                        //TESTING AGGIUNTA NOME UTENTE AL PROFILO
                        var updater = UserProfileChangeRequest.Builder().setDisplayName("MiraiMizu").build()
                        auth.currentUser?.updateProfile(updater)
                        Log.w(TAG_login,auth.currentUser?.displayName.toString())
                        */

                        findViewById<TextView>(R.id.status).text =  auth.currentUser?.displayName.toString()
                    }else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG_login, "User creation: FAILED", task.exception)
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