package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.*
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.fitterAPP.fitter.databases.StaticRecapDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FitnessCardExercisesAdapter (val context2: Context, val fitnessCard: FitnessCard, val isEditable : Boolean) : RecyclerView.Adapter<FitnessCardExercisesAdapter.Holder>() {

    var exerciseRecap : MutableList<ExerciseRecap> = mutableListOf()
    val currentDateAndTime : String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm:ss"))
    val dayRecap = DayRecap(currentDateAndTime, fitnessCard.key, exerciseRecap)

    private val databaseRef = StaticFitnessCardDatabase.database.getReference(context2.getString(R.string.FitnessCardsReference))

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val exName : TextView = itemView.findViewById(R.id.ExName_TV)
        val exReps : TextView = itemView.findViewById(R.id.ExReps_TV)
        val icon : ImageView = itemView.findViewById(R.id.exercise_icon_IV)

        fun setCard(ex:Exercise, context: Context){
            exName.text = ex.exerciseName

            when (ex.type){
                ExerciseType.warmup -> {
                    exReps.text = ex.exerciseDuration.toString() + " min"
                    icon.setImageResource(R.drawable.warmup_exercise_icon)
                }

                ExerciseType.normal -> {
                    exReps.text = ex.exerciseSer.toString() + " x " + ex.exerciseRep.toString() + " - " + ex.exerciseRest + " s"
                    icon.setImageResource(R.drawable.normal_exercise_icon)
                }
                ExerciseType.series -> {
                    exReps.text = ex.exerciseSeries?.size.toString() + " - " + ex.exerciseRest + " s"
                    icon.setImageResource(R.drawable.series_exercise_icon)
                }
                ExerciseType.piramid -> {
                    icon.setImageResource(R.drawable.pyramid_exercise_icon)
                    var reps = ""
                    for (i in 0 until ex.piramidSeries?.size!!) {
                        reps += ex.piramidSeries?.get(i)!!.toString()
                        if(i < ex.piramidSeries?.lastIndex!!)
                            reps += " x "
                    }

                    exReps.text = reps + " - " + ex.exerciseRest + " s"
                }

                else ->{
                    exReps.text = ex.exerciseSer.toString() + " x " + ex.exerciseRep.toString() + " - " + ex.exerciseRest + " s"
                    icon.setImageResource(R.drawable.normal_exercise_icon)
                }
            }
        }

    }

    /**
     * Used to delete an exercise
     * @author Daniel Satriano
     * @since 23/07/2022
     * @param index it's the index of the item that needs to be removed
     */
    fun deleteItem(index : Int){
        fitnessCard.exercises!!.removeAt(index)
        notifyItemRemoved(index)
        StaticFitnessCardDatabase.setFitnessCardItem(databaseRef,Athlete.UID,fitnessCard)
    }

    /**
     * Used to set a new recap
     * @author Daniel Satriano
     * @since 23/07/2022
     * @param index it's the index of the item that needs to be removed
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
        val view: View = if(!isEditable) {
                LayoutInflater.from(context2).inflate(R.layout.item_exercise, parent, false)
        }else{
                LayoutInflater.from(context2).inflate(R.layout.item_edit_exercise, parent, false)
        }
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val card: Exercise = fitnessCard.exercises!![position]
        holder.setCard(card, context2)
    }

    override fun getItemCount(): Int {
        return fitnessCard.exercises!!.size
    }

}