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
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.RealTimeDBHelper
import com.fitterAPP.fitter.databinding.ActivityLoginBinding.inflate
import com.fitterAPP.fitter.databinding.FragmentLoginBinding
import com.fitterAPP.fitter.databinding.FragmentMyFitnessCardsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.ktx.Firebase

class MyFitnessCards : Fragment() {
    private val TAG : String = "FragmentFitnessCard-"
    private lateinit var auth : FirebaseAuth
    private lateinit var currentUser : FirebaseUser

    private lateinit var binding : FragmentMyFitnessCardsBinding //Binding
    //firebase database
    private lateinit var user : Athlete
    private val fitnessCads : MutableList<FitnessCard> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyFitnessCardsBinding.inflate(inflater, container, false)

        //grab event from companion class RealTimeDBHelper
        RealTimeDBHelper.readToDoItems(getAthleteEventListener())

        //FIREBASE ACCOUNT
        auth = Firebase.auth
        currentUser = auth.currentUser!!
        //User update
        user.username = currentUser.displayName
        user.profilePic = currentUser.photoUrl
        user.UID = currentUser.uid


        fitnessCads.add(FitnessCard())
        fitnessCads.add(FitnessCard())
        fitnessCads.add(FitnessCard())
        fitnessCads.add(FitnessCard())
        fitnessCads.add(FitnessCard())
        fitnessCads.add(FitnessCard())
        fitnessCads.add(FitnessCard())
        fitnessCads.add(FitnessCard())

        val adapter : FitnessCardAdapter
        val recycle : RecyclerView = binding.MyFitnessCardsRV
        adapter = context?.let { fitnessCads?.let { it1 -> FitnessCardAdapter((activity as MainActivity).baseContext, it1) } }!!
        recycle!!.setAdapter(adapter)
        return binding.root
    }


    private fun getAthleteEventListener(): ChildEventListener {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(FitnessCard::class.java)
                //aggiungo nuova fitness card
                fitnessCads.add((item!!))
                //adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(FitnessCard::class.java)
                //cerco fitness card modificata
                val index = fitnessCads.indexOf(item)
                fitnessCads[index].set(item)
                //adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val item = snapshot.getValue(FitnessCard::class.java)
                fitnessCads.remove(item)
                //adapter.notifyDataSetChanged()
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