package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.*
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.fitterAPP.fitter.databases.StaticRecapDatabase
import com.fitterAPP.fitter.fragmentControllers.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate

/**
 * Adapter for the Exercise RecycleView used for showind and edith exercises
 * @author Claudio Menegotto
 */
class FitnessCardExercisesAdapter (val context2: Context, val fitnessCard: FitnessCard, val isEditable : Boolean, var monthlyRecap : MonthlyRecap? = null, var showControls:Boolean) : RecyclerView.Adapter<FitnessCardExercisesAdapter.Holder>() {

    val database = StaticRecapDatabase.database.getReference(context2.getString(R.string.RecapReference))

    private val databaseRef = StaticFitnessCardDatabase.database.getReference(context2.getString(R.string.FitnessCardsReference))

    /**
     * Used to check at the very start of the adapter if a given recap for a given card is already been done today. calls valueListener()
     * @author Daniel Satriano
     * @since 30/07/2022
     */

    init {
        checkForNullOrCorrectYear()
    }

    class Holder(itemView: View, edit: Boolean, fitnessCard: FitnessCard, showControls:Boolean) : RecyclerView.ViewHolder(itemView) {

        val exName : TextView = itemView.findViewById(R.id.ExName_TV)
        val exReps : TextView = itemView.findViewById(R.id.ExReps_TV)
        val icon : ImageView = itemView.findViewById(R.id.exercise_icon_IV)
        val edit_ : Boolean = edit
        var fitnessCard_ = fitnessCard
        var showControls_ = showControls


        fun setCard(ex:Exercise, index : Int, context2: Context){
            exName.text = ex.exerciseName

            if(edit_){
                itemView.findViewById<ImageView>(R.id.editExercise_IV).setOnClickListener {
                    val controller = itemView.findFragment<ModifyCard>().findNavController()

                    val action : NavDirections = when(ex.type){
                        ExerciseType.warmup-> ModifyCardDirections.actionModifyCardToSetWarmUpExercise(fitnessCard_, index,ex)
                        ExerciseType.normal-> ModifyCardDirections.actionModifyCardToNewExercieFormDialog(fitnessCard_, index, ex)
                        ExerciseType.pyramid-> ModifyCardDirections.actionModifyCardToSetPiramidalExercise(fitnessCard_, ex, index)
                    }
                    controller.navigate(action)

                }
            }else
            {
                itemView.findViewById<LinearLayout>(R.id.propertys_LL).isGone = !showControls_

                itemView.findViewById<LinearLayout>(R.id.descriptionItem).setOnClickListener {
                    showExerciseinformation(ex)
                }

                if(ex.type == ExerciseType.warmup || ex.exerciseRest == 0)
                    itemView.findViewById<ImageView>(R.id.exerciseTimer).isGone = true
                else
                    itemView.findViewById<ImageView>(R.id.exerciseTimer).setOnClickListener {
                        //visualiizzazione del timer
                        try {
                            itemView.findFragment<Fragment_showCardDialog>().showTimer(ex)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                itemView.findViewById<ImageView>(R.id.notesIV).setOnClickListener {
                    //visualiizzazione delle notes
                    try {
                        itemView.findFragment<Fragment_showCardDialog>().showNotes(index)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }

            when (ex.type){
                ExerciseType.warmup -> {
                    exReps.text = ex.exerciseDuration.toString().plus(" min")
                    icon.setImageResource(R.drawable.warmup_exercise_icon)
                }

                ExerciseType.normal -> {
                    exReps.text = ex.exerciseSer.toString().plus(" x " + ex.exerciseRep.toString() + " - " + ex.exerciseRest + " s")
                    icon.setImageResource(R.drawable.normal_exercise_icon)
                }

                ExerciseType.pyramid -> {
                    icon.setImageResource(R.drawable.pyramid_exercise_icon)
                    var reps = ""
                    for (i in 0 until ex.piramidSeries?.size!!) {
                        reps += ex.piramidSeries?.get(i)!!.toString()
                        if(i < ex.piramidSeries?.lastIndex!!)
                            reps += " x "
                    }
                    exReps.text = reps.plus(" - " + ex.exerciseRest + " s")
                }
            }
        }

        fun showExerciseinformation(exercise: Exercise) {
            var found: Boolean = false
            if (exercise.wgerBaseId != null) {

                try {
                    itemView.findFragment<Fragment_showCardDialog>()
                        .showExerciseinformation(exercise)
                    found = true
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                try {
                    if (found == false) {
                        itemView.findFragment<ShowOthersCardDialog>()
                            .showExerciseinformation(exercise)
                        found = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
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
    fun deleteItem(index : Int, recyclerView : RecyclerView){
        val deletedItem = fitnessCard.exercises!![index]
        fitnessCard.exercises!!.removeAt(index)
        notifyItemRemoved(index)
        Snackbar.make(recyclerView,deletedItem.exerciseName,Snackbar.LENGTH_LONG).setAnchorView(R.id.newExercise_BT).setAction("Undo") {
            fitnessCard.exercises!!.add(index, deletedItem)
            notifyItemInserted(index)
            StaticFitnessCardDatabase.setFitnessCardItem(databaseRef,Athlete.UID,fitnessCard)
        }.show()
        StaticFitnessCardDatabase.setFitnessCardItem(databaseRef,Athlete.UID,fitnessCard)
    }


    /**
     * Used to set a new recap
     * @author Daniel Satriano
     * @since 23/07/2022
     * @param index it's the index of the item that needs to be removed
     * @param improvement Is the value given by the user that holds the weight or the minutes used/done for an exercise. EG : Today I lifted 50Kg , Today I run 15 minutes
     */
    fun addRecap(index : Int, improvement : Int){

        val exercise = fitnessCard.exercises!![index]

        //checks if the monthly recap is not empty
        if(monthlyRecap!!.recapExercise.isNotEmpty()) {

            //trying to find the swiped exercise inside the recap list
            val index2 = monthlyRecap!!.recapExercise.indexOf(
                monthlyRecap!!.recapExercise.find { it.exerciseName == exercise.exerciseName }
            )

            //if its not found then it means its a new recap record, which will be added normally. If its found instead it makes the avarage between the user value and the monthly value
            if (index2 == -1) {
                monthlyRecap!!.recapExercise.add(ExerciseRecap(exercise.exerciseName, improvement))
            } else {
                //Execute the average between the current value for that exercise and the new one
                monthlyRecap!!.recapExercise[index2].improvement =
                    (monthlyRecap!!.recapExercise[index2].improvement + improvement) / 2
            }

        }else{
            //if the monthly recap its empty it just add the exercise skipping all the code above, since its guaranteed to be a new record
            monthlyRecap!!.recapExercise.add(ExerciseRecap(exercise.exerciseName, improvement))
        }

        //update the database
        StaticRecapDatabase.setRecapItem(database, Athlete.UID, monthlyRecap!!)

    }

    /**
     * This logic method is simply creating a new monthly recap whenever the given one from [Fragment_showCardDialog] is null or the year doesn't match the current one
     * @author Daniel Satriano
     * @since 1/08/2022
     */
    private fun checkForNullOrCorrectYear(){
        if(monthlyRecap == null || monthlyRecap!!.year != LocalDate.now().year){
            monthlyRecap = MonthlyRecap(LocalDate.now().month.toString(), fitnessCard.key, mutableListOf())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = if(!isEditable) {
            LayoutInflater.from(context2).inflate(R.layout.item_exercise, parent, false)
        }else{
            LayoutInflater.from(context2).inflate(R.layout.item_edit_exercise, parent, false)
        }
        return Holder(view, isEditable, fitnessCard, showControls)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val ex: Exercise = fitnessCard.exercises!![position]
        holder.setCard(ex, position, context2)
    }

    override fun getItemCount(): Int {
        return if(fitnessCard.exercises == null){
            0
        }else{
            fitnessCard.exercises!!.size
        }

    }

}