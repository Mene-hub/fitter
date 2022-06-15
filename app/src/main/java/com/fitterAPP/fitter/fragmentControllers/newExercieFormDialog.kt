package com.fitterAPP.fitter.fragmentControllers

import android.app.Dialog
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.classes.NormalExercise
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.fitterAPP.fitter.databinding.FragmentNewExercieFormDialogBinding
import com.fitterAPP.fitter.databinding.FragmentShowCardDialogBinding


class newExercieFormDialog : DialogFragment() {

    private lateinit var binding : FragmentNewExercieFormDialogBinding
    private val args by navArgs<newExercieFormDialogArgs>()
    private lateinit var fitnessCard : FitnessCard
    private var index : Int = 0

    /**
     * onCreate method which is used to set the dialog style. This mathod is paired with a WindowManager setting done in [onCreateView]
     * @author Daniel Satriano
     * @author Claudio MEnegotto
     * @since 1/06/2022
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Set transparent status bar
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


        binding = FragmentNewExercieFormDialogBinding.inflate(inflater, container, false)

        //Get FitnessCard by bundle passed via navigation controller in [FitnessCardAdapter.kt] (the bundle is also set in fragment_navigation.xml
        fitnessCard = args.fitnessCard
        index = args.index

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

        //Series
        var subSeries = binding.subSeriesCV
        var addSeries = binding.addSeriesCV
        var textSeries = binding.SeriesTV

        subSeries.setOnClickListener{
            var tmp = textSeries.text.toString().toInt()
            if(tmp > 1){
                tmp --
                textSeries.text = tmp.toString()
            }
        }

        addSeries.setOnClickListener{
            var tmp = textSeries.text.toString().toInt()
                tmp ++
                textSeries.text = tmp.toString()
        }

        //Repetitions
        var subReps = binding.subRepsCV
        var addReps = binding.addRepsCV
        var textReps = binding.RepsTV

        subReps.setOnClickListener{
            var tmp = textReps.text.toString().toInt()
            if(tmp > 1){
                tmp --
                textReps.text = tmp.toString()
            }
        }

        addReps.setOnClickListener{
            var tmp = textReps.text.toString().toInt()
            tmp ++
            textReps.text = tmp.toString()
        }

        //restore time
        var subTime = binding.subTimeCV
        var addTime = binding.addTimeCV
        var textTime = binding.TimeTV

        subTime.setOnClickListener{
            var tmp = textTime.text.toString().removeSuffix("s").toInt()
            if(tmp > 0){
                tmp --
                textTime.text = tmp.toString() + "s"
            }
        }

        addTime.setOnClickListener{
            var tmp = textTime.text.toString().removeSuffix("s").toInt()
            tmp ++
            textTime.text = tmp.toString() + "s"
        }

        //save card on db and close the fragment
        binding.SaveExercise.setOnClickListener {
            (fitnessCard.exercises?.get(index) as NormalExercise).exerciseSer = textSeries.text.toString().toInt()
            (fitnessCard.exercises?.get(index) as NormalExercise).exerciseRep = textReps.text.toString().toInt()
            (fitnessCard.exercises?.get(index) as NormalExercise).exerciseRest = textTime.text.toString().removeSuffix("s").toDouble()
            StaticFitnessCardDatabase.setFitnessCardItem(StaticFitnessCardDatabase.database.getReference(getString(R.string.FitnessCardsReference)), Athlete.UID, fitnessCard)
            findNavController().navigateUp()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}