package com.fitterAPP.fitter.ItemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.Classes.FitnessCard
import com.fitterAPP.fitter.FragmentControlers.Fragment_showCardDialog
import com.fitterAPP.fitter.R

class FitnessCardAdapter (val context2: Context, val Cards:MutableList<FitnessCard>) : RecyclerView.Adapter<FitnessCardAdapter.Holder>() {

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnLongClickListener{

        val CardName : TextView = itemView.findViewById(R.id.CardName_TV)
        val CardDuration : TextView = itemView.findViewById(R.id.TimeDuration_TV)
        val CardExercises : TextView = itemView.findViewById(R.id.ExerciseCount_TV)

        init {
            itemView.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View?): Boolean {
            v?.startAnimation(AnimationUtils.loadAnimation(context2, R.anim.wibble_animation))
            return true
        }

        fun setCard(Card:FitnessCard, context: Context){
            CardName.text = Card.name
            CardDuration.text = "Duration: " + Card.timeDuration.toString() + " min"
            if(Card.exercises != null)
                CardExercises.text = Card.exercises?.count().toString() + " exercise"
            else
                CardExercises.text = "0 exercise"

            itemView.setOnClickListener {
                transaction(context, Card)
            }

        }

        private fun transaction(context2:Context, card : FitnessCard) {

            val fragmentManager = (context2 as AppCompatActivity).supportFragmentManager
            val newFragment = Fragment_showCardDialog(card)

            // The device is smaller, so show the fragment fullscreen
            val transaction = fragmentManager.beginTransaction()
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction
                .replace(android.R.id.content, newFragment)
                .addToBackStack(null)
                .commit()

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(context2).inflate(R.layout.item_fitnesscard, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val Card: FitnessCard = Cards[position]
        holder.setCard(Card, context2)
    }

    override fun getItemCount(): Int {
        return Cards.size
    }

}