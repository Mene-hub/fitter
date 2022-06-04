package com.fitterAPP.fitter.classes

/**
 *
 * @param exerciseName name of the exercise
 * @param exerciseRep number of reps
 * @param exerciseSer number of sets (series)
 * @param exerciseRest time of rest between sets
 * @param images list of images for the exercise
 * @author Daniel Satriano
 */
data class Exercise(var exerciseName : String, var exerciseRep : Int, var exerciseSer : Int, var exerciseRest : Double, var images : List<String?>) {

    constructor() : this("Default name",1,1,60.0, images = listOf())

}
