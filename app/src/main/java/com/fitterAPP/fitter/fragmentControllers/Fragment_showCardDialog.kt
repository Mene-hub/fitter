package com.fitterAPP.fitter.fragmentControllers

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.itemsAdapter.FitnessCardExercisesAdapter
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.databinding.FragmentShowCardDialogBinding


class Fragment_showCardDialog(var newFitnessCard: FitnessCard) : DialogFragment() {

    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */

    private lateinit var binding : FragmentShowCardDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShowCardDialogBinding.inflate(inflater, container, false)

        binding.backBt.setOnClickListener {
            activity?.onBackPressed()
        }

        val recycle : RecyclerView = binding.exercisesListRV

        /*
        var databaseHelper : RealTimeDBHelper
        val _REFERENCE : String = "FITNESS_CARDS"
        var dbReference : DatabaseReference = FirebaseDatabase.getInstance(RealTimeDBHelper.getDbURL()).getReference(_REFERENCE).child("${Athlete.UID}-FITNESSCARD")
        databaseHelper = RealTimeDBHelper(dbReference) //USING DEFAULT VALUE

        databaseHelper.setFitnessCardItem(newFitnessCard)*/

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

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val a: Animation = object : Animation() {}
        a.duration = 0
        return a
    }

}