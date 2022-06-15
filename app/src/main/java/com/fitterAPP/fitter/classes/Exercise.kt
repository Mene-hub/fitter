package com.fitterAPP.fitter.classes

/**
 *
 * @param exerciseName name of the exercise
 * @param exerciseRep number of reps
 * @param exerciseSer number of sets (series)
 * @param exerciseRest time of rest between sets
 * @param images list of images for the exercise
 * @author Menegotto Claudio
 */
data class Exercise(var exerciseName : String, var type : ExerciseType) {

    var exerciseDuration : Int? = null
    var exerciseRep : Int? = null
    var exerciseSer : Int? = null
    var exerciseRest : Double? = null
    var images : List<String?>? = null
    var piramidSeries : MutableList<Int>? = null
    var exerciseSeries : MutableList<Exercise>? = null

    var notes : String ?= null

    //default
    constructor() : this("Default name", ExerciseType.normal)

}