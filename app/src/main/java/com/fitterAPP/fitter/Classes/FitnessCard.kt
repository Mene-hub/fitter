package com.fitterAPP.fitter.Classes

import java.text.SimpleDateFormat
import java.util.*


data class FitnessCard(var name:String?, var description: String?, var timeDuration: Int?, var exercises: MutableList<Exercise>?, val key: String = SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(Date())) {

    constructor() : this ("Scheda", "Default description",60, null)

    override fun toString() : String{
        return description.toString()
    }

    //metodo per sovrascrivere un oggetto
    fun set(fitnessCard : FitnessCard?){
        name = fitnessCard?.name
        description = fitnessCard?.description
        exercises = fitnessCard?.exercises
        timeDuration = fitnessCard?.timeDuration
    }
}

