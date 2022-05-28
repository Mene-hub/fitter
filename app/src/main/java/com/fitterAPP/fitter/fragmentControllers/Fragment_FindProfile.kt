package com.fitterAPP.fitter.fragmentControllers

import android.annotation.SuppressLint
import com.fitterAPP.fitter.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.databases.RealTimeDBHelper
import com.fitterAPP.fitter.databinding.FragmentFindprofileBinding
import com.fitterAPP.fitter.itemsAdapter.SuggestionAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.database.*

class FindProfile : Fragment() {
    private lateinit var binding : FragmentFindprofileBinding
    private var databaseReference: DatabaseReference = FirebaseDatabase.getInstance(RealTimeDBHelper.getDbURL()).getReference("USERS")
    private var suggestedUsers : MutableList<Athlete> = ArrayList()
    private lateinit var adapter : SuggestionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFindprofileBinding.inflate(inflater,container,false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener(bottomNavItemSelected())

        binding.SVFindUsers.clearFocus()
        binding.SVFindUsers.setOnQueryTextListener(queryTextListener())


        adapter = context?.let { SuggestionAdapter((activity as MainActivity), suggestedUsers) }!!
        binding.recyclerView.adapter = adapter

        return binding.root
    }


    /**
     * * Private listener for the SearchView which updates on every change in the textfield or when submit is pressed.
     * In particular onQUeryTextChange is the method used for the research in the database which sends an input to databaseQuery(textString) whenever textString is not Blank.<br />
     * @see databaseQuery for more information about that method
     * @author Daniel Satriano
     */
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


    /**
     * * databaseQuery is a function that is called in queryTextListener() and it queries the database for the particular string given as input (text : String?).
     * * for more information about the database query method check the [documentation](https://firebase.google.com/docs/database/android/lists-of-data#sorting_and_filtering_data)
     * * This function also has inside a child listener which will get updates everytime it finds new child that have inside the given string
     * @see queryTextListener for more information about that function
     * @author Daniel Satriano
     */
    @SuppressLint("NotifyDataSetChanged")
    fun databaseQuery(text : String?){
        val usernameOrdered : Query  = databaseReference.orderByChild("username").startAt(text).endAt("$text\uF7FF").limitToFirst(10)
        //.get().addOnSuccessListener
        suggestedUsers.clear()
        adapter.notifyDataSetChanged()
        usernameOrdered.addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(Athlete::class.java)
                suggestedUsers.add(item!!)
                adapter.notifyItemChanged(suggestedUsers.indexOf(item))
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("FindProfile", "entro changed")
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("FindProfile", "entro removed")
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("FindProfile", "entro cancelled")
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