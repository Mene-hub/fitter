package com.fitterAPP.fitter.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *
 * @param exerciseName name of the exercise
 * @param exerciseRep number of reps
 * @param exerciseSer number of sets (series)
 * @param exerciseRest time of rest between sets
 * @param images list of images for the exercise
 * @author Menegotto Claudio
 */
@Parcelize
data class Exercise(var exerciseName : String, var type : ExerciseType) : Parcelable {

    //warmup
    var exerciseDuration : Int? = null

    //normal exercise
    var exerciseRep : Int? = null
    var exerciseSer : Int? = null
    var exerciseRest : Int? = null

    //piramidal exercise
    var piramidSeries : MutableList<Int>? = null

    var images : List<String?>? = null
    var notes : String ?= null
    var description : String ?= null
    var exerciseId : Int ? = null
    var wgerId : Int ? = null
    var wgerBaseId : Int ? = null

    //default
    constructor() : this("Default name", ExerciseType.normal)

    fun setAsWarmup(duration: Int){

        type = ExerciseType.warmup
        exerciseDuration = duration
        exerciseRep = null
        exerciseSer = null
        exerciseRest = null
        piramidSeries = null

    }

    fun setAsNormal(rep: Int, ser : Int, rest : Int){

        type = ExerciseType.normal
        exerciseDuration = null
        exerciseRep = rep
        exerciseSer = ser
        exerciseRest = rest
        piramidSeries = null

    }

    fun setAsPiramid(ser : Int, rest : Int){

        type = ExerciseType.pyramid
        exerciseDuration = null
        exerciseRep = null
        exerciseSer = ser
        exerciseRest = rest
        piramidSeries = null
    }

}