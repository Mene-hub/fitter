package com.fitterAPP.fitter.FragmentControlers

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.Classes.Athlete
import com.fitterAPP.fitter.Classes.Exercise
import com.fitterAPP.fitter.Classes.FitnessCard
import com.fitterAPP.fitter.ItemsAdapter.FitnessCardAdapter
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.RealTimeDBHelper
import com.fitterAPP.fitter.databinding.FragmentMyFitnessCardsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import com.fitterAPP.fitter.R

class MyFitnessCards : Fragment() {
    private val TAG : String = "FragmentFitnessCard-"
    private val _REFERENCE : String = "FITNESS_CARDS"
    private lateinit var binding : FragmentMyFitnessCardsBinding //Binding
    private lateinit var adapter : FitnessCardAdapter
    private var dbReference : DatabaseReference = FirebaseDatabase.getInstance(RealTimeDBHelper.getDbURL()).getReference(_REFERENCE).child("${Athlete.UID}-FITNESSCARD")
    private var databaseHelper : RealTimeDBHelper = RealTimeDBHelper(dbReference) //USING DEFAULT VALUE
    //firebase database
    private val fitnessCads : MutableList<FitnessCard> = ArrayList()
    private lateinit var bottomNavigation : BottomNavigationView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentMyFitnessCardsBinding.inflate(inflater, container, false)

        //grab event from companion class RealTimeDBHelper
        databaseHelper.readItems(getAthleteEventListener())



        val recycle : RecyclerView = binding.MyFitnessCardsRV
        adapter = context?.let { FitnessCardAdapter((activity as MainActivity), fitnessCads) }!!
        recycle.adapter = adapter

        bottomNavigation = (requireContext() as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation)
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
                    showAlertDialogFitnessCard()
                    // Respond to navigation item 2 click
                    true
                }
                R.id.search -> {
                    // Respond to navigation item 3 click
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.addToBackStack("FindProfileFragment")
                    transaction.replace(R.id.FragmentContainer, findprofile())
                    bottomNavigation.menu.clear()
                    bottomNavigation.inflateMenu(R.menu.bot_nav_fromsearch_to_home)
                    transaction.commit()
                    true
                }
                else -> false
            }
        }
        return listener
    }



    private fun createNewCard(): View.OnClickListener {
        val listener = View.OnClickListener {
            showAlertDialogFitnessCard()
        }
        return listener
    }

    private fun transaction(newFitnessCard : FitnessCard) {

        val fragmentManager = parentFragmentManager
        val newFragment = Fragment_showCardDialog(newFitnessCard)

        // The device is smaller, so show the fragment fullscreen
        val transaction = fragmentManager.beginTransaction()
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction
            .replace(android.R.id.content, newFragment)
            .addToBackStack(null)
            .commit()

    }

    /**
     * @author Daniel Satriano
     * @param card FitnessCard object
     * @see Athlete
     * @see FitnessCard
     */
    fun addFitnessCard(card : FitnessCard){

        databaseHelper.setFitnessCardItem(card)
    }

    private fun getAthleteEventListener(): ChildEventListener {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(FitnessCard::class.java)
                //aggiungo nuova fitness card
                if(!fitnessCads.contains(item)) {
                    fitnessCads.add((item!!))
                    adapter.notifyItemInserted(fitnessCads.indexOf(item))
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(FitnessCard::class.java)
                //cerco fitness card modificata

                val index = fitnessCads.indexOf(fitnessCads.find { it.key == item?.key })

                fitnessCads[index].set(item)
                adapter.notifyItemChanged(index)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val item = snapshot.getValue(FitnessCard::class.java)
                val index = fitnessCads.indexOf(item)
                fitnessCads.remove(item)
                adapter.notifyItemRemoved(index)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //viene triggerato quando la locazione del child cambia
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "postcomments:onCancelled", error.toException())
                Toast.makeText(activity?.baseContext, "Failed to load comment.", Toast.LENGTH_SHORT).show()
            }
        }
        return childEventListener
    }

    fun showAlertDialogFitnessCard(){

        var newFitnessCard = FitnessCard()

        // Create an alert builder
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Create new fitness card")

        // set the custom layout
        val customLayout: View = layoutInflater.inflate( com.fitterAPP.fitter.R.layout.dialog_input_text, null)
        builder.setView(customLayout)

        // add a button
        builder
            .setPositiveButton("OK") { _, _ -> // send data from the
                // AlertDialog to the Activity
                val name = customLayout.findViewById<EditText>(com.fitterAPP.fitter.R.id.et_cardName).text.toString()
                val description = customLayout.findViewById<EditText>(com.fitterAPP.fitter.R.id.et_description).text.toString()
                val duration = customLayout.findViewById<EditText>(com.fitterAPP.fitter.R.id.et_duration).text.toString()

                if((!name.isNullOrBlank() && name != "") || (!duration.isNullOrBlank() && duration != "")){
                    newFitnessCard.name = name
                    newFitnessCard.description = description
                    newFitnessCard.key = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm:ss"))
                    newFitnessCard.timeDuration = duration.toInt()

                    addFitnessCard(newFitnessCard)

                    transaction(newFitnessCard)
                }else{
                    Toast.makeText(requireContext(), "Missing name field on fitness card", Toast.LENGTH_LONG).show()
                }
            }.setNegativeButton("BACK") { _, _ ->
            }.setOnDismissListener {
                if(newFitnessCard.name?.replace(" ","")?.length == 0)
                {
                    Toast.makeText(activity, "insert a valid name", Toast.LENGTH_SHORT).show()
                    activity?.onBackPressed()
                }
            }
            .setIcon(AppCompatResources.getDrawable(requireContext(),com.fitterAPP.fitter.R.drawable.fitness_24)).show()
    }

}