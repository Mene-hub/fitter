package com.fitterAPP.fitter.FragmentControlers

import android.R
import com.fitterAPP.fitter.R.layout
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.Classes.Exercise
import com.fitterAPP.fitter.Classes.FitnessCard
import com.fitterAPP.fitter.ItemsAdapter.FitnessCardAdapter
import com.fitterAPP.fitter.ItemsAdapter.FitnessCardExercisesAdapter
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.databinding.FragmentCreateCardDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class Fragment_createCardDialog(var newFitnessCard: FitnessCard) : DialogFragment() {

    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */

    private lateinit var binding : FragmentCreateCardDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateCardDialogBinding.inflate(inflater, container, false)

        binding.backBt.setOnClickListener {
            activity?.onBackPressed()
        }

        val recycle : RecyclerView = binding.exercisesListRV

        if(newFitnessCard.exercises != null && newFitnessCard.exercises?.size!! > 0){

            Toast.makeText(activity, newFitnessCard.exercises?.count().toString(), Toast.LENGTH_SHORT).show()
            var adapter = context?.let {FitnessCardExercisesAdapter((activity as MainActivity),newFitnessCard,newFitnessCard.exercises!!)}!!
            recycle.adapter = adapter

        }

        // Inflate the layout for this fragment
        return binding.root
    }

    /** The system calls this only when creating the layout in a dialog. */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.


        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val a: Animation = object : Animation() {}
        a.duration = 0
        return a
    }

}