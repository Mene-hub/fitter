package com.fitterAPP.fitter.fragmentControllers

import android.R
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.databinding.FragmentShowCardDialogBinding
import com.fitterAPP.fitter.itemsAdapter.FitnessCardExercisesAdapter


class Fragment_showCardDialog() : DialogFragment() {

    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */

    private lateinit var binding : FragmentShowCardDialogBinding
    private val args by navArgs<Fragment_showCardDialogArgs>()
    private lateinit var newFitnessCard : FitnessCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_DeviceDefault_DialogWhenLarge)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShowCardDialogBinding.inflate(inflater, container, false)

        newFitnessCard = args.cardBundle

        binding.backBt.setOnClickListener {
            findNavController().navigateUp()
        }

        val recycle : RecyclerView = binding.exercisesListRV

        if(newFitnessCard.exercises != null && newFitnessCard.exercises?.size!! > 0){

            newFitnessCard.exercises?.addAll(newFitnessCard.exercises!!)
            newFitnessCard.exercises?.addAll(newFitnessCard.exercises!!)
            newFitnessCard.exercises?.addAll(newFitnessCard.exercises!!)

            var adapter = context?.let {FitnessCardExercisesAdapter((activity as MainActivity),newFitnessCard,newFitnessCard.exercises!!)}!!
            recycle.adapter = adapter
        }

        val cardName : TextView = binding.CardNameTV
        val cardDuration : TextView = binding.TimeDurationTV
        val cardDescription: TextView = binding.DescriptionTV

        cardName.text = newFitnessCard.name
        cardDescription.text = newFitnessCard.description
        cardDuration.text = newFitnessCard.timeDuration.toString() + " minutes"

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



    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation {
        val a: Animation = object : Animation() {}
        a.duration = 0
        return a
    }

}