package com.fitterAPP.fitter.fragmentControllers

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.Exercise
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.fitterAPP.fitter.databinding.FragmentSetWarmUpExerciseBinding

class SetWarmUpExercise : DialogFragment() {

    private lateinit var binding : FragmentSetWarmUpExerciseBinding
    private val args by navArgs<newExercieFormDialogArgs>()
    private lateinit var fitnessCard : FitnessCard
    private lateinit var exercise: Exercise
    private var index : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Set transparent status bar
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


        binding = FragmentSetWarmUpExerciseBinding.inflate(inflater, container, false)

        //Get FitnessCard by bundle passed via navigation controller in [FitnessCardAdapter.kt] (the bundle is also set in fragment_navigation.xml
        fitnessCard = args.fitnessCard
        exercise = args.exercise!!
        index = args.index

        //setting the exercise name
        binding.ExNameTV.text = exercise.exerciseName

        var screenHeight = 0

        //getting the screen height
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val windowMetrics = activity?.windowManager?.currentWindowMetrics
                val display: Rect = windowMetrics?.bounds!!
                screenHeight = display.height()/3
            } catch (e: NoSuchMethodError) {}

        } else {
            val metrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
            screenHeight = metrics.heightPixels/3
        }

        //setting the height
        val params = FrameLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT, screenHeight)

        binding.Header.layoutParams = params

        //back bt pressed
        binding.backBt.setOnClickListener {
            findNavController().navigateUp()
        }

        //restore time
        var subTime = binding.subTimeCV
        var addTime = binding.addTimeCV
        var textTime = binding.TimeTV

        subTime.setOnClickListener{
            var tmp = textTime.text.toString().removeSuffix( " min").toInt()
            if(tmp > 0){
                tmp --
                textTime.text = tmp.toString() + " min"
            }
        }

        addTime.setOnClickListener{
            var tmp = textTime.text.toString().removeSuffix(" min").toInt()
            tmp ++
            textTime.text = tmp.toString() + " min"
        }

        if(exercise.exerciseDuration != null){
            textTime.text = exercise.exerciseDuration.toString() + " min"
        }

        binding.SaveExercise.setOnClickListener {
            //set the exercise
            exercise.setAsWarmup(textTime.text.toString().removeSuffix(" min").toInt())

            //save the exercise in the card object
            if(fitnessCard.exercises?.size!! == index)
                fitnessCard.exercises?.add(exercise)
            else
                fitnessCard.exercises?.set(index, exercise)

            //get the reference of fitness_card
            StaticFitnessCardDatabase.setFitnessCardItem(StaticFitnessCardDatabase.database.getReference(getString(R.string.FitnessCardsReference)), Athlete.UID, fitnessCard)

            val action : NavDirections = SetWarmUpExerciseDirections.actionSetWarmUpExerciseToModifyCard(fitnessCard)
            findNavController().navigate(action)
        }

        return binding.root
    }

}