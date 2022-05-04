package com.fitterAPP.fitter

import java.time.Duration

data class Exercise(var exerciseName : String, var exerciseRep : Int, var exerciseRest : Double) {

    constructor() : this("Default name",1,1.0)

}
