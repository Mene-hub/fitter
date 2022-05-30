package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.graphics.Interpolator
import android.icu.number.Scale
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateInterpolator
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat.animate
import androidx.core.view.isGone
import androidx.core.view.isVisible

import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.classes.Exercise
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.R

class FitnessCardExercisesAdapter (val context2: Context, val Card:FitnessCard, val exercises : MutableList<Exercise>, val isEditable : Boolean) : RecyclerView.Adapter<FitnessCardExercisesAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val ExName : TextView = itemView.findViewById(R.id.ExName_TV)
        val ExReps : TextView = itemView.findViewById(R.id.ExReps_TV)
        val openMenu : ImageView = itemView.findViewById(R.id.openMenu_IV)
        val exMenu : LinearLayout = itemView.findViewById(R.id.ExMenu_LL)


        fun setCard(ex:Exercise, context: Context){
            ExName.text = ex.exerciseName
            ExReps.text = ex.exerciseSer.toString() + " x " + ex.exerciseRep.toString()
            openMenu.setOnClickListener {
                exMenu.isGone = !exMenu.isGone
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
        if(!isEditable) {
            val view: View =
                LayoutInflater.from(context2).inflate(R.layout.item_exercise, parent, false)
            return Holder(view)
        }else{
            val view: View =
                LayoutInflater.from(context2).inflate(R.layout.item_edit_exercise, parent, false)
            return Holder(view)
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val Card: Exercise = exercises?.get(position)!!
        holder.setCard(Card, context2)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

}