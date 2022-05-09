package com.fitterAPP.fitter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.fitterAPP.fitter.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//FARE CONTROLLO PASSWORD ALMENO 6 CARATTERI

class Fragment_SignUp : Fragment() {
    private val TAG_register : String = "LoginActivity-Register"

    private lateinit var binding : FragmentSignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        //firebase auth
        auth = Firebase.auth

        binding.openLogin.setOnClickListener {
            (activity as LoginActivity).showLogin()
        }

        //BUTTONS for login/register
        binding.btnSignup.setOnClickListener(registerEmailPSW())
        binding.IVLoginGoogle.setOnClickListener(loginGoogle())
        binding.IVLoginFacebook.setOnClickListener(loginFacebook())

        //EVENTO PER CONTROLLARE SE L'EMAIL INSERITA E' CORRETTA
        binding.etSignupEmail.setOnFocusChangeListener{ _, focused ->
            if(!focused){
                Log.d(TAG_register,validEmail().toString())
                binding.etSignupEmailLayout.helperText = validEmail()
            }
        }


        binding.etSignupConfirmPassword.setOnFocusChangeListener{ _, focused ->
            if(!focused){
                if(!checkPassword()){
                    binding.etSignupConfirmPasswordLayout.error = "PASSWORDS DON'T MATCH"
                }
            }else{
                binding.etSignupConfirmPasswordLayout.error = null
            }
        }

        // Inflate the layout for this fragment
        return binding.root;
    }

    /**
     * Checks if the inserted email is valid or not
     */
    private fun validEmail(): String? {
        val emailText = binding.etSignupEmail.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return getString(R.string.invalid_email)
        }
        return null
    }
    /**
     * returns true if confirm password and password are the same,
     * returns false is the passwords don't match
     */
    private fun checkPassword() : Boolean {
        if(binding.etSignupPassword.text.toString() == binding.etSignupConfirmPassword.text.toString()){
            return true
        }
        return false
    }

    private fun loginGoogle(): View.OnClickListener {
        val listener = View.OnClickListener {

        }
        return listener
    }
    private fun loginFacebook(): View.OnClickListener {
        val listener = View.OnClickListener {

        }
        return listener
    }

    //IF USER NOT FOUND IN "loginEmailPSW" THEN CREATES ONE
    private fun registerEmailPSW() : View.OnClickListener {
        val listener = View.OnClickListener {
            binding.etSignupConfirmPassword.clearFocus()
            val email : String = binding.etSignupEmail.text.toString()
            val password : String = binding.etSignupPassword.text.toString()
            val username : String = binding.etSignupUsername.text.toString()

            if(!email.isNullOrBlank() && !password.isNullOrBlank() && checkPassword()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((activity as LoginActivity)) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_register, "User creation: OK")

                            //TESTING AGGIUNTA NOME UTENTE AL PROFILO
                            var updater = UserProfileChangeRequest.Builder().setDisplayName(username).build()
                            auth.currentUser?.updateProfile(updater)

                            Log.w(TAG_register,auth.currentUser?.displayName.toString())

                            //Login into MainActivity
                            //INTENT PER APRIRE MAIN WINDOW
                            val intent : Intent = Intent(requireActivity(), MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_register, "User creation: FAILED", task.exception)

                            binding.etSignupEmailLayout.helperText = "EMAIL ALREADY REGISTERED"
                            Toast.makeText(requireActivity().baseContext, "EMAIL ALREADY REGISTERED", Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }
        return listener
    }

}