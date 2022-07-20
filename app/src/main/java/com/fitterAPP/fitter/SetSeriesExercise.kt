package com.fitterAPP.fitter

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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.fitterAPP.fitter.databinding.FragmentNewExercieFormDialogBinding
import com.fitterAPP.fitter.databinding.FragmentSetSeriesExerciseBinding
import com.fitterAPP.fitter.fragmentControllers.newExercieFormDialogArgs
import com.fitterAPP.fitter.fragmentControllers.newExercieFormDialogDirections

class SetSeriesExercise : DialogFragment() {
    private lateinit var binding : FragmentSetSeriesExerciseBinding
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
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Set transparent status bar
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


        binding = FragmentSetSeriesExerciseBinding.inflate(inflater, container, false)

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
        binding.AddSeriesExercise.setOnClickListener {
            //fitnessCard.exercises?.get(index)?.setAsSeries()
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