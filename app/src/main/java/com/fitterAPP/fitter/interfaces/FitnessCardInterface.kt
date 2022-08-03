package com.fitterAPP.fitter.interfaces

import com.fitterAPP.fitter.classes.CardsCover
import com.fitterAPP.fitter.classes.Exercise

interface FitnessCardInterface {
    var name:String?
    var description: String?
    var timeDuration: Int?
    var exercises:  MutableList<Exercise>?
    var key : String
    var imageCover : CardsCover

    //metodo per sovrascrivere un oggetto
    fun set(fitnessCard : com.fitterAPP.fitter.classes.FitnessCard?)

}