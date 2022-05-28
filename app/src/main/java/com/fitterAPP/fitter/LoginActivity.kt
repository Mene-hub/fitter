package com.fitterAPP.fitter

import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.fitterAPP.fitter.classes.LoadingDialog
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
import kotlin.random.Random

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private val TAG_login : String = "LoginActivity-Login"
    private lateinit var auth: FirebaseAuth
    private var bgimage : ImageView ?=null
    private lateinit var loadingDialog : LoadingDialog

    //region googleStuff
        private lateinit var oneTapClient: SignInClient
        private lateinit var signInRequest: BeginSignInRequest
        private val REQ_ONE_TAP : Int = 2
    //endregion

    //region facebookStuff
        private lateinit var callbackManager : CallbackManager
        private var FB_ONE_TAP : Int = 64206
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set transparent status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        loadingDialog = LoadingDialog(this@LoginActivity)


        auth = Firebase.auth    //istantiate auth variable

        //Event for switching to signUp activity
        binding.btnSignup.setOnClickListener {
            showRegister()
        }

        binding.tvForgotPSW.setOnClickListener(forgotPasswordListener())

        //region Login Email
        binding.btnLogin.setOnClickListener(loginEmailPSW())  //BUTTONS for login/register

        //ERROR FOR WRONG PASSWORD / WRONG EMAIL
        val psw_text_layout : TextInputLayout = binding.etLoginPasswordLayout
        binding.etLoginPassword.doOnTextChanged { text, start, before, count ->
            if(psw_text_layout.error != null) {
                psw_text_layout.error = null
            }
        }
        //EVENT TO CHECK IF THE EMAIL ENTERED IS CORRECT
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
        callbackManager = CallbackManager.Factory.create()
        //endregion

        randomBgImages()
    }

    private fun forgotPasswordListener(): View.OnClickListener {
        val listener = View.OnClickListener {
            //
            val i = Intent(this, ForgotPasswordActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(i)
        }
        return listener
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

                    val request = GraphRequest.newGraphPathRequest(token, "/${token.userId}/picture") { response ->
                        val uri = Uri.parse(response.connection?.url.toString())

                        val updater = UserProfileChangeRequest.Builder().setDisplayName(user?.displayName.toString().replace("\\s".toRegex(),"")).setPhotoUri(uri).build()
                        auth.currentUser!!.updateProfile(updater).addOnCompleteListener{ task2 ->
                            if(task2.isSuccessful){
                                Log.d(TAG_login, "User profile updated")
                                auth.currentUser?.reload()
                                Log.w(TAG_login,auth.currentUser?.displayName.toString())

                                val intent = Intent(this, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent.putExtra("HASTOSAVE",true)
                                startActivity(intent)

                            }else{
                                Toast.makeText(this,task2.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    request.executeAsync()



                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG_login, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
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
                    e.localizedMessage?.let { it1 -> Log.d(TAG_login, it1) }
                }
        }
        return listener
    }

    /**
     * @author Daniel Satriano
     */
    @Deprecated("Ignore this deprecation")
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

                                        val updater = UserProfileChangeRequest.Builder().setDisplayName(user?.displayName.toString().replace("\\s".toRegex(),"")).build()
                                        auth.currentUser!!.updateProfile(updater).addOnCompleteListener{ task2 ->
                                            if(task2.isSuccessful){
                                                Log.d(TAG_login, "User profile updated")
                                                auth.currentUser?.reload()
                                                Log.w(TAG_login,auth.currentUser?.displayName.toString())

                                                val intent = Intent(this, MainActivity::class.java)
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                intent.putExtra("HASTOSAVE",true)
                                                startActivity(intent)

                                            }else{
                                                Toast.makeText(this,task2.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                                            }
                                        }
                                        Log.d(TAG_login, auth.currentUser?.displayName.toString())
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(this,task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
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
                            Toast.makeText(this,getString(R.string.network_error), Toast.LENGTH_LONG).show()
                            // Try again or just ignore.
                        }
                        else -> {
                            Log.d(TAG_login, "Couldn't get credential from result." + " (${e.localizedMessage})")
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
                override fun onSuccess(result: LoginResult) {
                    Log.d(TAG_login, "facebook:onSuccess:$result")
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                    Log.d(TAG_login, "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d(TAG_login, "facebook:onError", error)
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
            val i = Intent(this, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            i.putExtra("HASTOSAVE",false)
            startActivity(i)
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

            if(email.isNotBlank() && password.isNotBlank()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_login, "Login success")

                            //val user = auth.currentUser
                            //updateUI(user) UPDATE UI ACCORDINGLY
                            val i = Intent(this, MainActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            i.putExtra("HASTOSAVE",false)
                            startActivity(i)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_login, "Login failed", task.exception)

                            binding.etLoginPasswordLayout.error = getString(R.string.password_incorrect)
                            Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
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
        val i = Intent(this, RegisterActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(i)
    }

    /**
     * @author Claudio Menegotto
     * EVENTO PER CAMBIARE L'IMMAGINE DI BACKGROUND
     */
    fun randomBgImages(){
        bgimage = findViewById(R.id.LoginGB_IV)
        val mybgs : MutableList<Int> = ArrayList()
        mybgs.add(R.drawable.gigachad)
        mybgs.add(R.drawable.gigachad2)
        mybgs.add(R.drawable.man_bodybuilder)
        mybgs.add(R.drawable.man_bodybuilder)
        mybgs.add(R.drawable.man_bodybuilder2)
        mybgs.add(R.drawable.man_bodybuilder2)
        mybgs.add(R.drawable.woman_bodybuilder)
        mybgs.add(R.drawable.woman_bodybuilder)
        mybgs.add(R.drawable.woman_bodybuilder2)
        mybgs.add(R.drawable.woman_bodybuilder2)

        bgimage?.setImageResource(mybgs[Random.nextInt(0,mybgs.size)])
    }

}