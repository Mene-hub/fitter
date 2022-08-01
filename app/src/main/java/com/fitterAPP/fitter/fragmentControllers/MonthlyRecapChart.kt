package com.fitterAPP.fitter.fragmentControllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.DayRecap
import com.fitterAPP.fitter.classes.ExerciseRecap
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.databinding.FragmentMonthlyRecapChartBinding
import com.fitterAPP.fitter.itemsAdapter.MonthlyRecapAdapter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import kotlin.collections.ArrayList


class MonthlyRecapChart : Fragment() {

    private lateinit var binding : FragmentMonthlyRecapChartBinding
    private val args by navArgs<MonthlyRecapChartArgs>()
    private lateinit var fitnessCard : FitnessCard
    private lateinit var adapter : MonthlyRecapAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentMonthlyRecapChartBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //grab bundle
        fitnessCard = args.fitnessCard

        //set text to textview in layout
        binding.TVMonthlyRecap.text = (context?.getString(R.string.MonthlyRecap) + ": ${fitnessCard.name}")

        val monthlyRecap : MutableList<DayRecap> = ArrayList()

        //set default settings to BarChart
        setDefaultBarChartSettings(binding.BarChartMonthlyRecap)

        //set adapter
        adapter = context?.let { MonthlyRecapAdapter((activity as MainActivity), monthlyRecap, binding.BarChartMonthlyRecap)}!!
        binding.monthlyRecapRecycler.adapter = adapter

        //fill list
        retrieveMonthlyData(monthlyRecap)

        //notify adapter of the changes
        adapter.notifyDataSetChanged()


    }

    /**
     * This method is used to retrieve the monthly recaps
     * @author Daniel Satriano
     * @since 1/08/2022
     * @param monthlyRecap list passed where it'll add all the recaps
     */
    //TODO("Farla reale")
    private fun retrieveMonthlyData(monthlyRecap: MutableList<DayRecap>) {

        val tmpExerciseRecap1 : MutableList<ExerciseRecap> = ArrayList()
        val tmpExerciseRecap2 : MutableList<ExerciseRecap> = ArrayList()
        tmpExerciseRecap1.add(ExerciseRecap("Tapis Roullant",10))
        tmpExerciseRecap1.add(ExerciseRecap("Chest Press",35))
        tmpExerciseRecap1.add(ExerciseRecap("Spinner",7))
        tmpExerciseRecap1.add(ExerciseRecap("Exercise 1",50))
        tmpExerciseRecap1.add(ExerciseRecap("Claudio 1kg",1))
        tmpExerciseRecap1.add(ExerciseRecap("Dani 200kg",200))
        tmpExerciseRecap1.add(ExerciseRecap("Dani pro",200))


        monthlyRecap.add(DayRecap("Luglio", fitnessCard.key, tmpExerciseRecap1 ))

        tmpExerciseRecap2.add(ExerciseRecap("Tapis Roullant",10))
        tmpExerciseRecap2.add(ExerciseRecap("Leverage Machine Chest Press",40))
        tmpExerciseRecap2.add(ExerciseRecap("Spinner",10))
        tmpExerciseRecap2.add(ExerciseRecap("Exercise 1",60))
        tmpExerciseRecap2.add(ExerciseRecap("Claudio 1kg",1))
        tmpExerciseRecap2.add(ExerciseRecap("Dani 200kg",200))
        tmpExerciseRecap2.add(ExerciseRecap("Dani pro",200))

        monthlyRecap.add(DayRecap("Agosto", fitnessCard.key, tmpExerciseRecap2 ))

    }

    /**
     * Used to set the initial parameter for the graph
     * @author Daniel Satriano
     * @since 1/08/2022
     */
    private fun setDefaultBarChartSettings(graph: BarChart) {

        graph.isDragEnabled = true
        graph.isScaleYEnabled = false
        graph.isDoubleTapToZoomEnabled = false
        graph.isHighlightPerTapEnabled = false
        graph.isHighlightPerDragEnabled = false

        graph.description.isEnabled = false
        graph.xAxis.setDrawAxisLine(false)
        graph.setVisibleXRangeMaximum(4f)
        graph.legend.isEnabled = false

        graph.animateY(1000)
        //TODO("extract")
        graph.setNoDataText("Click on a card to start checking your progress")
        graph.setNoDataTextTypeface(ResourcesCompat.getFont(requireContext(), R.font.roboto_regular))

        //region not working
        graph.setVisibleXRange(3f,6f)
        graph.setFitBars(false)
        //endregion


        val xAxis = graph.xAxis
        xAxis.granularity = 1F
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.setDrawGridLines(false)

    }



}