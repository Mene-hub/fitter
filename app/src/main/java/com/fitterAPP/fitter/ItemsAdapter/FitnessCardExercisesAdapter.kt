package com.fitterAPP.fitter.ItemsAdapter

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.core.view.isVisible

import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.Classes.Exercise
import com.fitterAPP.fitter.Classes.FitnessCard
import com.fitterAPP.fitter.R

class FitnessCardExercisesAdapter (val context2: Context, val Card:FitnessCard, val exercises : MutableList<Exercise>) : RecyclerView.Adapter<FitnessCardExercisesAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val ExName : TextView = itemView.findViewById(R.id.ExName_TV)
        val ExReps : TextView = itemView.findViewById(R.id.ExReps_TV)
        val TimeIndictor : CardView = itemView.findViewById(R.id.TimeIndicator_CV)


        fun setCard(ex:Exercise, context: Context){
            ExName.text = ex.exerciseName
            ExReps.text = ex.exerciseSer.toString() + " x " + ex.exerciseRep.toString()
            TimeIndictor.isGone = true

            itemView.setOnClickListener {
                if( TimeIndictor.isGone) {

                    val params = FrameLayout.LayoutParams(
                        itemView.width,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                    )
                    TimeIndictor.layoutParams = params

                    TimeIndictor.isGone = false
                    startTimer(ex, context)
                }else
                    TimeIndictor.isGone = true
            }

        }

        fun startTimer(ex : Exercise, context: Context){
            var count : Int = 1 //itemView.width/ex.exerciseRep.toInt()
            var countDownTimer = object : CountDownTimer(((ex.exerciseRest*1000).toLong()),100){
                //end of timer
                override fun onFinish() {
                    TimeIndictor.isGone = true;
                    TimeIndictor.isVisible = false;
                }

                override fun onTick(millisUntilFinished: Long) {
                    val params = FrameLayout.LayoutParams(
                        TimeIndictor.width - count,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                    )
                    TimeIndictor.layoutParams = params
                }

            }.start()
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