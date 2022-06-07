package com.fitterAPP.fitter.fragmentControllers

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import com.fitterAPP.fitter.classes.ExerciseQueryHelper
import com.fitterAPP.fitter.classes.Root
import com.fitterAPP.fitter.databinding.DialogNewExerciseBinding

class Fragment_newExercise() : DialogFragment(){

    private var exerciseSuggestions : Root? = null
    private lateinit var binding : DialogNewExerciseBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogNewExerciseBinding.inflate(inflater,container,false)

        binding.SVFindExercise.setOnQueryTextListener(queryTextListener())

        return binding.root
    }

    private fun queryTextListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchExercise(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchExercise(newText)
                return true
            }
        }
    }

    private fun searchExercise(query: String?){
        if(query != null) {
            exerciseSuggestions = ExerciseQueryHelper.getExercises(query)
        }
    }

}