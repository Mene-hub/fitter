package com.fitterAPP.fitter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class Fragment_SignUp : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v : View = inflater.inflate(R.layout.fragment_signup, container, false)

        val loginopener_TV : TextView = v.findViewById(R.id.open_login)
        loginopener_TV.setOnClickListener {
            (activity as LoginActivity).showLogin()
        }

        return v;
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