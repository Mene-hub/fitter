package com.fitterAPP.fitter.Classes

import com.fitterAPP.fitter.Classes.Exercise


data class FitnessCard(val name:String , var description : String, var timeDuration : Int, var exercises : MutableList<Exercise>? ) {

    constructor() : this ("Scheda", "Default description Default description Default description Default description",60, null)
}
