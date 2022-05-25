package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.fragmentControllers.MyFitnessCardsDirections

class FitnessCardAdapter (val context2: Context, val Cards:MutableList<FitnessCard>) : RecyclerView.Adapter<FitnessCardAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val CardName : TextView = itemView.findViewById(R.id.CardName_TV)
        val CardDuration : TextView = itemView.findViewById(R.id.TimeDuration_TV)
        val CardExercises : TextView = itemView.findViewById(R.id.ExerciseCount_TV)

        fun setCard(Card:FitnessCard){
            CardName.text = Card.name
            CardDuration.text = "Duration: " + Card.timeDuration.toString() + " min"
            if(Card.exercises != null)
                CardExercises.text = Card.exercises?.count().toString() + " exercise"
            else
                CardExercises.text = "0 exercise"

            itemView.setOnClickListener {
                val action : NavDirections = MyFitnessCardsDirections.actionMyFitnessCardsToFragmentShowCardDialog(Card)
                it.findNavController().navigate(action)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(context2).inflate(R.layout.item_fitnesscard, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val Card: FitnessCard = Cards[position]
        holder.setCard(Card)
    }

    override fun getItemCount(): Int {
        return Cards.size
    }

}