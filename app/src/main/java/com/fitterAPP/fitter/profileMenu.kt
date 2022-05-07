package com.fitterAPP.fitter

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView



// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    profileMenu.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */

class profileMenu : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v:View  = inflater.inflate(R.layout.fragment_profile_menu, container, false)
        val searchbt : AppCompatTextView = v.findViewById(R.id.txt_search_profiles)
        searchbt.setOnClickListener( View.OnClickListener {
            (activity as MainActivity).showSearch()
            dismiss()
        })

        return v
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }
}