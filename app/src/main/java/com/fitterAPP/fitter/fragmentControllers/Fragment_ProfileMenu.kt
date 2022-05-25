package com.fitterAPP.fitter.fragmentControllers

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R

const val ARG_ITEM_COUNT = "item_count"

class profileMenu : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v:View  = inflater.inflate(R.layout.fragment_profile_menu, container, false)
        val searchbt : AppCompatTextView = v.findViewById(R.id.txt_search_profiles)
        val logoutbt : AppCompatTextView = v.findViewById(R.id.txt_logout)
        val recapbt : AppCompatTextView = v.findViewById(R.id.txt_recap)
        val calendarbt : AppCompatTextView = v.findViewById(R.id.txt_calendar)
        val myCardsbt : AppCompatTextView = v.findViewById(R.id.txt_mycards)

        searchbt.setOnClickListener( View.OnClickListener {
            (activity as MainActivity).showSearch()
            dismiss()
        })

        logoutbt.setOnClickListener( View.OnClickListener {
            (activity as MainActivity).logout()
            dismiss()
        })

        recapbt.setOnClickListener( View.OnClickListener {
            (activity as MainActivity).showRecap()
            dismiss()
        })

        myCardsbt.setOnClickListener( View.OnClickListener {
            (activity as MainActivity).showMyFitnessCards()
            dismiss()
        })


        return v
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }
}