package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.MonthlyRecap
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.Month

/**
 * An adapter for the recycler view inside [com.fitterAPP.fitter.fragmentControllers.MonthlyRecapChart] layout
 * @author Daniel Satriano
 * @since 31/07/2022
 * @param context2 context
 * @param recapList a list which contains all the monthly recap
 * @param graph the BarChart defined in the layout
 */
class MonthlyRecapAdapter(private val context2: Context, private val recapList: MutableList<MonthlyRecap>, private val graph : BarChart) : RecyclerView.Adapter<MonthlyRecapAdapter.Holder>() {

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val cardLabel: TextView = itemView.findViewById(R.id.recapName_TV)

        fun setCard(monthlyRecap: MonthlyRecap){

            cardLabel.text = monthlyRecap.month
            //cardLabel.textSize = 30F

            itemView.setOnClickListener {
                updateGraph(monthlyRecap)
            }

            //TODO("Aggiunta immagini diverse per ogni stagione ?")
            /*
            val id: Int = context.resources.getIdentifier( "com.fitterAPP.fitter:drawable/" + "recapbg",null,null)
            bgImage.setImageResource(id)
            */
        }

    }


    /**
     * Used in [com.fitterAPP.fitter.fragmentControllers.MonthlyRecapChart] to set the default chart to display
     * @author Daniel Satriano
     * @since 13/08/2022
     */
    fun setFirstDisplayedItem(recap : MonthlyRecap){
        updateGraph(recap)
    }


    /**
     * Method called when a user clicks on a cardview inside the recycler view. This method will generate all the necessary data to populate the graph
     * @author Daniel Satriano
     * @since 1/08/2022
     * @param recap recap of the month clicked in the recycler view
     */
    private fun updateGraph(recap: MonthlyRecap) {
        val month : String = recap.month

        val xLabels : ArrayList<String> = ArrayList()
        val dataSets : MutableList<IBarDataSet> = ArrayList()

        recap.recapExercise.forEachIndexed { index, exerciseRecap ->
            val tmp = BarEntry(index.toFloat(), exerciseRecap.improvement.toFloat())    //creates a BarEntry
            val datasetTMP = BarDataSet(mutableListOf(tmp), exerciseRecap.exerciseName) //creates the dataSet
            datasetTMP.valueTextSize = 18f
            datasetTMP.color = ColorTemplate.MATERIAL_COLORS[index%4] //Set a different color for each bar its % 4 because in the int array there are only 4 items, otherwise it would throw an index exception
            dataSets.add(datasetTMP)

            xLabels.add(exerciseRecap.exerciseName) //Adds the corrisponding label for the bar
        }

        val barData = BarData(dataSets)
        barData.barWidth = 0.9f

        graph.data = barData

        graph.description.text = month
        graph.xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)
        graph.xAxis.labelCount = recap.recapExercise.size


        graph.invalidate()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyRecapAdapter.Holder {
        val view: View = LayoutInflater.from(context2).inflate(R.layout.item_monthly_recap, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: MonthlyRecapAdapter.Holder, position: Int) {
        val tmp: MonthlyRecap = recapList[position]
        holder.setCard(tmp)
    }

    override fun getItemCount(): Int {
        return recapList.size
    }

}