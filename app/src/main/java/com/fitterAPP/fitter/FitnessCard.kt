package com.fitterAPP.fitter


data class FitnessCard(var description : String, var exercises : MutableList<Exercise>? ) {

    constructor() : this ("Default description", null)
}
