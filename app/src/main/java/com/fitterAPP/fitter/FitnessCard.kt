package com.fitterAPP.fitter


data class FitnessCard(var description : String?, var exercises : MutableList<Exercise>? ) {

    //costruttore vuoto
    constructor() : this ("Default description", null)

    override fun toString() : String{
        return description.toString()
    }

    //metodo per sovrascrivere un oggetto
    fun set(fitnessCard : FitnessCard?){
        description = fitnessCard?.description
        exercises = fitnessCard?.exercises
    }

}
