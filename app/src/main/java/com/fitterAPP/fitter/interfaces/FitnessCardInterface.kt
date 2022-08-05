package com.fitterAPP.fitter.interfaces

import com.fitterAPP.fitter.classes.CardsCover
import com.fitterAPP.fitter.classes.Exercise

/**
 * @author Daniel Satriano
 * @since 3/08/2022
 */
interface FitnessCardInterface {
    var name:String?
    var description: String?
    var timeDuration: Int?
    var exercises:  MutableList<Exercise>?
    var key : String
    var imageCover : CardsCover


}