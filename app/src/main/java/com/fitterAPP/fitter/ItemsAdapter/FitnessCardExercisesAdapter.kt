package com.fitterAPP.fitter.ItemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.Classes.Exercise
import com.fitterAPP.fitter.Classes.FitnessCard
import com.fitterAPP.fitter.FragmentControlers.Fragment_createCardDialog
import com.fitterAPP.fitter.R

class FitnessCardExercisesAdapter (val context2: Context, val Card:FitnessCard, val exercises : MutableList<Exercise>) : RecyclerView.Adapter<FitnessCardExercisesAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val CardName : TextView = itemView.findViewById(R.id.CardName_TV)
        val CardDescription : TextView = itemView.findViewById(R.id.Description_TV)
        val CardDuration : TextView = itemView.findViewById(R.id.TimeDuration_TV)
        val CardExercises : TextView = itemView.findViewById(R.id.ExerciseCount_TV)

        fun setCard(ex:Exercise, context: Context){


            itemView.setOnClickListener {
                //transaction(context, ex)
            }

        }

        /*
        private fun transaction(context2:Context, ex : Exercise) {

            val fragmentManager = (context2 as AppCompatActivity).supportFragmentManager
            val newFragment = Fragment_createCardDialog(ex)

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
         */
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(context2).inflate(R.layout.item_exercise, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val Card: Exercise = exercises?.get(position)!!
        holder.setCard(Card, context2)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

}