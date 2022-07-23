package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.isGone

import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.*
import com.fitterAPP.fitter.databases.StaticRecapDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FitnessCardExercisesAdapter (val context2: Context, val fitnessCard: FitnessCard,  val exercises : MutableList<Exercise>, val isEditable : Boolean) : RecyclerView.Adapter<FitnessCardExercisesAdapter.Holder>() {

    var exerciseRecap : MutableList<ExerciseRecap> = mutableListOf()
    val currentDateAndTime : String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm:ss"))
    val dayRecap = DayRecap(currentDateAndTime, fitnessCard.key, exerciseRecap)

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ExName : TextView = itemView.findViewById(R.id.ExName_TV)
        val ExReps : TextView = itemView.findViewById(R.id.ExReps_TV)
        val icon : ImageView = itemView.findViewById(R.id.exercise_icon_IV)
        val cardView : CardView = itemView.findViewById(R.id.checkedBackground)

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
    //TODO("Apertura form per il recap e cambio color")
    fun addRecap(index : Int){
        val database = StaticRecapDatabase.database.getReference(context2.getString(R.string.RecapReference))

        exerciseRecap.add(ExerciseRecap(0,80))
        exerciseRecap.add(ExerciseRecap(0,100))
        exerciseRecap.add(ExerciseRecap(0,2000))

        StaticRecapDatabase.setRecapItem(database, Athlete.UID, dayRecap)


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
        val Card: Exercise = exercises[position]
        holder.setCard(Card, context2)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

}