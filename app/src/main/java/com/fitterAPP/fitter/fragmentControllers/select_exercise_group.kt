package com.fitterAPP.fitter.fragmentControllers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Exercise
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.classes.NormalExercise
import com.fitterAPP.fitter.databinding.FragmentNewExercieFormDialogBinding
import com.fitterAPP.fitter.databinding.FragmentSelectExerciseGroupBinding

/**
 * class for select new exercise group
 */
class select_exercise_group : DialogFragment() {

    private lateinit var binding : FragmentSelectExerciseGroupBinding
    private val args by navArgs<newExercieFormDialogArgs>()
    private lateinit var fitnessCard : FitnessCard
    private var index : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSelectExerciseGroupBinding.inflate(inflater, container, false)

        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        fitnessCard = args.fitnessCard
        index = args.index

        binding.NormalExerciseCV.setOnClickListener{
            fitnessCard.exercises?.add(NormalExercise("Name PlaceHolder", ArrayList(), 1, 1,60.0))
            val action : NavDirections = select_exercise_groupDirections.actionSelectExerciseGroupToNewExercieFormDialog(fitnessCard, index)
            findNavController().navigate(action)
        }

        return binding.root
    }
}