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
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.itemsAdapter.PiramidalAdapter
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.Exercise
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.fitterAPP.fitter.databinding.FragmentSetPyramidalExerciseBinding

/**
 * @author Menegotto Claudio
 */
class SetPiramidalExercise : DialogFragment() {
    private lateinit var binding : FragmentSetPyramidalExerciseBinding
    private val args by navArgs<newExercieFormDialogArgs>()
    private lateinit var fitnessCard : FitnessCard
    private lateinit var exercise : Exercise
    private var index : Int = 0
    private lateinit var adapter : PiramidalAdapter

    /**
     * onCreate method which is used to set the dialog style. This mathod is paired with a WindowManager setting done in [onCreateView]
     * @author Daniel Satriano
     * @since 1/06/2022
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Set transparent status bar
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


        binding = FragmentSetPyramidalExerciseBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        if(exercise.piramidSeries == null)
            exercise.piramidSeries = ArrayList()

        val recycle : RecyclerView = binding.exercisesListRV
        adapter = PiramidalAdapter(exercise.piramidSeries!!, exercise, requireContext())
        recycle.adapter = adapter

        //restore time
        var subTime = binding.subTimeCV
        var addTime = binding.addTimeCV
        var textTime = binding.TimeTV

        subTime.setOnClickListener{
            var tmp = textTime.text.toString().removeSuffix(" s").toInt()
            if(tmp > 0){
                tmp --
                textTime.text = tmp.toString() + " s"
            }
        }

        addTime.setOnClickListener{
            var tmp = textTime.text.toString().removeSuffix(" s").toInt()
            tmp ++
            textTime.text = tmp.toString() + " s"
        }

        //series
        var subSeries = binding.subSeriesCV
        var addSeries = binding.addSeriesCV
        var textSeries = binding.SeriesTV



        subSeries.setOnClickListener{
            var tmp = textSeries.text.toString().toInt()
            if(tmp > 1){
                tmp --
                textSeries.text = tmp.toString()

                exercise.piramidSeries?.removeLast()
                adapter.notifyDataSetChanged()
            }

        }

        if(exercise.piramidSeries?.size==0)
            exercise.piramidSeries?.add(1)

        adapter.notifyDataSetChanged()

        textSeries.text = exercise.piramidSeries?.size.toString()

        addSeries.setOnClickListener{
            var tmp = textSeries.text.toString().toInt()
            tmp ++
            exercise.piramidSeries?.add(1)
            textSeries.text = tmp.toString()
            adapter.notifyDataSetChanged()

        }

        //save card on db and close the fragment
        binding.saveExercise.setOnClickListener {
            exercise.exerciseSer = exercise.piramidSeries!!.size
            exercise.exerciseRest =  textTime.text.toString().removeSuffix(" s").toInt()

            //save the exercise in the card object
            if(fitnessCard.exercises?.size!! == index)
                fitnessCard.exercises?.add(exercise)
            else
                fitnessCard.exercises?.set(index, exercise)

            StaticFitnessCardDatabase.setFitnessCardItem(StaticFitnessCardDatabase.database.getReference(getString(R.string.FitnessCardsReference)), Athlete.UID, fitnessCard)
            val action : NavDirections = SetPiramidalExerciseDirections.actionSetPiramidalExerciseToModifyCard(fitnessCard)
            //findNavController().clearBackStack(0)
            findNavController().navigate(action)
        }
    }


    private fun clearBackStack(fragmentManager: FragmentManager) {
        if (fragmentManager.backStackEntryCount > 0) {
            val entry: FragmentManager.BackStackEntry = fragmentManager.getBackStackEntryAt(0)
            fragmentManager.popBackStack(entry.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
}