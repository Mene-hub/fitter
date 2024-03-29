package com.fitterAPP.fitter.fragmentControllers

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.databases.StaticAthleteDatabase
import com.fitterAPP.fitter.databinding.FragmentFindprofileBinding
import com.fitterAPP.fitter.itemsAdapter.SuggestionAdapter
import com.google.firebase.database.*

/**
 * Fragment used to implement "find other users" feature
 * @author Daniel Satriano
 * @since 28/05/2022
 */
class FindProfile : Fragment() {
    private lateinit var binding : FragmentFindprofileBinding
    private lateinit var databaseReference : DatabaseReference
    private var suggestedUsers : MutableList<Athlete> = ArrayList()
    private lateinit var adapter : SuggestionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFindprofileBinding.inflate(inflater,container,false)

        databaseReference = StaticAthleteDatabase.database.getReference(getString(R.string.AthleteReference))

        binding.SVFindUsers.setOnQueryTextListener(queryTextListener())
        binding.SVFindUsers.requestFocus()
        binding.SVFindUsers.setOnQueryTextFocusChangeListener { view, hasFocus ->
            if (hasFocus) { showInputMethod(view.findFocus()) }
        }

        adapter = context?.let { SuggestionAdapter((activity as MainActivity), suggestedUsers) }!!
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    private fun showInputMethod(view: View) {
        val imm: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }


    /**
     * * Private listener for the SearchView which updates on every change in the textfield or when submit is pressed.
     * In particular onQueryTextChange is the method used for the research in the database which sends an input to databaseQuery(textString) whenever textString is not Blank.<br />
     * @see databaseQuery for more information about that method
     * @author Daniel Satriano
     * @since 28/05/2022
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
     * @since 28/05/2022
     */
    fun databaseQuery(text : String?){
        val usernameOrdered : Query  = databaseReference.orderByChild("username").startAt(text).endAt("$text\uF7FF").limitToFirst(10)
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
}