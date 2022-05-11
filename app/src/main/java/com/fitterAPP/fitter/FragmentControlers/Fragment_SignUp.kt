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

    /**
     * @author Daniel Satriano
     * onCreateView, once view is created it sets all the needed listeners for the UI
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        //firebase auth
        auth = Firebase.auth

        //SERVE PER TORNARE ALLA REGISTER FRAGMENT
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

        //CONTROLLA SE LA PASSWOD DI CONFERMA è UGUALE ALLA PASSWORD
        binding.etSignupConfirmPassword.onFocusChangeListener = signUpConfPasswordEventListener()
        binding.etSignupPassword.onFocusChangeListener = signUpPasswordEventListener()


        // Inflate the layout for this fragment
        return binding.root;
    }

    private fun signUpConfPasswordEventListener(): View.OnFocusChangeListener {
        val listener = View.OnFocusChangeListener{ _, focused ->
            if(!focused){
                if(!checkPasswordConfirmation(binding.etSignupPassword.text.toString(), binding.etSignupConfirmPassword.text.toString())){
                    binding.etSignupConfirmPasswordLayout.error = getString(R.string.pass_dont_match)
                }
            }else{
                binding.etSignupConfirmPasswordLayout.error = null
            }
        }
        return listener
    }

    private fun signUpPasswordEventListener(): View.OnFocusChangeListener {
        val listener = View.OnFocusChangeListener{ _, focused ->
            if(!focused){
                if(!binding.etSignupPassword.text.isNullOrBlank()) {
                    if (!checkPasswordLength(binding.etSignupPassword.text.toString())) {
                        binding.etSignupPasswordLayout.helperText =
                            "Password length must be at least 6 characters"
                    }
                }else{
                    binding.etSignupPasswordLayout.helperText = "Password cannot be empty or blank spaces"
                }
            }else{
                binding.etSignupPasswordLayout.helperText = null
            }
        }
        return listener
    }

    /**
     * @author Daniel Satriano
     * @since 21/05/2022
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
     * @author Daniel Satriano
     * @since 21/05/2022
     * @return true if confirm password and password are the same, false is the passwords don't match
     * this mathod its used in registerEmailPSW()
     * @see registerEmailPSW
     */
    private fun checkPasswordConfirmation(password : String, confPassword: String) : Boolean {
        if (password == confPassword) {
            return true
        }
        return false
    }

    /**
     * @author Daniel Satriano
     * @since 21/05/2022
     * @return true if password has 6 or more chars, false if it doesn't
     * this mathod its used in registerEmailPSW()
     * @see registerEmailPSW
     */
    private fun checkPasswordLength(password : String) : Boolean{
        if(password.length >= 6){
            return true
        }
        return false
    }

    /**
     * @author Daniel Satriano
     * @since 21/05/2022
     */
    private fun loginGoogle(): View.OnClickListener {
        val listener = View.OnClickListener {

        }
        return listener
    }
    /**
     * @author Daniel Satriano
     * @since 21/05/2022
     */
    private fun loginFacebook(): View.OnClickListener {
        val listener = View.OnClickListener {

        }
        return listener
    }

    /**
     * @author Daniel Satriano
     * @since 21/05/2022
     * IF USER NOT FOUND IN "loginEmailPSW" THEN CREATES ONE
     * It uses email, password, username
     */
    private fun registerEmailPSW() : View.OnClickListener {
        val listener = View.OnClickListener {
            binding.etSignupConfirmPassword.clearFocus()
            val email : String = binding.etSignupEmail.text.toString()
            val password : String = binding.etSignupPassword.text.toString()
            val username : String = binding.etSignupUsername.text.toString()

            if(!email.isNullOrBlank() && !password.isNullOrBlank()
                && checkPasswordConfirmation(password, binding.etSignupConfirmPassword.text.toString()) && checkPasswordLength(password)) {

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

                            binding.etSignupEmailLayout.helperText = getString(R.string.email_already_registered)
                            Toast.makeText(requireActivity().baseContext, getString(R.string.email_already_registered), Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }
        return listener
    }

}