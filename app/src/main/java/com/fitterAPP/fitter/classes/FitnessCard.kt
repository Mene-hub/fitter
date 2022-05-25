package com.fitterAPP.fitter.classes


data class FitnessCard(var name:String?, var description: String?, var timeDuration: Int?, var exercises: MutableList<Exercise>?, var key : String) {

    constructor() : this ("Scheda", "Default description",60, null, "")

    override fun toString() : String{
        return description.toString()
    }

    //metodo per sovrascrivere un oggetto
    fun set(fitnessCard : FitnessCard?){
        this.name = fitnessCard?.name
        this.description = fitnessCard?.description
        this.exercises = fitnessCard?.exercises
        this.timeDuration = fitnessCard?.timeDuration
        this.key = fitnessCard!!.key
    }
}

