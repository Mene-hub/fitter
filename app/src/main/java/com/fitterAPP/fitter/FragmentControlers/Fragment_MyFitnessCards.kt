package com.fitterAPP.fitter.FragmentControlers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    private var dbReference : DatabaseReference = FirebaseDatabase.getInstance(RealTimeDBHelper.getDbURL()).getReference(_REFERENCE)
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
        adapter.notifyDataSetChanged()
        recycle.adapter = adapter

        Log.w("Fragment", binding.MyFitnessCardsRV.id.toString())
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        addFitnessCard(Athlete.UID, FitnessCard())
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

    fun addFitnessCard(UID : String, card : MutableList<FitnessCard>){
        databaseHelper.setFitnessCardItem(UID,card)
    }

    private fun getAthleteEventListener(): ChildEventListener {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(FitnessCard::class.java)
                //aggiungo nuova fitness card
                fitnessCads.add((item!!))
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(FitnessCard::class.java)
                //cerco fitness card modificata
                val index = fitnessCads.indexOf(item)
                fitnessCads[index].set(item)
                adapter.notifyDataSetChanged()
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