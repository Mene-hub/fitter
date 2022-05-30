package com.fitterAPP.fitter.fragmentControllers

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class StringEditMenu(val propertyname : String, var Value : String, var card: FitnessCard) : BottomSheetDialogFragment() {

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        val v : View = inflater.inflate(R.layout.fragment_string_edit_menu, container, false)

        v.findViewById<TextView>(R.id.PropertyName_TV).text = propertyname
        v.findViewById<TextView>(R.id.et_editedString).text = Value

        v.findViewById<TextView>(R.id.backBt_TV).setOnClickListener {
            dismiss()
        }

        v.findViewById<TextView>(R.id.saveBt_TV).setOnClickListener {

            if(propertyname == "Card name")
                card.name = v.findViewById<TextView>(R.id.et_editedString).text.toString()
            else
                if(propertyname == "Card description")
                    card.description = v.findViewById<TextView>(R.id.et_editedString).text.toString()

            MyFitnessCards().addFitnessCard(card)
            dismiss()
        }

        return v
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

}