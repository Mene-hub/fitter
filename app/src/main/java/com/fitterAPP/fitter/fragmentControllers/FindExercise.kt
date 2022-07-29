package com.fitterAPP.fitter.fragmentControllers

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.ExerciseQueryHelper
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.classes.Result
import com.fitterAPP.fitter.classes.Root
import com.fitterAPP.fitter.databinding.FragmentFindExerciseBinding
import com.fitterAPP.fitter.itemsAdapter.SuggestionAdapter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FindExercise : DialogFragment() {
    private lateinit var binding : FragmentFindExerciseBinding
    private val args by navArgs<newExercieFormDialogArgs>()
    private lateinit var fitnessCard : FitnessCard
    private lateinit var adapter : SuggestionAdapter
    private var index : Int = 0
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
        index = args.index

        binding.SVFindExercise.setOnQueryTextListener(queryTextListener())

        binding.ExRecycle.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            fitnessCard.exercises?.get(index)?.exerciseName = parent.getItemAtPosition(position) as String

            queryById(Exercises?.suggestions?.get(position)?.data?.id!!)

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
                fitnessCard.exercises?.get(index)?.description = ex.description
                val action : NavDirections = FindExerciseDirections.actionFindExerciseToNewExercieFormDialog(fitnessCard, index)
                findNavController().navigate(action)
            })
        })
    }
}