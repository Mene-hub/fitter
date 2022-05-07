package com.fitterAPP.fitter

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class login : Fragment() {
    private val TAG_login : String = "LoginActivity-Login"
    private val TAG_register : String = "LoginActivity-Register"

    private lateinit var auth: FirebaseAuth

    private lateinit var loginButton : Button
    private lateinit var registerButton : Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_login, container, false)


        //istantiate auth variable
        auth = Firebase.auth

        //BUTTONS for login/register
        loginButton = v.findViewById(R.id.btn_login)
        loginButton.setOnClickListener(loginEmailPSW())

        /*
        registerButton = findViewById(R.id.btn_register)
        tmpButtonRegister.setOnClickListener(createUser())

        val tmpButtonSignOut : Button = findViewById(R.id.btn_signout)
        tmpButtonSignOut.setOnClickListener{
            auth.signOut()
            findViewById<TextView>(R.id.status).text =  "USER NOT LOGGED IN"
        }
        */

        // Inflate the layout for this fragment
        return v
    }

    override fun onStart() {
        super.onStart()
        //GRAB CURRENT USER IF ALREADY LOGGED-IN IN THE PAST
        val currentUser = auth.currentUser

        if(currentUser != null) {
            //updateUI(currentUser) UPDATE UI ACCORDINGLY
            //START MAIN ACTIVITY
            Log.d(TAG_login,"LOGGATO")
        }else{
            //USER NOT LOGGED IN - needs to login
            Log.d(TAG_login,"NON LOGGATO")
        }
    }

    //LOGIN VIA EMAIL AND PASSWORD
    fun loginEmailPSW(): View.OnClickListener {
        val listener = View.OnClickListener {
            val email : String = view?.findViewById<EditText>(R.id.et_loginEmail)?.text.toString()
            val password : String = view?.findViewById<EditText>(R.id.et_loginPassword)?.text.toString()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(requireActivity().mainExecutor){ task ->
                        if(task.isSuccessful){
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_login,  "Login success")

                            //val user = auth.currentUser
                            //updateUI(user) UPDATE UI ACCORDINGLY
                            Log.d(TAG_login,auth.currentUser?.displayName.toString())

                            val i : Intent = Intent(requireActivity(), MainActivity::class.java)
                            i.putExtra("USER", auth.currentUser)
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(i)

                        }else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_login, "Login failed", task.exception)
                            Toast.makeText(requireActivity().baseContext, "USERNAME OR PASSWORD ERROR", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
        return listener
    }


    /*
    //IF USER NOT FOUND IN "loginEmailPSW" THEN CREATES ONE
    private fun createUser() : View.OnClickListener {
        val listener = View.OnClickListener {
            val email : String = findViewById<EditText>(R.id.et_email).text.toString()
            val password : String = findViewById<EditText>(R.id.et_password).text.toString()

            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){ task ->
                    if(task.isSuccessful){
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG_register, "User creation: OK")

                        /*
                        //TESTING AGGIUNTA NOME UTENTE AL PROFILO
                        var updater = UserProfileChangeRequest.Builder().setDisplayName("MiraiMizu").build()
                        auth.currentUser?.updateProfile(updater)
                        Log.w(TAG_login,auth.currentUser?.displayName.toString())
                        */

                        findViewById<TextView>(R.id.status).text =  auth.currentUser?.displayName.toString()
                    }else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG_register, "User creation: FAILED", task.exception)
                        finish()
                    }
                }

        }
        return listener
    }
*/
}