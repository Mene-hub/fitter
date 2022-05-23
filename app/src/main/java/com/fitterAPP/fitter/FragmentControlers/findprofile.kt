package com.fitterAPP.fitter.FragmentControlers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.databinding.FragmentFindprofileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class findprofile : Fragment() {

    private lateinit var binding : FragmentFindprofileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentFindprofileBinding.inflate(inflater, container, false)

        binding.SVFindUsers.isIconified = false
        val bottomNavigation = (requireContext() as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener(bottomNavigationListener())


        return binding.root
    }

    private fun bottomNavigationListener(): NavigationBarView.OnItemSelectedListener {
        val listener = NavigationBarView.OnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.userProfile -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.addCard -> {
                    // Respond to navigation item 3 click
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.addToBackStack("MyFitnessCardsFragment")
                    transaction.replace(R.id.FragmentContainer, MyFitnessCards())
                    item.icon = AppCompatResources.getDrawable(requireContext(),R.drawable.ic_plus)
                    item.title = getString(R.string.AddCard)
                    transaction.commit()
                    true
                }
                R.id.search -> {
                    // Respond to navigation item 3 click

                    true
                }
                else -> false
            }
        }
        return listener
    }

}