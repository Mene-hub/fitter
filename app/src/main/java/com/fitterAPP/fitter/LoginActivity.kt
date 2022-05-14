package com.fitterAPP.fitter

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.fitterAPP.fitter.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private val TAG_login : String = "LoginActivity-Login"
    private lateinit var auth: FirebaseAuth

    //region googleStuff
        private lateinit var oneTapClient: SignInClient
        private lateinit var signInRequest: BeginSignInRequest
        private val REQ_ONE_TAP = 2
    //endregion

    //region facebookStuff
        private lateinit var callbackManager : CallbackManager
        private var FB_ONE_TAP : Int = 64206
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //INTENT PER APRIRE MAIN WINDOW
        intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("HASTOSAVE",false)

        auth = Firebase.auth    //istantiate auth variable

        //Event for switching to signUp activity
        binding.btnSignup.setOnClickListener {
            showRegister()
        }

        //region Login Email
        binding.btnLogin.setOnClickListener(loginEmailPSW())  //BUTTONS for login/register

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
        //endregion

        //region googleStuff
        binding.IVLoginGoogle.setOnClickListener(loginGoogle())
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()
        //endregion

        //region facebookStuff
        binding.IVLoginFacebook.setOnClickListener(loginFacebook())
        callbackManager = CallbackManager.Factory.create();
        //endregion

    }

    /**
     * @author Daniel Satriano
     */
    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG_login, "signInWithCredential:success")
                    val user = auth.currentUser

                    var updater = UserProfileChangeRequest.Builder().setDisplayName(user?.displayName.toString().replace("\\s".toRegex(),"")).build()
                    auth.currentUser!!.updateProfile(updater).addOnCompleteListener{ task ->
                        if(task.isSuccessful){
                            Log.d(TAG_login, "User profile updated")
                            auth.currentUser?.reload()
                            Log.w(TAG_login,auth.currentUser?.displayName.toString())

                            val intent = Intent(this, MainActivity::class.java)
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
                    Log.w(TAG_login, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * @author Daniel Satriano
     * @since 10/05/2022
     */
    private fun loginGoogle(): View.OnClickListener {
        val listener = View.OnClickListener {
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        startIntentSenderForResult(result.pendingIntent.intentSender, REQ_ONE_TAP, null, 0, 0, 0, null)
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e(TAG_login, "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(this) { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    Log.d(TAG_login, e.localizedMessage)
                }
        }
        return listener
    }

    /**
     * @author Daniel Satriano
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG_login,requestCode.toString())

        //Facebook SignIn
        when(requestCode){
            FB_ONE_TAP -> {
                // Pass the activity result back to the Facebook SDK
                callbackManager.onActivityResult(requestCode, resultCode, data)
            }
        }

        //Google SignIn
        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with Firebase.
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG_login, "signInWithCredential:success")
                                        val user = auth.currentUser

                                        var updater = UserProfileChangeRequest.Builder().setDisplayName(user?.displayName.toString().replace("\\s".toRegex(),"")).build()
                                        auth.currentUser!!.updateProfile(updater).addOnCompleteListener{ task ->
                                            if(task.isSuccessful){
                                                Log.d(TAG_login, "User profile updated")
                                                auth.currentUser?.reload()
                                                Log.w(TAG_login,auth.currentUser?.displayName.toString())

                                                val intent = Intent(this, MainActivity::class.java)
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                intent.putExtra("HASTOSAVE",true)
                                                startActivity(intent)

                                            }else{
                                                Toast.makeText(this,"There was a problem during the registration, try again later", Toast.LENGTH_LONG).show()
                                            }
                                        }

                                        Log.d(TAG_login, auth.currentUser?.displayName.toString())
                                        //updateUI(user)
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG_login, "signInWithCredential:failure", task.exception)
                                        //updateUI(null)
                                    }
                                }
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG_login, "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            Log.d(TAG_login, "One-tap dialog was closed.")
                            // Don't re-prompt the user.
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            Log.d(TAG_login, "One-tap encountered a network error.")
                            // Try again or just ignore.
                        }
                        else -> {
                            Log.d(TAG_login, "Couldn't get credential from result." +
                                    " (${e.localizedMessage})")
                        }
                    }
                }
            }
        }
    }

    /**
     * @author Daniel Satriano
     * @since 10/05/2022
     */
    private fun loginFacebook(): View.OnClickListener {
        val listener = View.OnClickListener {

            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile","email"))
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d(TAG_login, "facebook:onSuccess:$loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d(TAG_login, "facebook:onCancel")
                }

                override fun onError(exception: FacebookException) {
                    Log.d(TAG_login, "facebook:onError", exception)
                }

            })
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
                    .addOnCompleteListener(this) { task ->
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
                            Toast.makeText(this, getString(R.string.password_incorrect), Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
        return listener
    }

    /**
     * @author Claudio Menegotto
     * EVENTO PER CAMBIARE IL FRAGMENT DI LOGIN NEL FRAGMENT DI SIGN UP
     */
    fun showRegister(){
        val i : Intent = Intent(this, RegisterActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }

}