package com.fitterAPP.fitter.classes

/**
 * Classed used to save a recap
 *
 * @author Daniel Satriano
 * @since 23/07/2022
 * @param key Its the date and time converted in a string format, this will be used as the node key.
 * @param cardKey Its the key of the node of the fitnessCard needed for the recap.
 * @param recapExercise mutable list containing many object of  [ExerciseRecap]
 */
data class DayRecap (var key : String, var cardKey : String, var recapExercise : MutableList<ExerciseRecap>  ){
    constructor(): this("", "", mutableListOf())
}

/**
 * Add-on class used in [DayRecap]
 *
 * @author Daniel Satriano
 * @since 23/07/2022
 * @param improvement Contains the improvement set by the user in terms of weight or cardio minutes (warmup).
 * @param uidExercise UID of the exercise, retrievable in [Exercise] class.
 */
data class ExerciseRecap(var uidExercise : Int, var improvement : Int){
    constructor() : this(0,0)
}