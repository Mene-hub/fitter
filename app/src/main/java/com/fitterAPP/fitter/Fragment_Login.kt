package com.fitterAPP.fitter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.fitterAPP.fitter.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Fragment_Login : Fragment() {
    private val TAG_login : String = "LoginActivity-Login"
    private val TAG_register : String = "LoginActivity-Register"

    private lateinit var auth: FirebaseAuth

    private lateinit var loginButton : Button
    private lateinit var registerButton : Button

    private lateinit var intent : Intent
    private lateinit var psw_text_layout : TextInputLayout

    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        //istantiate auth variable
        auth = Firebase.auth

        //BUTTONS for login/register
        loginButton = binding.btnLogin
        loginButton.setOnClickListener(loginEmailPSW())

        //ERRORE PER PASSWORD SBAGLIATA / EMAIL SBAGLIATA
        psw_text_layout = binding.etLoginPasswordLayout
        val psw_editText = binding.etLoginPassword
        psw_editText.doOnTextChanged { text, start, before, count ->
            if(psw_text_layout.error != null) {
                psw_text_layout.error = null
            }
        }

        //EVENTO PER CONTROLLARE SE L'EMAIL INSERITA E' CORRETTA
        binding.etLoginEmail.setOnFocusChangeListener{
            _, focused ->
            if(!focused){
                binding.etLoginEmailLayout.helperText = validEmail()
            }
        }

        //INTENT PER APRIRE MAIN WINDOW
        intent = Intent(requireActivity(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun validEmail(): String? {
        val emailText = binding.etLoginEmail.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return getString(R.string.invalid_email)
        }
        return null
    }

    override fun onStart() {
        super.onStart()
        //GRAB CURRENT USER IF ALREADY LOGGED-IN IN THE PAST
        val currentUser = auth.currentUser

        if(currentUser != null) {
            //START MAIN ACTIVITY
            Log.d(TAG_login,"LOGGED")
            intent.putExtra("USER", auth.currentUser)
            startActivity(intent)
        }else{
            //USER NOT LOGGED IN - needs to login
            Log.d(TAG_login,"NOT LOGGED")
        }
    }

    //LOGIN VIA EMAIL AND PASSWORD
    fun loginEmailPSW(): View.OnClickListener {
        val listener = View.OnClickListener {
            val email : String = binding.etLoginEmail.text.toString()
            val password : String = binding.etLoginPassword.text.toString()

            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener((activity as LoginActivity)){ task ->
                    if(task.isSuccessful){
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG_login,  "Login success")

                        //val user = auth.currentUser
                        //updateUI(user) UPDATE UI ACCORDINGLY
                        intent.putExtra("USER", auth.currentUser)
                        startActivity(intent)

                    }else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG_login, "Login failed", task.exception)

                        psw_text_layout.error = getString(R.string.password_incorrect)
                        Toast.makeText(requireActivity().baseContext, getString(R.string.password_incorrect), Toast.LENGTH_LONG).show()
                    }
            }
        }
        return listener
    }

}