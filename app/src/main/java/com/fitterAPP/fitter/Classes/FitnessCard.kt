package com.fitterAPP.fitter.Classes

import com.fitterAPP.fitter.Classes.Exercise


data class FitnessCard(var description : String, var TimeDuration : Int, var exercises : MutableList<Exercise>? ) {

    constructor() : this ("Default description",60, null)
}
