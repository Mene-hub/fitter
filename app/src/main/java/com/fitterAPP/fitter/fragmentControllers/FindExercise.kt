package com.fitterAPP.fitter.fragmentControllers

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.*
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.fitterAPP.fitter.databinding.FragmentFindExerciseBinding
import com.fitterAPP.fitter.itemsAdapter.SuggestionAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jsoup.Jsoup
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.log

class FindExercise : DialogFragment() {
    private lateinit var binding : FragmentFindExerciseBinding
    private val args by navArgs<newExercieFormDialogArgs>()
    private lateinit var fitnessCard : FitnessCard
    private lateinit var adapter : SuggestionAdapter
    private var index : Int = 0
    private lateinit var exercise : Exercise
    var Exercises : Root? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding = FragmentFindExerciseBinding.inflate(inflater, container, false)

        fitnessCard = args.fitnessCard
        exercise = args.exercise!!
        index = args.index

        binding.SVFindExercise.setOnQueryTextListener(queryTextListener())

        //licking the item will open the exercise information dialog
        binding.ExRecycle.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            var temp : Exercise = Exercise()

            temp.exerciseName = parent.getItemAtPosition(position) as String

            //saving the wger id in the temp exercise
            temp.wgerId = Exercises?.suggestions?.get(position)?.data?.id!!

            showExerciseinformation(temp)

        }
        return binding.root
    }

    fun queryTextListener() : SearchView.OnQueryTextListener {
        val listener = object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.SVFindExercise.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!!.isNotBlank()) {

                    val executor: ExecutorService = Executors.newSingleThreadExecutor()
                    val handler: Handler = Handler(Looper.getMainLooper())
                    var normalEx : MutableList<Result>? = null

                    executor.execute(Runnable () {
                        executor.run() {
                            try {
                                Exercises = ExerciseQueryHelper.getExercises(newText)
                            }catch(e:Exception ){ Log.d("FindExercise", e.toString()) }
                        }

                        handler.post(Runnable() {
                            handler.run {
                                try {
                                var names : MutableList<String> = ArrayList()
                                    for (i in 0..(Exercises?.suggestions?.size!!) -1)
                                        names.add(Exercises?.suggestions?.get(i)?.data?.name!!)

                                    binding.ExRecycle.adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, names)
                                }catch(e:Exception ){ Log.d("FindExercise", e.toString()) }
                            }
                        })
                    })
                }
                return true
            }
        }
        return listener
    }

    private fun queryById(id : Int){
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler: Handler = Handler(Looper.getMainLooper())
        var ex : Result

        executor.execute(Runnable () {
            executor.run() {
               ex  = ExerciseQueryHelper.getSingleExerciseById(id)
            }

            handler.post(Runnable() {
                exercise.description = Jsoup.parse(ex.description).text()
                exercise.exerciseName = ex.name
                exercise.wgerId = ex.id
                exercise.wgerBaseId = ex.exercise_base
                var action : NavDirections ? = null

                if(exercise.type == ExerciseType.normal)
                    action = FindExerciseDirections.actionFindExerciseToNewExercieFormDialog(fitnessCard, index, exercise)

                if(exercise.type == ExerciseType.pyramid)
                    action = FindExerciseDirections.actionFindExerciseToSetPiramidalExercise(fitnessCard, exercise, index)

                findNavController().navigate(action!!)
            })
        })
    }

    fun showExerciseinformation( ex:Exercise ){
        // Create an alert builder
        val builder = MaterialAlertDialogBuilder(requireContext(),  R.style.ThemeOverlay_App_MaterialAlertDialog)
        // set the custom layout

        val customLayout: View = layoutInflater.inflate(R.layout.dialog_open_exercise, null)


        var imageList : ImageSlider = customLayout.findViewById(R.id.exercise_image_slider)
        var list : MutableList<SlideModel> = ArrayList<SlideModel>()

        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler: Handler = Handler(Looper.getMainLooper())
        var imageUri : MutableList<String> ? = null
        var exerciseInfo : Result ? = null
        var exName : TextView = customLayout.findViewById(R.id.ExName_TV)
        var exDescription : TextView = customLayout.findViewById(R.id.exDescription_TV)
        var description : String = ""

        executor.execute(Runnable () {
            executor.run() {

                //getting the base exercise id from the wger exercise id
                exerciseInfo = ExerciseQueryHelper.getSingleExerciseById(ex.wgerId!!)


                description = Jsoup.parse(exerciseInfo?.description!!).text()

                //getting the images for the exercise
                imageUri = ExerciseQueryHelper.getImageFromExercise(exerciseInfo?.exercise_base!!)
            }

            handler.post(Runnable() {

                //setting the images
                if(imageUri != null && imageUri?.size!!>0) {

                    for(i in 1..imageUri?.size!!)
                        list.add(SlideModel(imageUri?.get(i-1)))

                    imageList.setImageList(list, ScaleTypes.CENTER_INSIDE)
                    customLayout.findViewById<ImageView>(R.id.placeHolder).isGone = true
                }else
                    customLayout.findViewById<ImageView>(R.id.placeHolder).isGone = false

                //setting the name in view
                exName.setText(exerciseInfo?.name)

                //setting the description in th view
                exDescription.setText(description)
            })
        })



        //building the view
        builder.setView(customLayout)









        // add a button
        builder

            .setPositiveButton("OK") { _, _ -> // send data from the

                queryById(exerciseInfo?.id!!)

            }

            .setNegativeButton("BACK") { _, _ ->


            }

            .setOnDismissListener {

            }
            .show()
    }

}