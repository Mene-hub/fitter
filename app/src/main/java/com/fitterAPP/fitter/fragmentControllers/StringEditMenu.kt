package com.fitterAPP.fitter.fragmentControllers

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * BottomSheetDialogFragment to change the string properties of the FitnessCard
 * @author Menegotto Claudio
 */
class StringEditMenu(val propertyname : String, var Value : String, var card: FitnessCard) : BottomSheetDialogFragment() {

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {

        val v : View = inflater.inflate(R.layout.fragment_string_edit_menu, container, false)

        //setting the old value
        v.findViewById<TextView>(R.id.PropertyName_TV).text = propertyname
        v.findViewById<TextView>(R.id.et_editedString).text = Value

        //closing the dialog
        v.findViewById<TextView>(R.id.backBt_TV).setOnClickListener {
            dismiss()
        }

        //save bt clicked
        v.findViewById<TextView>(R.id.saveBt_TV).setOnClickListener {

            if(propertyname == "Card name")
                card.name = v.findViewById<TextView>(R.id.et_editedString).text.toString()
            else
                if(propertyname == "Card description")
                    card.description = v.findViewById<TextView>(R.id.et_editedString).text.toString()


            StaticFitnessCardDatabase.setFitnessCardItem(StaticFitnessCardDatabase.database.getReference(getString(R.string.FitnessCardsReference)), Athlete.UID, card)
            dismiss()
        }

        return v
    }

}