package com.fitterAPP.fitter.fragmentControllers

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fitterAPP.fitter.MainActivity
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
    private lateinit var exercise: Exercise
    private var index : Int = 0
    private var backupCard : FitnessCard ? = null

    //set full screen fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        fitnessCard = args.fitnessCard
        index = args.index

        //warm-up exercise selected
        binding.WarmapCV.setOnClickListener {

            exercise = Exercise("Place Holder", ExerciseType.warmup)
            exercise.exerciseId = getLastIndex()


            val action : NavDirections = select_exercise_groupDirections.actionSelectExerciseGroupToSelectExerciseList(fitnessCard, index, exercise)
            findNavController().navigate(action)
        }

        //normal exercise selected
        binding.NormalExerciseCV.setOnClickListener{

            exercise = Exercise("Place Holder", ExerciseType.normal)
            exercise.exerciseId = getLastIndex()

            val action : NavDirections = select_exercise_groupDirections.actionSelectExerciseGroupToFindExercise(fitnessCard, index, exercise)
            findNavController().navigate(action)
        }

        //piramidal exercise selected
        binding.PyramidExerciseCV.setOnClickListener{

            exercise = Exercise("Place Holder", ExerciseType.pyramid)
            exercise.exerciseId = getLastIndex()

            val action : NavDirections = select_exercise_groupDirections.actionSelectExerciseGroupToFindExercise(fitnessCard, index, exercise)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSelectExerciseGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun getLastIndex() : Int{
        var lastindex : Int = 0

        if(fitnessCard.exercises?.size!! > 0)
            for (i in fitnessCard.exercises!!)
                if(i.exerciseId != null && i.exerciseId!! > lastindex)
                    lastindex = i.exerciseId!!

        return lastindex + 1
    }


}