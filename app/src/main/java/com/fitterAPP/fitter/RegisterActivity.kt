package com.fitterAPP.fitter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.fitterAPP.fitter.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private val TAG_register : String = "LoginActivity-Register"
    private lateinit var auth: FirebaseAuth
    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        //firebase auth
        auth = Firebase.auth

        //SERVE PER TORNARE ALLA REGISTER FRAGMENT
        binding.openLogin.setOnClickListener {
            showLogin()
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

        setContentView(binding.root)
    }

    private fun showLogin() {
        val i : Intent = Intent(this, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
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
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_register, "User creation: OK")

                            var updater = UserProfileChangeRequest.Builder().setDisplayName(username).build()
                            auth.currentUser!!.updateProfile(updater).addOnCompleteListener{ task ->
                                if(task.isSuccessful){
                                    Log.d(TAG_register, "User profile updated")
                                    auth.currentUser?.reload()
                                    Log.w(TAG_register,auth.currentUser?.displayName.toString())
                                    val intent : Intent = Intent(this, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    intent.putExtra("HASTOSAVE",true)
                                    startActivity(intent)
                                }else{
                                    Toast.makeText(this,"There was a problem during the registration, try again later", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_register, "User creation: FAILED", task.exception)

                            binding.etSignupEmailLayout.helperText = getString(R.string.email_already_registered)
                            Toast.makeText(this, getString(R.string.email_already_registered), Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }
        return listener
    }

}