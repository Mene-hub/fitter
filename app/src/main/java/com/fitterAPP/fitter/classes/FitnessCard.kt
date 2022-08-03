package com.fitterAPP.fitter.classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import com.fitterAPP.fitter.interfaces.FitnessCardInterface


@Parcelize
data class FitnessCard(override var name: String?, override var description: String?, override var timeDuration: Int?,  override var  exercises: @RawValue MutableList<Exercise>?, override var key: String, override var imageCover: CardsCover) : Parcelable,FitnessCardInterface {

    constructor() : this ("Scheda", "Default description",60, null, "", CardsCover.woman)

    override fun toString() : String{
        return description.toString()
    }

    //metodo per sovrascrivere un oggetto
    override fun set(fitnessCard : FitnessCard?){
        this.name = fitnessCard?.name
        this.description = fitnessCard?.description
        this.exercises = fitnessCard?.exercises
        this.timeDuration = fitnessCard?.timeDuration
        this.key = fitnessCard!!.key
        this.imageCover = fitnessCard.imageCover
    }


}

