package com.fitterAPP.fitter.fragmentControllers

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.fragment.findNavController
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.databinding.FragmentProfileMenuBinding

const val ARG_ITEM_COUNT = "item_count"

class profileMenu : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentProfileMenuBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding =FragmentProfileMenuBinding.inflate(inflater,container,false)

        val logoutbt : AppCompatTextView = binding.txtLogout
        val recapbt : AppCompatTextView = binding.txtRecap



        logoutbt.setOnClickListener {
            (activity as MainActivity).logout()
            dismiss()
        }

        recapbt.setOnClickListener( View.OnClickListener {
            findNavController().navigate(R.id.action_myFitnessCards_to_timeRacap)
            dismiss()
        })

        return binding.root
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }
}