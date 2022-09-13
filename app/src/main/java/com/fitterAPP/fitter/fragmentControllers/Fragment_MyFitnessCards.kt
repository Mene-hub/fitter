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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.itemsAdapter.FitnessCardAdapter
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.classes.CardsCover
import com.fitterAPP.fitter.databinding.FragmentMyFitnessCardsBinding
import com.fitterAPP.fitter.itemsAdapter.HomeRecapAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class MyFitnessCards : Fragment() {
    private lateinit var binding : FragmentMyFitnessCardsBinding //Binding
    private lateinit var adapter : FitnessCardAdapter
    private lateinit var recapAdapter: HomeRecapAdapter
    private lateinit var dbReference : DatabaseReference
    //firebase database
    private val fitnessCads : MutableList<FitnessCard> = ArrayList()
    private val recapCards : MutableList<FitnessCard> = ArrayList()


    private companion object{
        //TAG for debugging
        private const val TAG = "FRAGMENT_MyFitnessCards"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentMyFitnessCardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbReference = StaticFitnessCardDatabase.database.getReference(getString(R.string.FitnessCardsReference))

        val recycle : RecyclerView = binding.MyFitnessCardsRV
        adapter = context?.let { FitnessCardAdapter((activity as MainActivity), fitnessCads, this) }!!
        recycle.adapter = adapter


        val mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        val recapRecycle : RecyclerView = binding.MyRecapsRV
        recapAdapter = context?.let { HomeRecapAdapter((activity as MainActivity), recapCards) }!!
        recapRecycle.adapter = recapAdapter

        StaticFitnessCardDatabase.setSingleValueEventListener(dbReference, Athlete.UID, initialCardDownload())

        binding.addCardBT.setOnClickListener(buttonAddCardListener())

        Log.w(TAG, binding.MyFitnessCardsRV.id.toString())
    }

    /**
     * TMP method (and button) used to call [showAlertDialogFitnessCard]
     * @author Daniel Satriano
     * @since 20/08/2022
     */
    private fun buttonAddCardListener(): View.OnClickListener {
        return View.OnClickListener {
            showAlertDialogFitnessCard()
        }
    }

    /**
     * This method is used primarily for the shimmer effect, so that it gets done once and not everytime like if it was in a child event listener
     * @author Daniel Satriano
     * @since 11/08/2022
     */
    private fun initialCardDownload(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for(tmp in snapshot.children){
                    val item = tmp.getValue(FitnessCard::class.java)
                    //aggiungo nuova fitness card
                    if(!fitnessCads.contains(item)) {
                        fitnessCads.add((item!!))
                        adapter.notifyItemInserted(fitnessCads.indexOf(item))
                    }
                    if(!recapCards.contains(item)){
                        recapCards.add(item!!)
                        recapAdapter.notifyItemInserted(recapCards.indexOf(item))
                    }
                }
                for (item in fitnessCads){
                    Log.d("FITNESS_CARDS" ,item.toString())
                }

                binding.MyFitnessCardsShimmerRV.visibility = View.INVISIBLE
                binding.MyRecapsShimmerRV.visibility = View.INVISIBLE
                binding.MyFitnessCardsShimmerRV.stopShimmer()
                binding.MyRecapsShimmerRV.stopShimmer()

                binding.MyFitnessCardsRV.visibility = View.VISIBLE
                binding.MyRecapsRV.visibility = View.VISIBLE
                StaticFitnessCardDatabase.setFitnessCardChildListener(dbReference, Athlete.UID, getFitnessCardEventListener())

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
            }

        }

    }

    /**
     * @author Daniel Satriano
     * @param card FitnessCard object
     * @see Athlete
     * @see FitnessCard
     */
    private fun addFitnessCard(card : FitnessCard){
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

                if((name.isNotBlank() && name != "") && (duration.isNotBlank() && duration != "")){
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


    /**
     * Called right after the value listener has been called
     * @author Daniel Satriano
     * @since 1/06/2022
     */
    private fun getFitnessCardEventListener(): ChildEventListener {
        val childEventListener = object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(FitnessCard::class.java)
                //aggiungo nuova fitness card
                if(!fitnessCads.contains(item)) {
                    fitnessCads.add((item!!))
                    adapter.notifyItemInserted(fitnessCads.indexOf(item))
                }
                if(!recapCards.contains(item)){
                    recapCards.add(item!!)
                    recapAdapter.notifyItemInserted(recapCards.indexOf(item))
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

}