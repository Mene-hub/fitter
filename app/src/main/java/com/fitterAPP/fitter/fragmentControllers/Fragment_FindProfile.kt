package com.fitterAPP.fitter.fragmentControllers

import com.fitterAPP.fitter.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.findNavController
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.databases.RealTimeDBHelper
import com.fitterAPP.fitter.databinding.FragmentFindprofileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.database.*

class FindProfile : Fragment() {
    private lateinit var binding : FragmentFindprofileBinding
    private var databaseReference: DatabaseReference = FirebaseDatabase.getInstance(RealTimeDBHelper.getDbURL()).getReference("USERS")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFindprofileBinding.inflate(inflater,container,false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener(bottomNavItemSelected())

        binding.SVFindUsers.clearFocus()
        binding.SVFindUsers.setOnQueryTextListener(queryTextListener())


        return binding.root
    }

    private fun queryTextListener(): SearchView.OnQueryTextListener {
        val listener = object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.SVFindUsers.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!!.isNotBlank()) {
                    databaseQuery(newText)
                }
                return true
            }
        }
        return listener
    }

    fun databaseQuery(text : String?){
        val usernameOrdered : Query  = databaseReference.orderByChild("username").startAt(text).endAt("$text\uF7FF") .limitToFirst(20)
        Log.d("FindProfile", "---------------------")
        usernameOrdered.addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(Athlete::class.java)
                Log.d("FindProfile", item?.username.toString())

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
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
                    item.isChecked = true
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