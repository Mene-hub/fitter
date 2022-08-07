package com.fitterAPP.fitter.classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

/**
 * Classed used to save a recap
 *
 * @author Daniel Satriano
 * @since 23/07/2022
 * @param month It's the month converted in a string format, this will be used as the node key.
 * @param cardKey It's the key of the node of the fitnessCard needed for the recap.
 * @param month Month of the recap
 * @param recapExercise mutable list containing many object of  [ExerciseRecap]
 */
@Parcelize
data class MonthlyRecap (var month : String, var cardKey : String, var recapExercise : MutableList<ExerciseRecap>, val year : Int = LocalDate.now().year  ) : Parcelable{
    constructor(): this( "" , "", mutableListOf())
}

/**
 * Add-on class used in [MonthlyRecap]
 *
 * @author Daniel Satriano
 * @since 23/07/2022
 * @param improvement Contains the improvement set by the user in terms of weight or cardio minutes (warmup).
 * @param exerciseName The name of the exercise, retrievable in [Exercise] class.
 */
@Parcelize
data class ExerciseRecap(var exerciseName : String, var improvement : Int) : Parcelable{
    constructor() : this("",0)
}