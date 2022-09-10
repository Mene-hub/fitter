package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Exercise

class PiramidalAdapter(var series : MutableList<Int>, var exercise: Exercise, val context2:Context): RecyclerView.Adapter<PiramidalAdapter.Holder>(){

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val value : TextView = itemView.findViewById(R.id.Series_TV)
        val add : CardView = itemView.findViewById(R.id.addSeries_CV)
        val sub : CardView = itemView.findViewById(R.id.subSeries_CV)

        fun setReps(reps:Int, context2: Context, position : Int){
            value.text = reps.toString()
            add.setOnClickListener {
                exercise.piramidSeries!![position] ++
                value.text = exercise.piramidSeries!![position].toString()
            }

            sub.setOnClickListener {
                if(exercise.piramidSeries!![position] > 1){
                    exercise.piramidSeries!![position] --
                    value.text = exercise.piramidSeries!![position].toString()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PiramidalAdapter.Holder {
        val view: View = LayoutInflater.from(context2).inflate(R.layout.item_pyramid_repetition, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: PiramidalAdapter.Holder, position: Int) {
        val reps: Int = series[position]
        holder.setReps(reps, context2, position)
    }

    override fun getItemCount(): Int {
        return series.size
    }
}