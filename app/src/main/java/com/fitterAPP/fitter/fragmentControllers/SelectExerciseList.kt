package com.fitterAPP.fitter.fragmentControllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.classes.Root
import com.fitterAPP.fitter.databinding.FragmentSelectExerciseListBinding
import com.fitterAPP.fitter.itemsAdapter.SuggestionAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SelectExerciseList : DialogFragment() {

    private lateinit var binding : FragmentSelectExerciseListBinding
    private val args by navArgs<newExercieFormDialogArgs>()
    private lateinit var fitnessCard : FitnessCard
    private lateinit var adapter : SuggestionAdapter
    private val warmupex = "[\"Tapis Roullant\",\"Cyclette\",\"Elliptical\",\"Spin Bike\", \"Stepper\"]"
    private var index : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSelectExerciseListBinding.inflate(inflater, container, false)

        fitnessCard = args.fitnessCard
        index = args.index
        val gson = Gson()
        val type = object : TypeToken<MutableList<String>>() {}.type
        var warmupEx : MutableList<String> = gson.fromJson(warmupex, type)

        //adapter = context?.let { SuggestionAdapter((activity as MainActivity), warmupEx) }!!
        binding.ExRecycle.adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1,warmupEx)

        binding.ExRecycle.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            fitnessCard.exercises?.get(index)?.exerciseName = parent.getItemAtPosition(position) as String
            val action : NavDirections = SelectExerciseListDirections.actionSelectExerciseListToSetWarmUpExercise(fitnessCard, index)
            findNavController().navigate(action)
        }

        return binding.root
    }
}