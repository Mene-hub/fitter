package com.fitterAPP.fitter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.fitterAPP.fitter.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private val TAGForgotPassword : String = "ForgotPasswordActivity: "
    private lateinit var binding : ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set transparent status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.btnLogin.setOnClickListener(resetPswListener())

    }

    /**
     * Method to reset password, it checks if the email is correct, if it is, it then proceeds to send a reset email to the given address
     * @author Daniel Satriano
     */
    private fun resetPswListener(): View.OnClickListener {
        val listener = View.OnClickListener {
            val email : String = binding.etPswReset.text.toString().trim{it <= ' '}
            Log.d(TAGForgotPassword,email)
            if(email.isNotBlank() && email.isNotEmpty()){
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, getString(R.string.email_sent_successfully), Toast.LENGTH_LONG).show()
                        finish()
                    }else{
                        Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }else{
                Toast.makeText(this, getString(R.string.please_enter_valid_email), Toast.LENGTH_LONG).show()
            }
        }
        return listener
    }

}