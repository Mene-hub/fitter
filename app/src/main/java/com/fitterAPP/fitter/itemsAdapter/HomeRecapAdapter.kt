package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.fragmentControllers.MyFitnessCardsDirections

/**
 *
 * @author Daniel Satriano
 * @property context2 context
 * @property Cards mutable list of cards
 */
class HomeRecapAdapter(private val context2: Context, private val Cards:MutableList<FitnessCard>) : RecyclerView.Adapter<HomeRecapAdapter.Holder>() {

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val cardName : TextView = itemView.findViewById(R.id.recapName_TV)

        fun setCard(Card:FitnessCard){
            cardName.text = Card.name

            itemView.setOnClickListener {
                val action : NavDirections = MyFitnessCardsDirections.actionMyFitnessCardsToMonthlyRecapChart(fitnessCard = Card)
                it.findNavController().navigate(action)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecapAdapter.Holder {
        val view: View = LayoutInflater.from(context2).inflate(R.layout.item_monthly_recap, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: HomeRecapAdapter.Holder, position: Int) {
        val Card: FitnessCard = Cards[position]
        holder.setCard(Card)
    }

    override fun getItemCount(): Int {
        return Cards.size
    }

}