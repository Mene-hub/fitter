package com.fitterAPP.fitter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentContainer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun showRegister(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack("registerFragment")
        transaction.replace(R.id.loginContainer, Fragment_SignUp() )
        transaction.commit()
    }
}