package com.fitterAPP.fitter.FragmentControlers

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.Classes.CardsCover
import com.fitterAPP.fitter.Classes.FitnessCard
import com.fitterAPP.fitter.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ComboBoxSelectItemMenu(val propertyname : String, var card: FitnessCard) : BottomSheetDialogFragment() {

    var selectedImage:CardsCover = card.imageCover

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        val v : View = inflater.inflate(R.layout.fragment_combo_box_select_item_menu, container, false)

        v.findViewById<TextView>(R.id.PropertyName_TV).text = propertyname

        v.findViewById<TextView>(R.id.backBt_TV).setOnClickListener {
            dismiss()
        }

        v.findViewById<TextView>(R.id.saveBt_TV).setOnClickListener {
            card.imageCover = selectedImage
            MyFitnessCards().addFitnessCard(card)
            dismiss()
        }

        v.findViewById<RecyclerView>(R.id.imageGridList_RV)
        return v
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}