package com.fitterAPP.fitter.fragmentControllers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.classes.DayRecap
import com.fitterAPP.fitter.classes.ExerciseRecap
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.databinding.FragmentMonthlyRecapChartBinding
import com.fitterAPP.fitter.itemsAdapter.FitnessCardAdapter
import com.fitterAPP.fitter.itemsAdapter.MonthlyRecapAdapter


class MonthlyRecapChart : Fragment() {

    private lateinit var binding : FragmentMonthlyRecapChartBinding
    private val args by navArgs<MonthlyRecapChartArgs>()
    private lateinit var fitnessCard : FitnessCard
    private lateinit var adapter : MonthlyRecapAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMonthlyRecapChartBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fitnessCard = args.fitnessCard
        binding.TVMonthlyRecap.text = "Recap mensili della scheda: ${fitnessCard.name}"
        val monthlyRecap : MutableList<DayRecap> = ArrayList()



        adapter = context?.let { MonthlyRecapAdapter((activity as MainActivity), monthlyRecap, binding.BarChartMonthlyRecap)}!!
        binding.monthlyRecapRecycler.adapter = adapter

        retrieveMonthlyData(monthlyRecap)
        adapter.notifyDataSetChanged()



    }


    private fun retrieveMonthlyData(monthlyRecap: MutableList<DayRecap>) {

        val tmpExerciseRecap : MutableList<ExerciseRecap> = ArrayList()
        tmpExerciseRecap.add(ExerciseRecap(0,80))
        tmpExerciseRecap.add(ExerciseRecap(1,20))
        tmpExerciseRecap.add(ExerciseRecap(2,50))


        monthlyRecap.add(DayRecap("Luglio", fitnessCard.key, tmpExerciseRecap ))

        tmpExerciseRecap.clear()
        tmpExerciseRecap.add(ExerciseRecap(0,120))
        tmpExerciseRecap.add(ExerciseRecap(1,40))
        tmpExerciseRecap.add(ExerciseRecap(2,80))

        monthlyRecap.add(DayRecap("Agosto", fitnessCard.key, tmpExerciseRecap ))

    }


}