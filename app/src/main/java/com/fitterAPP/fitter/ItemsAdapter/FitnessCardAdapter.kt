package com.fitterAPP.fitter.ItemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.Classes.FitnessCard
import com.fitterAPP.fitter.R

class FitnessCardAdapter (val context2: Context, val Cards:ArrayList<FitnessCard>) : RecyclerView.Adapter<FitnessCardAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setCard(Card:FitnessCard, context: Context){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(context2).inflate(R.layout.item_fitnesscard, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var Card: FitnessCard = Cards.get(position)
        holder.setCard(Card, context2)
    }

    override fun getItemCount(): Int {
        return Cards.size
    }

}