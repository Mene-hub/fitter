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
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.databases.RealTimeDBHelper
import com.fitterAPP.fitter.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

/**
 * First activity that the user see when opening the app. This activity provides methods to login via google and facebook.
 * @see loginGoogle for more clarifications
 * @see loginFacebook for more clarifications
 * @author Daniel Satriano
 * @since 10/05/2022
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private val TAG_login : String = "LoginActivity-Login"
    private lateinit var auth: FirebaseAuth
    private var bgimage : ImageView ?=null

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
        Firebase.database.setPersistenceEnabled(true)

        //Set transparent status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        auth = Firebase.auth    //istantiate auth variable

        //region googleStuff
        binding.IVLoginGoogle.setOnClickListener(loginGoogle())
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.google_token)
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

    /**
     * Checks if the user is currently signed in or not. If it is then it launches [MainActivity] with bundle("HASTOSAVE", false)
     * @see MainActivity for more information about it
     * @author Daniel Satriano
     * @since 10/05/2022
     */
    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null) {
            //START MAIN ACTIVITY
            val i = Intent(this, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            i.putExtra("HASTOSAVE",false)
            startActivity(i)
        }
    }

    /**
     * Method that is called in [handleFacebookAccessToken] whenever the operation succeeded, this method simply queries the database and checks if there is already a user
     * with the same UID registered. If so it tells [MainActivity] to not overwrite the existing data in the RealTimeDatabase with this ones.
     * @see handleFacebookAccessToken for more information about it
     * @author Daniel Satriano
     * @since 30/05/2022
     */
    private fun startActivityByFacebook(uid : String, token : AccessToken, user : FirebaseUser){
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance(RealTimeDBHelper.getDbURL()).getReference("USERS")
        databaseReference.orderByKey().equalTo(uid).get().addOnSuccessListener{
                snapshot ->
            val item = snapshot.getValue(Athlete::class.java)
            if(item?.UID == auth.uid){
                //ESISTE
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.putExtra("HASTOSAVE",false)
                startActivity(intent)
            }else{
                val request = GraphRequest.newGraphPathRequest(token, "/${token.userId}/picture") { response ->
                    val uri = Uri.parse(response.connection?.url.toString())

                    val updater = UserProfileChangeRequest.Builder().setDisplayName(user.displayName.toString().replace("\\s".toRegex(),"")).setPhotoUri(uri).build()
                    auth.currentUser!!.updateProfile(updater).addOnCompleteListener{ task2 ->
                        if(task2.isSuccessful){
                            Log.d(TAG_login, "User profile updated")
                            auth.currentUser?.reload()
                            Log.w(TAG_login,user.displayName.toString())

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

            }
        }.addOnFailureListener {
            val request = GraphRequest.newGraphPathRequest(token, "/${token.userId}/picture") { response ->
                val uri = Uri.parse(response.connection?.url.toString())

                val updater = UserProfileChangeRequest.Builder().setDisplayName(user.displayName.toString().replace("\\s".toRegex(),"")).setPhotoUri(uri).build()
                auth.currentUser!!.updateProfile(updater).addOnCompleteListener{ task2 ->
                    if(task2.isSuccessful){
                        Log.d(TAG_login, "User profile updated")
                        auth.currentUser?.reload()
                        Log.w(TAG_login,user.displayName.toString())

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

        }
    }

    /**
     * Method that is called in [onActivityResult] whenever the operation succeeded, this method simply queries the database and checks if there is already a user
     * with the same UID registered. If so it tells [MainActivity] to not overwrite the existing data in the RealTimeDatabase with this ones.
     * @see onActivityResult for more information about it
     * @author Daniel Satriano
     * @since 30/05/2022
     */
    private fun startActivityByGoogle(uid : String){
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance(RealTimeDBHelper.getDbURL()).getReference("USERS")
        databaseReference.orderByKey().equalTo(uid).get().addOnSuccessListener{
                snapshot ->
            val item = snapshot.getValue(Athlete::class.java)
            if(item?.UID == auth.uid){
                //ESISTE
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.putExtra("HASTOSAVE",false)
                startActivity(intent)
            }else{
                //NON ESISTE
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
            }
        }.addOnFailureListener {
            //NON ESISTE
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
        }
    }

    /**
     * This method is the OnClickListener of the facebook button in the UI. Its job is to start the oneTapUI which will be then processed by [onActivityResult]
     * @see onActivityResult for more information about it
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
     * This method is the OnClickListener of the google button in the UI. Its job is to start the oneTapUI which will be then processed by [onActivityResult]
     * @see onActivityResult for more information about it
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
     * This method is used by [loginFacebook] and [loginGoogle] to handle the API callbacks
     * @author Daniel Satriano
     * @since 10/05/2022
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

                                        startActivityByGoogle(auth.currentUser?.uid!!)

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
     * * Method that handle the token given by facebook API and uses it to register onto Firebase.
     * * This method is paired with three more methods listed below
     * @see loginFacebook which is the onClick listener of the button that you can press on the UI
     * @see onActivityResult which is the method that retrieves calls the facebook API
     * @see startActivityByFacebook which is used to check if the user is already registered with that particular account
     * @author Daniel Satriano
     * @since 10/05/2022
     */
    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG_login, "signInWithCredential:success")
                    startActivityByFacebook(auth.currentUser?.uid!!, token, auth.currentUser!!)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG_login, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
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