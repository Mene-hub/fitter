package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.FitnessCard

class RecapAdapter(private val context2: Context, private val Cards:MutableList<FitnessCard>) : RecyclerView.Adapter<RecapAdapter.Holder>() {


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val cardName : TextView = itemView.findViewById(R.id.CardName_TV)
        private val cardExercises : TextView = itemView.findViewById(R.id.ExerciseCount_TV)
        private val bgImage : ImageView = itemView.findViewById(R.id.CardBgImage_IV)

        fun setCard(Card:FitnessCard, context: Context){
            cardName.text = Card.name

            cardExercises.text = "Ci sono 0 recap"
            cardExercises.setTextColor(Color.BLACK)

            cardName.setTextColor(Color.BLACK)


            itemView.setOnClickListener {
                //val action : NavDirections = MyFitnessCardsDirections.actionMyFitnessCardsToFragmentShowCardDialog(Card)
               //it.findNavController().navigate(action)
            }

            val id: Int = context.resources.getIdentifier( "com.fitterAPP.fitter:drawable/" + "recap",null,null)
            bgImage.setImageResource(id)

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecapAdapter.Holder {
        val view: View = LayoutInflater.from(context2).inflate(R.layout.item_fitnesscard, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: RecapAdapter.Holder, position: Int) {
        val Card: FitnessCard = Cards[position]
        holder.setCard(Card, context2)
    }

    override fun getItemCount(): Int {
        return Cards.size
    }

}