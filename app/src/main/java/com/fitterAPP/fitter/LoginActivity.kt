package com.fitterAPP.fitter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fitterAPP.fitter.FragmentControlers.Fragment_Login
import com.fitterAPP.fitter.FragmentControlers.Fragment_SignUp

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    //EVENTO PER CAMBIARE IL FRAGMENT DI LOGIN NEL FRAGMENT DI SIGN UP
    fun showRegister(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.loginContainer, Fragment_SignUp() )
        transaction.commit()
    }

    fun showLogin(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.loginContainer, Fragment_Login() )
        transaction.commit()
    }
}