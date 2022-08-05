package com.fitterAPP.fitter.fragmentControllers

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.databinding.FragmentProfileMenuBinding

class profileMenu : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentProfileMenuBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileMenuBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtUserProfile.setOnClickListener{
            findNavController().navigate(R.id.action_profileMenu_to_profile)
            dismiss()
        }

        binding.txtLogout.setOnClickListener {
            (activity as MainActivity).logout()
            dismiss()
        }

        binding.txtBookmark.setOnClickListener{
            findNavController().navigate(R.id.action_bottomSheetDialog_to_bookmarkCards)
            dismiss()
        }

    }



    companion object {
        const val TAG = "ModalBottomSheet"
    }
}