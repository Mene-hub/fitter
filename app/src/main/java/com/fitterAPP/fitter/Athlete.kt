package com.fitterAPP.fitter

data class Athlete(var firstName : String, var lastName : String, var schede : MutableList<FitnessCard>?, var isInstructor : Boolean = false){

    constructor() : this("", "", null, false)
}
