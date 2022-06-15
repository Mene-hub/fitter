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
data class Exercise(var exerciseName : String, var images : List<String?>) {

    constructor() : this("Default name", images = listOf())

}
