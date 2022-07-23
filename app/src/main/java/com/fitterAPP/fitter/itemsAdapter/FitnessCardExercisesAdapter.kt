package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone

import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.classes.Exercise
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.ExerciseType

class FitnessCardExercisesAdapter (val context2: Context,  val exercises : MutableList<Exercise>, val isEditable : Boolean) : RecyclerView.Adapter<FitnessCardExercisesAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val ExName : TextView = itemView.findViewById(R.id.ExName_TV)
        val ExReps : TextView = itemView.findViewById(R.id.ExReps_TV)
        val icon : ImageView = itemView.findViewById(R.id.exercise_icon_IV)


        fun setCard(ex:Exercise, context: Context){
            ExName.text = ex.exerciseName

            when (ex.type){
                ExerciseType.warmup -> {
                    ExReps.text = ex.exerciseDuration.toString() + " min"
                    icon.setImageResource(R.drawable.warmup_exercise_icon)
                }

                ExerciseType.normal -> {
                    ExReps.text = ex.exerciseSer.toString() + " x " + ex.exerciseRep.toString() + " - " + ex.exerciseRest + " s"
                    icon.setImageResource(R.drawable.normal_exercise_icon)
                }
                ExerciseType.series -> {
                    ExReps.text = ex.exerciseSeries?.size.toString() + " - " + ex.exerciseRest + " s"
                    icon.setImageResource(R.drawable.series_exercise_icon)
                }
                ExerciseType.piramid -> {
                    icon.setImageResource(R.drawable.pyramid_exercise_icon)
                    var reps : String = ""
                    for (i in 0 until ex.piramidSeries?.size!!) {
                        reps += ex.piramidSeries?.get(i)!!.toString()
                        if(i < ex.piramidSeries?.lastIndex!!)
                            reps += " x "
                    }

                    ExReps.text = reps + " - " + ex.exerciseRest + " s"
                }

                else ->{
                    ExReps.text = ex.exerciseSer.toString() + " x " + ex.exerciseRep.toString() + " - " + ex.exerciseRest + " s"
                    icon.setImageResource(R.drawable.normal_exercise_icon)
                }
            }
        }
    }

    /**
     * Used to delete an exercise
     * @author Daniel Satriano
     * @since 23/07/2022
     * @param index its the index of the item that needs to be removed
     */
    fun deleteItem(index : Int){
        exercises.removeAt(index)
        notifyItemRemoved(index)
    }

    /**
     * Used to set a new recap
     * @author Daniel Satriano
     * @since 23/07/2022
     * @param index its the index of the item that needs to be removed
     */
    fun addRecap(index : Int){
        //TODO("Aggiungere recap")
        //TODO("Cambiare il colore della CardView in verde")
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