package com.fitterAPP.fitter.Classes

data class Exercise(var exerciseName : String, var exerciseRep : Int, var exerciseSer : Int, var exerciseRest : Double) {

    constructor() : this("Default name",1,1,30.0)

}