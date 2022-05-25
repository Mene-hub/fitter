package com.fitterAPP.fitter.fragmentControllers

import com.fitterAPP.fitter.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fitterAPP.fitter.databinding.FragmentFindprofileBinding
import com.fitterAPP.fitter.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class FindProfile : Fragment() {
    private lateinit var binding : FragmentFindprofileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFindprofileBinding.inflate(inflater,container,false)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener(bottomNavItemSelected())

        return binding.root
    }


    private fun bottomNavItemSelected(): NavigationBarView.OnItemSelectedListener {
        val listener = NavigationBarView.OnItemSelectedListener{ item ->
            when (item.itemId){
                R.id.home ->{
                    findNavController().navigate(R.id.action_findprofile_to_myFitnessCards)
                    true
                }
                R.id.addCard ->{

                    true
                }
                R.id.findprofile ->{

                    true
                }
                else ->{
                    false
                }
            }
        }
        return listener

    }


}