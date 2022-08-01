package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.DayRecap
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class MonthlyRecapAdapter(private val context2: Context, private val recapList: MutableList<DayRecap>, private val graph : BarChart) : RecyclerView.Adapter<MonthlyRecapAdapter.Holder>() {

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val cardLabel: TextView = itemView.findViewById(R.id.recapName_TV)
        private val description : TextView = itemView.findViewById(R.id.recapCount_TV)

        fun setCard(monthlyRecap: DayRecap, context: Context){

            description.visibility = View.GONE

            cardLabel.text = monthlyRecap.key
            //cardLabel.textSize = 30F

            itemView.setOnClickListener {
                updateGraph(monthlyRecap.key)
            }

            //TODO("Aggiunta immagini diverse per ogni stagione ?")
            /*
            val id: Int = context.resources.getIdentifier( "com.fitterAPP.fitter:drawable/" + "recapbg",null,null)
            bgImage.setImageResource(id)
            */
        }

    }

    private fun updateGraph(mese: String) {
        graph.setTouchEnabled(false)

        val a : MutableList<IBarDataSet> = ArrayList()

        val tmp1 : BarEntry = BarEntry(0F,30F)
        val tmp2 : BarEntry = BarEntry(1F,50F)
        val tmp3 : BarEntry = BarEntry(2F,80F)

        val gg1 : BarDataSet = BarDataSet(mutableListOf(tmp1), "Warmup")
        val gg2 : BarDataSet = BarDataSet(mutableListOf(tmp2), "ChestPress")
        val gg3 : BarDataSet = BarDataSet(mutableListOf(tmp3), "Boh")

        gg1.color = ColorTemplate.MATERIAL_COLORS[0]
        gg1.valueTextSize = 18F
        gg1.label = "Warmup"

        gg2.color = ColorTemplate.MATERIAL_COLORS[1]
        gg2.valueTextSize = 18F
        gg2.label = "ChestPress"

        gg3.color = ColorTemplate.MATERIAL_COLORS[2]
        gg3.valueTextSize = 18F
        gg3.label = "Boh"


        a.add(gg1)
        a.add(gg2)
        a.add(gg3)

/*
        barDataset.valueTextColor = Color.BLACK
        barDataset.valueTextSize = 16F
*/



        val barData = BarData(a)

        graph.xAxis.setDrawAxisLine(false)
        graph.xAxis.setDrawLabels(true)
        graph.xAxis.setDrawGridLines(false)
        graph.setFitBars(true)
        graph.data = barData
        graph.setFitBars(true)
        graph.description.text = mese
        graph.animateY(500)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyRecapAdapter.Holder {
        val view: View = LayoutInflater.from(context2).inflate(R.layout.item_monthly_recap, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: MonthlyRecapAdapter.Holder, position: Int) {
        val tmp: DayRecap = recapList[position]
        holder.setCard(tmp, context2)
    }

    override fun getItemCount(): Int {
        return recapList.size
    }

}