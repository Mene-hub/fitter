package com.fitterAPP.fitter.fragmentControllers

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.renderscript.Sampler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.databases.RealTimeDBHelper
import com.fitterAPP.fitter.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class Profile : Fragment() {

    private val REQUEST_CODE = 121
    private lateinit var binding : FragmentProfileBinding

    private lateinit var auth : FirebaseAuth
    private val dbReference : DatabaseReference = FirebaseDatabase.getInstance(RealTimeDBHelper.getDbURL()).getReference("USERS").child(Athlete.UID)
    private lateinit var user : Athlete

    private lateinit var etUsername : EditText
    private lateinit var etBio : EditText
    private lateinit var etEmail : EditText

    //SPOSTARE VERIFICA EMAIL NELLA REGISTRAZIONE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        val image = binding.imgProfile
        val username = binding.tvUsername
        val bio = binding.tvBio

        etUsername = binding.etUsername
        etBio = binding.etBio
        etEmail = binding.etEmail

        val btnUpdate = binding.btnUpdate


        if(checkIfShouldDisplay() != true){
            etEmail.error = "Email verification needed"
            etEmail.setOnClickListener(verificationProcess())
        }

        updateStaticInfo(image,username,bio)
        updateVariableInfo(etUsername,etBio,etEmail)

        btnUpdate.setOnClickListener(updateProfileListener())

        return binding.root
    }

    private fun verificationProcess(): View.OnClickListener {
        val listener = View.OnClickListener {
            Firebase.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Toast.makeText(requireContext(),"Email sent successfully", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(requireContext(),task.exception?.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
        return listener
    }
    private fun checkIfShouldDisplay() : Boolean?{
        if(Firebase.auth.currentUser?.reload()?.isSuccessful == true) {
            if (Firebase.auth.currentUser?.providerId == "facebook.com") {
                return true
            }
            return Firebase.auth.currentUser?.isEmailVerified
        }
        return false
    }

    private fun updateProfileListener(): View.OnClickListener {
        val listener = View.OnClickListener {

            Athlete.username = binding.etUsername.text.toString()
            //Athlete.profilePic = URL
            Athlete.profileBio = binding.etBio.text.toString()

            if(etUsername.text.isNotBlank()){
                val athlete = Athlete(Athlete.UID,Athlete.username,Athlete.profilePic,Athlete.profileBio,Athlete.spotifyplayList)
                dbReference.setValue(athlete)
            }

        }
        return listener
    }

    private fun updateStaticInfo(image : ImageView,username : TextView, bio : TextView){
        image.setImageDrawable(requireActivity().findViewById<ImageView>(R.id.profilepic_IV).drawable)
        username.text = Athlete.username
        bio.text = Athlete.profileBio
    }
    private fun updateVariableInfo(username : EditText, bio : EditText, email : EditText){
        username.setText(Athlete.username)
        bio.setText(Athlete.profileBio)
        email.setText(auth.currentUser?.email)
    }














    private fun GrabImageFromDisk(){
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            //SELEZIONA IMMAGINE

        }else{
            //CODICE REQUEST_CODE SERVE A IDENTIFICARE LA RISPOSTA CHE MI DARà IL COMPAT
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
        }
    }


    @Deprecated("Deprecated in Java", ReplaceWith("super.onRequestPermissionsResult(requestCode, permissions, grantResults)", "androidx.fragment.app.Fragment"))
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_CODE){
            GrabImageFromDisk()
        }else{
            Toast.makeText(requireContext(), "Oops, you just denied the permission for storage. You can also allow it from settings.", Toast.LENGTH_LONG).show()
        }
    }

}