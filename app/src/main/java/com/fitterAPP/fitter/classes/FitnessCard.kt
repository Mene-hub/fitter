package com.fitterAPP.fitter.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class FitnessCard(var name:String?, var description: String?, var timeDuration: Int?, var exercises: @RawValue MutableList<Exercise>?, var key : String, var imageCover : CardsCover) : Parcelable {

    constructor() : this ("Scheda", "Default description",60, null, "", CardsCover.woman)

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
        this.imageCover = fitnessCard.imageCover
    }

}

