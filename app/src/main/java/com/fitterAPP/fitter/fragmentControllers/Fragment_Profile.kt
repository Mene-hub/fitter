package com.fitterAPP.fitter.fragmentControllers

import com.fitterAPP.fitter.databases.StaticAthleteDatabase
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

/**
 * Fragment used to display user profile information and to allow him/her to modify it
 * @author Daniel Satriano
 * @since 30/05/2022
 */
class Profile : Fragment() {

    private val REQUEST_CODE = 121
    private lateinit var binding : FragmentProfileBinding

    private lateinit var auth : FirebaseAuth
    private lateinit var dbReference : DatabaseReference

    private lateinit var etUsername : EditText
    private lateinit var etBio : EditText
    private lateinit var etEmail : EditText

    /**
     * @author Daniel Satriano
     * @since 30/05/2022
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        dbReference = StaticAthleteDatabase.database.getReference(getString(R.string.AthleteReference))

        auth = Firebase.auth

        etUsername = binding.etUsername
        etBio = binding.etBio
        etEmail = binding.etEmail


        val btnUpdate = binding.btnUpdate

        updateVariableInfo(etUsername,etBio,etEmail)

        btnUpdate.setOnClickListener(updateProfileListener())

        return binding.root
    }
    /**
     * Update button listener, used to update data in the Realtime Database, it also closes the window once the update has been done and gives a Toast message to the user
     * as a feedback
     * @author Daniel Satriano
     * @since 30/05/2022
     */
    private fun updateProfileListener(): View.OnClickListener {
        val listener = View.OnClickListener {

            val check1 = binding.etUsername.text.toString().split("_").count()
            val check2 = binding.etUsername.text.toString().split(".").count()

            if(check1 <= 2 && check2 <= 2) {
                Athlete.username = binding.etUsername.text.toString()

                //Athlete.profilePic = URL
                Athlete.profileBio = binding.etBio.text.toString()

                if (etUsername.text.isNotBlank()) {

                    val athlete = Athlete(
                        Athlete.UID,
                        Athlete.username,
                        Athlete.profilePic,
                        Athlete.profileBio,
                        Athlete.spotifyplayList
                    )
                    StaticAthleteDatabase.setAthleteItem(dbReference, Athlete.UID, athlete)
                }

                Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_profile_to_myFitnessCards)
            }else{
                binding.etUsername.error = "Username can only contain one '.' or one '_'"
            }
        }
        return listener
    }

    /**
     * Method used to update all the EditTexts in the UI with the current data stored in [Athlete] companion class
     * @author Daniel Satriano
     * @since 30/05/2022
     */
    private fun updateVariableInfo(username : EditText, bio : EditText, email : EditText){
        username.setText(Athlete.username)
        bio.setText(Athlete.profileBio)
        email.setText(auth.currentUser?.email)
    }

}