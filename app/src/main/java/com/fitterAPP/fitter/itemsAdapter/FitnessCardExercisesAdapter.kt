package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.findFragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.*
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.fitterAPP.fitter.databases.StaticRecapDatabase
import com.fitterAPP.fitter.fragmentControllers.Fragment_showCardDialog
import com.fitterAPP.fitter.fragmentControllers.ModifyCard
import com.fitterAPP.fitter.fragmentControllers.ModifyCardDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Adapter for the Exercise RecycleView used for showind and edith exercises
 * @author Claudio Menegotto
 */
class FitnessCardExercisesAdapter (val context2: Context, val fitnessCard: FitnessCard, val isEditable : Boolean) : RecyclerView.Adapter<FitnessCardExercisesAdapter.Holder>() {

    var exerciseRecap : MutableList<ExerciseRecap> = mutableListOf()
    private val currentDateAndTime : String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-M-yyyy"))
    private val dayRecap = DayRecap(currentDateAndTime, fitnessCard.key, exerciseRecap)
    val database = StaticRecapDatabase.database.getReference(context2.getString(R.string.RecapReference))
    var recapDoneAlready : Boolean = false

    private val databaseRef = StaticFitnessCardDatabase.database.getReference(context2.getString(R.string.FitnessCardsReference))

    /**
     * Used to check at the very start of the adapter if a given recap for a given card is already been done today. calls valueListener()
     * @author Daniel Satriano
     * @since 30/07/2022
     */
    init {
        StaticRecapDatabase.setSingleListenerToCardRecap(databaseRef = database, Athlete.UID, dayRecap, valueListener())
    }


    class Holder(itemView: View, edit: Boolean, fitnessCard: FitnessCard) : RecyclerView.ViewHolder(itemView) {

        val exName : TextView = itemView.findViewById(R.id.ExName_TV)
        val exReps : TextView = itemView.findViewById(R.id.ExReps_TV)
        val icon : ImageView = itemView.findViewById(R.id.exercise_icon_IV)
        val edit_ : Boolean = edit
        var fitnessCard_ = fitnessCard


        fun setCard(ex:Exercise, context: Context, index : Int){
            exName.text = ex.exerciseName

            if(edit_){
                itemView.findViewById<ImageView>(R.id.edithExercise_IV).setOnClickListener {
                    val controller = itemView.findFragment<ModifyCard>().findNavController()
                    var action : NavDirections? = null

                    when(ex.type){

                        ExerciseType.warmup-> action =  ModifyCardDirections.actionModifyCardToSetWarmUpExercise(fitnessCard_, index, fitnessCard_.exercises?.get(index)!!)
                        ExerciseType.normal-> action =  ModifyCardDirections.actionModifyCardToNewExercieFormDialog(fitnessCard_, index, fitnessCard_.exercises?.get(index)!!)
                        ExerciseType.series-> action = null
                        ExerciseType.pyramid-> action = null
                        ExerciseType.seriesItem-> action = null
                    }

                    if(action != null)
                        controller.navigate(action!!)

                }
            }else
            {
                itemView.setOnClickListener {
                    showExerciseinformation(ex)
                }
            }

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
                ExerciseType.pyramid -> {
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

        fun showExerciseinformation(exercise: Exercise){
            if(exercise.wgerBaseId != null)
                itemView.findFragment<Fragment_showCardDialog>().showExerciseinformation(exercise)
        }

    }



    /**
     * Used to delete an exercise
     * @author Daniel Satriano
     * @since 23/07/2022
     * @param index it's the index of the item that needs to be removed
     */
    //TODO("Should it also delete each recap in that month ?")
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
        if(!recapDoneAlready) {
            val exercise = fitnessCard.exercises!![index]
            exerciseRecap.add(ExerciseRecap(exercise.exerciseId!!, improvement))
            StaticRecapDatabase.setRecapItem(database, Athlete.UID, dayRecap)
        }
    }

    /**
     * @author Daniel Satriano
     * @since 30/07/2022
     * Checks if the recap is already been done today, if it is then it sets [recapDoneAlready] to true. Note that this listener is not a normal ValueListener, but its a
     * ListenerForSingleValueEvent
     */
    private fun valueListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                   recapDoneAlready = true
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context2, error.message,Toast.LENGTH_LONG)
            }
        }
    }


    /**
     * Used to check if the recap that the user is trying to add is already added to the database for that given instance.
     * this function is being used in Fragment_showCardDialog.kt
     * @author Daniel Satriano
     * @param index it's the index of the item that needs to be checked
     * @return a boolean which defines whether it exists. If true it does exist, if false it doesn't
     */
    fun recapChecker(index : Int) : Boolean{
        val exercise = fitnessCard.exercises!![index]

        var alreadySet = false
        for (item in exerciseRecap) {
            if (item.uidExercise == exercise.exerciseId) {
                alreadySet = true
            }
        }
        return alreadySet
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = if(!isEditable) {
                LayoutInflater.from(context2).inflate(R.layout.item_exercise, parent, false)
        }else{
                LayoutInflater.from(context2).inflate(R.layout.item_edit_exercise, parent, false)
        }
        return Holder(view, isEditable, fitnessCard)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val ex: Exercise = fitnessCard.exercises!![position]
        holder.setCard(ex, context2, position)
    }

    override fun getItemCount(): Int {
        return if(fitnessCard.exercises == null){
            0
        }else{
            fitnessCard.exercises!!.size
        }

    }

}