package com.fitterAPP.fitter.FragmentControlers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.Classes.FitnessCard
import com.fitterAPP.fitter.ItemsAdapter.FitnessCardAdapter
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R

class MyFitnessCards : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v : View = inflater.inflate(R.layout.fragment_my_fitness_cards, container, false)

        var fitnessCads : ArrayList<FitnessCard> = ArrayList<FitnessCard>()

        fitnessCads.add(FitnessCard())
        fitnessCads.add(FitnessCard())
        fitnessCads.add(FitnessCard())
        fitnessCads.add(FitnessCard())
        fitnessCads.add(FitnessCard())
        fitnessCads.add(FitnessCard())
        fitnessCads.add(FitnessCard())
        fitnessCads.add(FitnessCard())

        val adapter : FitnessCardAdapter
        val recycle : RecyclerView = v.findViewById(R.id.MyFitnessCards_RV)
        adapter = context?.let { fitnessCads?.let { it1 -> FitnessCardAdapter((activity as MainActivity).baseContext, it1) } }!!
        recycle!!.setAdapter(adapter)
        return v
    }

}