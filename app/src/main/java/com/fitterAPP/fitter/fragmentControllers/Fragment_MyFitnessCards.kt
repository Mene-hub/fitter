package com.fitterAPP.fitter.fragmentControllers

import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.fitterAPP.fitter.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.itemsAdapter.FitnessCardAdapter
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.classes.CardsCover
import com.fitterAPP.fitter.databinding.FragmentMyFitnessCardsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class MyFitnessCards : Fragment() {
    private lateinit var binding : FragmentMyFitnessCardsBinding //Binding
    private lateinit var adapter : FitnessCardAdapter
    private lateinit var dbReference : DatabaseReference
    //firebase database
    private val fitnessCads : MutableList<FitnessCard> = ArrayList()
    private var dummyCard : FitnessCard = FitnessCard()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentMyFitnessCardsBinding.inflate(inflater, container, false)

        dbReference = StaticFitnessCardDatabase.database.getReference(getString(R.string.FitnessCardsReference))

        dummyCard = FitnessCard("","",null,null,"addCard",CardsCover.addCard)
        //grab event from companion class RealTimeDBHelper
        StaticFitnessCardDatabase.setFitnessCardChildListener(dbReference, Athlete.UID, getFitnessCardEventListener())


        val recycle : RecyclerView = binding.MyFitnessCardsRV
        adapter = context?.let { FitnessCardAdapter((activity as MainActivity), fitnessCads, this) }!!
        if(fitnessCads.indexOf(dummyCard) == -1){
            fitnessCads.add(dummyCard)
        }
        recycle.adapter = adapter

        adapter.notifyDataSetChanged()

        Log.w("Fragment", binding.MyFitnessCardsRV.id.toString())
        return binding.root
    }

    /**
     * @author Daniel Satriano
     * @author Claudio Menegotto
     */
    private fun transaction(newFitnessCard : FitnessCard) {
        val action : NavDirections = MyFitnessCardsDirections.actionMyFitnessCardsToFragmentShowCardDialog(newFitnessCard)
        findNavController().navigate(action)
    }

    /**
     * @author Daniel Satriano
     * @param card FitnessCard object
     * @see Athlete
     * @see FitnessCard
     */
    fun addFitnessCard(card : FitnessCard){
        StaticFitnessCardDatabase.setFitnessCardItem(dbReference, Athlete.UID, card)
    }

    fun showAlertDialogFitnessCard(){
        val newFitnessCard = FitnessCard()
        // Create an alert builder
        val builder = MaterialAlertDialogBuilder(requireContext(),  R.style.ThemeOverlay_App_MaterialAlertDialog)
        // set the custom layout
        val customLayout: View = layoutInflater.inflate(R.layout.dialog_input_text, null)
        builder.setView(customLayout)
        // add a button
        builder
            .setPositiveButton("OK") { _, _ -> // send data from the
                // AlertDialog to the Activity
                val name = customLayout.findViewById<EditText>(R.id.et_cardName).text.toString()
                val description = customLayout.findViewById<EditText>(R.id.et_description).text.toString()
                val duration = customLayout.findViewById<EditText>(R.id.et_duration).text.toString()

                if((name.isNotBlank() && name != "") || (duration.isNotBlank() && duration != "")){
                    newFitnessCard.name = name
                    newFitnessCard.description = description
                    newFitnessCard.key = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm:ss"))
                    newFitnessCard.timeDuration = duration.toInt()

                    addFitnessCard(newFitnessCard)

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
            .show()
    }


    private fun getFitnessCardEventListener(): ChildEventListener {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(FitnessCard::class.java)
                //aggiungo nuova fitness card
                if(!fitnessCads.contains(item)) {
                    fitnessCads.remove(dummyCard)
                    fitnessCads.add((item!!))
                    fitnessCads.add(dummyCard)
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
                Log.w("Fragment_MyFitnessCards: ", "postcomments:onCancelled", error.toException())
                Toast.makeText(activity?.baseContext, "Failed to load comment.", Toast.LENGTH_SHORT).show()
            }
        }
        return childEventListener
    }

}