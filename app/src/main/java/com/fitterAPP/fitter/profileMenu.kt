package com.fitterAPP.fitter

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView

const val ARG_ITEM_COUNT = "item_count"

class profileMenu : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v:View  = inflater.inflate(R.layout.fragment_profile_menu, container, false)
        val searchbt : AppCompatTextView = v.findViewById(R.id.txt_search_profiles)
        val logoutbt : AppCompatTextView = v.findViewById(R.id.txt_logout)

        searchbt.setOnClickListener( View.OnClickListener {
            (activity as MainActivity).showSearch()
            dismiss()
        })

        logoutbt.setOnClickListener( View.OnClickListener {
            (activity as MainActivity).showlogout()
            dismiss()
        })

        return v
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }
}