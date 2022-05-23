package com.fitterAPP.fitter.FragmentControlers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.Classes.Exercise
import com.fitterAPP.fitter.Classes.FitnessCard
import com.fitterAPP.fitter.ItemsAdapter.FitnessCardExercisesAdapter
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.databinding.FragmentModifyCardBinding
import com.fitterAPP.fitter.databinding.FragmentShowCardDialogBinding

class ModifyCard(var fitnessCard: FitnessCard) : DialogFragment() {

    private lateinit var binding : FragmentModifyCardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentModifyCardBinding.inflate(inflater, container, false)

        binding.backBt.setOnClickListener {
            activity?.onBackPressed()
        }

        val recycle : RecyclerView = binding.exercisesListRV

        var exs : MutableList<Exercise> = ArrayList()

        exs.add(Exercise())
        exs.add(Exercise())
        exs.add(Exercise())
        exs.add(Exercise())
        exs.add(Exercise())
        exs.add(Exercise())
        exs.add(Exercise())
        exs.add(Exercise())
        exs.add(Exercise())
        exs.add(Exercise())

        fitnessCard.exercises = exs

        if(fitnessCard.exercises != null && fitnessCard.exercises?.size!! > 0){
            var adapter = context?.let { FitnessCardExercisesAdapter((activity as MainActivity),fitnessCard,fitnessCard.exercises!!, true) }!!
            recycle.adapter = adapter
        }

        val cardName : TextView = binding.CardNameTV
        val cardDuration : TextView = binding.TimeDurationTV
        val cardDescription: TextView = binding.DescriptionTV

        cardName.text = fitnessCard.name
        cardDescription.text = fitnessCard.description
        cardDuration.text = fitnessCard.timeDuration.toString() + " minutes"


        return binding.root
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val a: Animation = object : Animation() {}
        a.duration = 0
        return a
    }

}