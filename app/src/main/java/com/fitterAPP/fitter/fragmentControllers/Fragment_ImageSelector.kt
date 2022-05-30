package com.fitterAPP.fitter.fragmentControllers

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.classes.CardsCover
import com.fitterAPP.fitter.itemsAdapter.imagerGridAdapter
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.FitnessCard
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ImageSelector(val propertyname : String, var card: FitnessCard) : BottomSheetDialogFragment() {

    var selectedImage:CardsCover = card.imageCover

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        val v : View = inflater.inflate(R.layout.fragment_image_selector, container, false)

        v.findViewById<TextView>(R.id.PropertyName_TV).text = propertyname

        v.findViewById<TextView>(R.id.backBt_TV).setOnClickListener {
            dismiss()
        }

        v.findViewById<TextView>(R.id.saveBt_TV).setOnClickListener {
            card.imageCover = selectedImage
            MyFitnessCards().addFitnessCard(card)
            dismiss()
        }

        val mybgs : MutableList<Int> = ArrayList()
        mybgs.add(R.drawable.gigachad)
        mybgs.add(R.drawable.gigachad2)
        mybgs.add(R.drawable.man_bodybuilder)
        mybgs.add(R.drawable.man_bodybuilder2)
        mybgs.add(R.drawable.woman_bodybuilder)
        mybgs.add(R.drawable.woman_bodybuilder2)

        var recycle = v.findViewById<RecyclerView>(R.id.imageGridList_RV)
        recycle.setLayoutManager(GridLayoutManager(context, 2))



        var adapter = context?.let { imagerGridAdapter((activity as MainActivity),mybgs) }
        recycle.adapter = adapter

        return v
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}