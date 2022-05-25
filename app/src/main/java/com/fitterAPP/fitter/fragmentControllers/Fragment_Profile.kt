package com.fitterAPP.fitter.fragmentControllers

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fitterAPP.fitter.databinding.FragmentProfileBinding

class Profile : Fragment() {

    private val REQUEST_CODE = 121
    private lateinit var binding : FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
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