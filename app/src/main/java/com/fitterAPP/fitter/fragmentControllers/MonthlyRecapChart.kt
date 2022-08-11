package com.fitterAPP.fitter.fragmentControllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.classes.MonthlyRecap
import com.fitterAPP.fitter.databases.StaticRecapDatabase
import com.fitterAPP.fitter.databinding.FragmentMonthlyRecapChartBinding
import com.fitterAPP.fitter.itemsAdapter.MonthlyRecapAdapter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
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
        binding.TVMonthlyRecap.text = context?.getString(R.string.MonthlyRecap).plus(": ${fitnessCard.name}")

        val monthlyRecap : MutableList<MonthlyRecap> = ArrayList()

        //set default settings to BarChart
        setDefaultBarChartSettings(binding.BarChartMonthlyRecap)

        //set adapter
        adapter = context?.let { MonthlyRecapAdapter((activity as MainActivity), monthlyRecap, binding.BarChartMonthlyRecap)}!!
        binding.monthlyRecapRecycler.adapter = adapter

        //fill list
        StaticRecapDatabase.setRecapChildListener(
            StaticRecapDatabase.database.getReference(getString(R.string.RecapReference)),
            Athlete.UID,
            fitnessCard.key,
            monthlyRecapListener(monthlyRecap),
        )
    }

    /**
     * This method is used to retrieve the monthly recaps
     * @author Daniel Satriano
     * @since 7/08/2022
     * @param monthlyRecap list passed where it'll add all the recaps
     */
    private fun monthlyRecapListener(monthlyRecap: MutableList<MonthlyRecap>): ChildEventListener {
        return object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(MonthlyRecap::class.java)
                if(item != null){
                    monthlyRecap.add(item)
                    adapter.notifyItemInserted(monthlyRecap.indexOf(item))
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(MonthlyRecap::class.java)
                if(item != null){
                    val index = monthlyRecap.indexOf(monthlyRecap.find { it.month == item.month})
                    monthlyRecap[index] = item
                    adapter.notifyItemChanged(index)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val item = snapshot.getValue(MonthlyRecap::class.java)
                if(item != null){
                    val index = monthlyRecap.indexOf(item)
                    monthlyRecap.removeAt(index)
                    adapter.notifyItemRemoved(index)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),error.message,Toast.LENGTH_LONG).show()
            }
        }
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
        graph.setNoDataText(getString(R.string.graph_noDataText))
        graph.setNoDataTextTypeface(ResourcesCompat.getFont(requireContext(), R.font.roboto_regular))

        //region not working
        graph.setVisibleXRange(3f,6f)
        graph.setFitBars(true)
        //endregion


        val xAxis = graph.xAxis
        xAxis.granularity = 1F
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.setDrawGridLines(false)

    }

    /**
     * metodo per la selezione del mese (da sistemare)
     * @author Claudio Menegotto
     */
    fun showMonthSelectDialog(){
        val newFitnessCard = FitnessCard()
        // Create an alert builder
        val builder = MaterialAlertDialogBuilder(requireContext(),  R.style.ThemeOverlay_App_MaterialAlertDialog)
        // set the custom layout
        val customLayout: View = layoutInflater.inflate(R.layout.dialog_month_select, null)
        builder.setView(customLayout)

        val recycle = customLayout.findViewById<ListView>(R.id.monthRecycle)
        var months : MutableList<String> = ArrayList()
        months.add("gennaio")
        months.add("febbraio")
        months.add("marzo")
        months.add("aprile")
        months.add("maggio")
        months.add("giugno")
        months.add("luglio")
        months.add("agosto")
        months.add("settembre")
        months.add("ottobre")
        months.add("novembre")
        months.add("dicembre")

        recycle.adapter =  ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, months)

        recycle.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            recycle.smoothScrollToPosition(position)
        }

        // add a button
        builder.show()
    }



}