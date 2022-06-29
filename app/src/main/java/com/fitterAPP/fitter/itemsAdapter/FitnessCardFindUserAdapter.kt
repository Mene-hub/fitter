package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.fragmentControllers.MyFitnessCardsDirections

class FitnessCardFindUserAdapter (private val context2: Context, private val Cards:MutableList<FitnessCard>) : RecyclerView.Adapter<FitnessCardFindUserAdapter.Holder>() {

    //TODO("Settare il long click listener che ti mostra un + per salvare la card")

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val cardName : TextView = itemView.findViewById(R.id.CardName_TV)
        private val cardDuration : TextView = itemView.findViewById(R.id.TimeDuration_TV)
        private val cardExercises : TextView = itemView.findViewById(R.id.ExerciseCount_TV)
        private val bgImage : ImageView = itemView.findViewById(R.id.CardBgImage_IV)

        fun setCard(card : FitnessCard, context: Context){
            cardName.text = card.name
            cardDuration.text = "Duration: " + card.timeDuration.toString() + " min"


            if(card.exercises != null)
                cardExercises.text = card.exercises?.count().toString() + " exercise"
            else {
                cardExercises.text = "0 exercise"
            }



            itemView.setOnClickListener {
                val action : NavDirections = MyFitnessCardsDirections.actionMyFitnessCardsToFragmentShowCardDialog(card)
                findNavController().navigate(action)
            }

            val id: Int = context.resources.getIdentifier(
                "com.fitterAPP.fitter:drawable/" + card.imageCover.toString(),
                null,
                null
            )

            bgImage.setImageResource(id)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(context2).inflate(R.layout.item_fitnesscard, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setCard(Cards[position], context2)
    }

    override fun getItemCount(): Int {
        return Cards.size
    }

}


