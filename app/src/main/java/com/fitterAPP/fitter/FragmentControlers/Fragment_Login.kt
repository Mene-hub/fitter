package com.fitterAPP.fitter.FragmentControlers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.fitterAPP.fitter.LoginActivity
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

public class Fragment_Login : Fragment() {
    private val TAG_login : String = "LoginActivity-Login"

    private lateinit var auth: FirebaseAuth
    private lateinit var intent : Intent    //MainWindow caller
    private lateinit var binding : FragmentLoginBinding //Binding

    /**
     * @author Daniel Satriano
     * @since 10/05/2022
     * onCreateView, once view is created it sets all the needed listeners for the UI
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        //istantiate auth variable
        auth = Firebase.auth

        //BUTTONS for login/register
        binding.btnLogin.setOnClickListener(loginEmailPSW())
        binding.IVLoginGoogle.setOnClickListener(loginGoogle())
        binding.IVLoginFacebook.setOnClickListener(loginFacebook())

        //ERRORE PER PASSWORD SBAGLIATA / EMAIL SBAGLIATA
        val psw_text_layout : TextInputLayout = binding.etLoginPasswordLayout
        binding.etLoginPassword.doOnTextChanged { text, start, before, count ->
            if(psw_text_layout.error != null) {
                psw_text_layout.error = null
            }
        }
        //EVENTO PER CONTROLLARE SE L'EMAIL INSERITA E' CORRETTA
        binding.etLoginEmail.setOnFocusChangeListener{ _, focused ->
            if(!focused){
                binding.etLoginEmailLayout.helperText = validEmail()
            }
        }

        binding.etLoginPassword.setOnFocusChangeListener{_, focused ->
            if(focused){
                binding.etLoginPasswordLayout.error = null
            }
        }

        //EVENTO PER INSERIRE IL FRAGMENT DI SIGNUP
        binding.btnSignup.setOnClickListener {
            (activity as LoginActivity).showRegister()
        }


        //INTENT PER APRIRE MAIN WINDOW
        intent = Intent(requireActivity(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("HASTOSAVE",false)

        // Inflate the layout for this fragment
        return binding.root
    }

    /**
     * @author Daniel Satriano
     * @since 10/05/2022
     */
    private fun loginGoogle(): View.OnClickListener {
        val listener = View.OnClickListener {

        }
        return listener
    }

    /**
     * @author Daniel Satriano
     * @since 10/05/2022
     */
    private fun loginFacebook(): View.OnClickListener {
        val listener = View.OnClickListener {

        }
        return listener
    }

    /**
     * @author Daniel Satriano
     * @since 10/05/2022
     * Checks if the inserted email is valid or not
     */
    private fun validEmail(): String? {
        val emailText = binding.etLoginEmail.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return getString(R.string.invalid_email)
        }
        return null
    }

    /**
     * @author Daniel Satriano
     * @since 10/05/2022
     * Checks if the user is currently signed in or not. If it is then it launches MainActivity
     * @see MainActivity for more information about it
     */
    override fun onStart() {
        super.onStart()
        //GRAB CURRENT USER IF ALREADY LOGGED-IN IN THE PAST
        val currentUser = auth.currentUser

        Log.d(TAG_login, currentUser.toString())

        if(currentUser != null) {
            //START MAIN ACTIVITY
            Log.d(TAG_login,"LOGGED")
            startActivity(intent)
        }else{
            //USER NOT LOGGED IN - needs to login
            Log.d(TAG_login,"NOT LOGGED")
        }
    }

    /**
     * @author Daniel Satriano
     * @since 10/05/2022
     * Login via password and email, if it finds an account it Log In and start MainActivity, if it doesn't it'll throw an error at the UI for the user
     * @see MainActivity for more information about it
     */
    fun loginEmailPSW(): View.OnClickListener {
        val listener = View.OnClickListener {
            val email : String = binding.etLoginEmail.text.toString()
            val password : String = binding.etLoginPassword.text.toString()

            if(!email.isNullOrBlank() && !password.isNullOrBlank()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((activity as LoginActivity)) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_login, "Login success")

                            //val user = auth.currentUser
                            //updateUI(user) UPDATE UI ACCORDINGLY
                            startActivity(intent)

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_login, "Login failed", task.exception)

                            binding.etLoginPasswordLayout.error = getString(R.string.password_incorrect)
                            Toast.makeText(requireActivity().baseContext, getString(R.string.password_incorrect), Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
        return listener
    }

}