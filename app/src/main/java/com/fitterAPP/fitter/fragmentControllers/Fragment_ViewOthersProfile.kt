package com.fitterAPP.fitter.fragmentControllers

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.facebook.shimmer.ShimmerFrameLayout
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.fitterAPP.fitter.databinding.FragmentViewOthersProfileBinding
import com.fitterAPP.fitter.itemsAdapter.FitnessCardAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.squareup.picasso.Picasso

class Fragment_ViewOthersProfile : DialogFragment() {

    private lateinit var binding : FragmentViewOthersProfileBinding
    private lateinit var adapter : FitnessCardAdapter
    private val cardList : MutableList<FitnessCard> = mutableListOf()
    private val args by navArgs<Fragment_ViewOthersProfileArgs>()
    private lateinit var shimmerFrameLayout : ShimmerFrameLayout

    /**
     * onCreate method which is used to set the dialog style. This mathod is paired with a WindowManager setting done in [onCreateView]
     * @author Daniel Satriano
     * @since 25/05/2022
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentViewOthersProfileBinding.inflate(inflater,container,false)

        shimmerFrameLayout = binding.RVCardsShimmer
        shimmerFrameLayout.startShimmer()

        //Set transparent status bar
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        //Get FitnessCard by bundle passed via navigation controller in [FitnessCardAdapter.kt] (the bundle is also set in fragment_navigation.xml
        val athlete : Athlete = args.bundleAthlete

        //Setting up top bar informations, name, profile picture and bio
        binding.TVUsername.text = athlete.username
        binding.TVBiography.text = athlete.profileBio
        if(athlete.profilePic != "") {
            val image = binding.profilepicIV
            Picasso.get()
                .load(athlete.profilePic)
                .resize(100, 100)
                .centerCrop()
                .into(image)
        }

        val tv_userCards = binding.TVUsernameCards

        adapter = context?.let { FitnessCardAdapter((activity as MainActivity), cardList, null) }!!
        binding.RVCards.adapter = adapter

        retrieveUserCards(athlete)

        return binding.root
    }


    private fun retrieveUserCards(athlete : Athlete) {
        val reference = StaticFitnessCardDatabase.database.getReference(getString(R.string.FitnessCardsReference))
        StaticFitnessCardDatabase.setFitnessCardChildListener(reference, athlete.UID, cardListener())
        shimmerFrameLayout.stopShimmer()
        binding.RVCardsShimmer.visibility = View.GONE
        binding.RVCards.visibility = View.VISIBLE
    }

    private fun cardListener(): ChildEventListener {
        return object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(FitnessCard::class.java)
                cardList.add(item!!)
                adapter.notifyItemInserted(cardList.indexOf(item))
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
    }

}