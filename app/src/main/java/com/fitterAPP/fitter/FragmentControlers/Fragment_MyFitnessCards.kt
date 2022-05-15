package com.fitterAPP.fitter.FragmentControlers

import android.R
import com.fitterAPP.fitter.R.layout
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.Classes.Athlete
import com.fitterAPP.fitter.Classes.FitnessCard
import com.fitterAPP.fitter.ItemsAdapter.FitnessCardAdapter
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.RealTimeDBHelper
import com.fitterAPP.fitter.databinding.FragmentMyFitnessCardsBinding
import com.google.firebase.database.*


class MyFitnessCards : Fragment() {
    private val TAG : String = "FragmentFitnessCard-"
    private lateinit var databaseHelper : RealTimeDBHelper
    private val _REFERENCE : String = "FITNESS_CARDS"
    private lateinit var binding : FragmentMyFitnessCardsBinding //Binding
    private lateinit var adapter : FitnessCardAdapter
    private var dbReference : DatabaseReference = FirebaseDatabase.getInstance(RealTimeDBHelper.getDbURL()).getReference(_REFERENCE).child("${Athlete.UID}-FITNESSCARD")
    //firebase database
    private val fitnessCads : MutableList<FitnessCard> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentMyFitnessCardsBinding.inflate(inflater, container, false)

        //grab event from companion class RealTimeDBHelper
        databaseHelper = RealTimeDBHelper(dbReference) //USING DEFAULT VALUE
        databaseHelper.readItems(getAthleteEventListener())

        val recycle : RecyclerView = binding.MyFitnessCardsRV
        adapter = context?.let { FitnessCardAdapter((activity as MainActivity), fitnessCads) }!!
        recycle.adapter = adapter

        //create new fitnessCard
        binding.FAPAddNewCard.setOnClickListener(createNewCard())


        Log.w("Fragment", binding.MyFitnessCardsRV.id.toString())
        return binding.root
    }

    private fun createNewCard(): View.OnClickListener? {
        val listener = View.OnClickListener {
            showAlertDialogFitnessCard()
        }
        return listener
    }

    private fun transaction(newFitnessCard : FitnessCard) {

        if (newFitnessCard != null) {
            val fragmentManager = parentFragmentManager
            val newFragment = Fragment_createCardDialog(newFitnessCard)

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
    }

    /**
     * @author Daniel Satriano
     * @param UID use Athlete.UID
     * @param card FitnessCard object
     * @see Athlete
     * @see FitnessCard
     */
    fun addFitnessCard(UID : String, card : MutableList<FitnessCard>){
        databaseHelper.setFitnessCardItem(UID, card)
    }

    fun addFitnessCard(UID : String, card : FitnessCard){
        databaseHelper.setFitnessCardItem(card)
    }

    private fun getAthleteEventListener(): ChildEventListener {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(FitnessCard::class.java)
                Log.d("Mmmm", item?.key.toString())
                Log.d("Mmmm", item?.description.toString())
                Log.d("Mmmm", item?.name.toString())
                //aggiungo nuova fitness card
                fitnessCads.add((item!!))
                adapter.notifyItemInserted(fitnessCads.indexOf(item))
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

        var newFitnessCard : FitnessCard = FitnessCard()

        // Create an alert builder
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle("Card Name")

        // set the custom layout
        val customLayout: View = layoutInflater.inflate( com.fitterAPP.fitter.R.layout.dialog_input_text, null)
        builder.setView(customLayout)

        // add a button
        builder
            .setPositiveButton(
                "OK",
                DialogInterface.OnClickListener { dialog, which -> // send data from the
                    // AlertDialog to the Activity
                    val name = customLayout.findViewById<EditText>(com.fitterAPP.fitter.R.id.cardName_ET)
                    val description = customLayout.findViewById<EditText>(com.fitterAPP.fitter.R.id.CardDescritpion_ET)
                    newFitnessCard?.name = name.text.toString()
                    newFitnessCard?.description = description.text.toString()

                    transaction(newFitnessCard)
                })

            .setNegativeButton(
                "BACK",
                DialogInterface.OnClickListener{dialog, which ->
                }
            )

            .setOnDismissListener {
                if(newFitnessCard == null)
                    activity?.onBackPressed()
                else
                    if(newFitnessCard?.name?.replace(" ","")?.length == 0)
                    {
                        Toast.makeText(activity, "insert a valid name", Toast.LENGTH_SHORT).show()
                        activity?.onBackPressed()
                    }
            }

        // create and show
        // the alert dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

}