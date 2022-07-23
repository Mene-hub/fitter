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
import com.fitterAPP.fitter.classes.ExerciseType
import com.fitterAPP.fitter.classes.FitnessCard
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

    //set full screen fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSelectExerciseGroupBinding.inflate(inflater, container, false)

        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        fitnessCard = args.fitnessCard
        index = args.index

        //warm-up exercise selected
        binding.WarmapCV.setOnClickListener {
            if(fitnessCard.exercises?.size == index)
                fitnessCard.exercises?.add(Exercise("Place Holder", ExerciseType.warmup))
            else
                fitnessCard.exercises?.set(index, Exercise(fitnessCard.exercises?.get(index)?.exerciseName!!, ExerciseType.warmup))

            val action : NavDirections = select_exercise_groupDirections.actionSelectExerciseGroupToSelectExerciseList(fitnessCard, index)
            findNavController().navigate(action)
        }

        //normal exercise selected
        binding.NormalExerciseCV.setOnClickListener{
            if(fitnessCard.exercises?.size == index)
                fitnessCard.exercises?.add(Exercise("Place Holder", ExerciseType.normal))
            else
                fitnessCard.exercises?.set(index, Exercise(fitnessCard.exercises?.get(index)?.exerciseName!!, ExerciseType.normal))

            val action : NavDirections = select_exercise_groupDirections.actionSelectExerciseGroupToFindExercise(fitnessCard, index)
            findNavController().navigate(action)
        }

        return binding.root
    }
}