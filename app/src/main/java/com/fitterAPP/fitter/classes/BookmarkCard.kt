package com.fitterAPP.fitter.classes

import android.os.Parcelable
import com.fitterAPP.fitter.interfaces.FitnessCardInterface
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * @author Daniel Satriano
 * @since 3/08/2022
 */
@Parcelize
class BookmarkCard(override var name: String?, override var description: String?, override var timeDuration: Int?, override var exercises: @RawValue MutableList<Exercise>?,
                   override var key: String, override var imageCover: CardsCover, var ownerUID: String) : Parcelable,FitnessCardInterface {

    constructor() : this ("Scheda", "Default description",60, null, "", CardsCover.woman, "")

    constructor(fitnessCard: FitnessCard, ownerUID: String) : this() {
        this.name = fitnessCard.name
        this.description = fitnessCard.description
        this.ownerUID = ownerUID
        this.exercises = fitnessCard.exercises
        this.timeDuration = fitnessCard.timeDuration
        this.key = fitnessCard.key
        this.imageCover = fitnessCard.imageCover
    }

    /**
     * Converts a [BookmarkCard] to a [FitnessCard]
     * @author Daniel Satriano
     * @since 5/08/2022
     */
    fun toFitnessCard() : FitnessCard{
        return FitnessCard(this.name,this.description,this.timeDuration,this.exercises,this.key,this.imageCover)
    }





    companion object {
        var bookmarkList : MutableList<BookmarkCard> = ArrayList()

    }

}