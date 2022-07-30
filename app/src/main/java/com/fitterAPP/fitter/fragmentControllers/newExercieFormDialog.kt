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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.fitterAPP.fitter.databinding.FragmentNewExercieFormDialogBinding


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


        //setting the exercise name
        binding.ExNameTV.text = fitnessCard.exercises?.get(index)?.exerciseName

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

        //settings ui with exercise data
        if(fitnessCard.exercises?.get(index)?.exerciseSer != null){
            textSeries.text = fitnessCard.exercises?.get(index)?.exerciseSer!!.toString()
            textReps.text = fitnessCard.exercises?.get(index)?.exerciseRep!!.toString()
            textTime.text = fitnessCard.exercises?.get(index)?.exerciseRest!!.toString() + "s"
        }

        //save card on db and close the fragment
        binding.SaveExercise.setOnClickListener {
            System.out.println(textReps.text.toString() + " " + textSeries.text.toString() + " " + textTime.text.toString().removeSuffix("s").toDouble())
            fitnessCard.exercises?.get(index)?.setAsNormal(textReps.text.toString().toInt(), textSeries.text.toString().toInt(), textTime.text.toString().removeSuffix("s").toInt())
            StaticFitnessCardDatabase.setFitnessCardItem(StaticFitnessCardDatabase.database.getReference(getString(R.string.FitnessCardsReference)), Athlete.UID, fitnessCard)
            val action : NavDirections = newExercieFormDialogDirections.actionNewExercieFormDialogToModifyCard(fitnessCard)
            //findNavController().clearBackStack(0)
            findNavController().navigate(action)
        }

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun clearBackStack(fragmentManager: FragmentManager) {
        if (fragmentManager.backStackEntryCount > 0) {
            val entry: FragmentManager.BackStackEntry = fragmentManager.getBackStackEntryAt(0)
            fragmentManager.popBackStack(entry.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

}