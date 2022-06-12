package com.fitterAPP.fitter.fragmentControllers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.databinding.FragmentViewOthersProfileBinding
import com.fitterAPP.fitter.itemsAdapter.FitnessCardAdapter

class Fragment_ViewOthersProfile : DialogFragment() {

    private lateinit var binding : FragmentViewOthersProfileBinding
    private lateinit var adapter : FitnessCardAdapter
    private val cardList : MutableList<FitnessCard> = mutableListOf()

    /**
     * onCreate method which is used to set the dialog style. This mathod is paired with a WindowManager setting done in [onCreateView]
     * @author Daniel Satriano
     * @since 25/05/2022
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentViewOthersProfileBinding.inflate(inflater,container,false)

        //Set transparent status bar
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val username = binding.TVUsername
        val bio = binding.TVBiography
        val profilePIC = binding.profilepicIV

        val tv_userCards = binding.TVUsernameCards

        adapter = context?.let { FitnessCardAdapter((activity as MainActivity), cardList, null) }!!
        binding.RVCards.adapter = adapter


        return binding.root
    }

}