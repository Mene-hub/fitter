package com.fitterAPP.fitter.FragmentControlers

import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.Classes.Exercise
import com.fitterAPP.fitter.Classes.FitnessCard
import com.fitterAPP.fitter.ItemsAdapter.FitnessCardExercisesAdapter
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.databinding.FragmentModifyCardBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton


class ModifyCard(var fitnessCard: FitnessCard) : DialogFragment() {

    private lateinit var binding : FragmentModifyCardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentModifyCardBinding.inflate(inflater, container, false)

        binding.backBt.setOnClickListener {
            activity?.onBackPressed()
        }

        var screenHeight : Int = 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            val size = Point()
            try {
                val windowMetrics = activity?.windowManager?.currentWindowMetrics
                val display: Rect = windowMetrics?.bounds!!
                screenHeight = display.height()/3
            } catch (e: NoSuchMethodError) {}

        } else {
            val metrics = DisplayMetrics()
            activity?.getWindowManager()?.getDefaultDisplay()?.getMetrics(metrics)
            screenHeight = metrics.heightPixels/3
        }

        val params = FrameLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT, screenHeight)

        binding.Header.layoutParams = params

        val recycle : RecyclerView = binding.exercisesListRV

        if(fitnessCard.exercises != null && fitnessCard.exercises?.size!! > 0){
            var adapter = context?.let { FitnessCardExercisesAdapter((activity as MainActivity),fitnessCard,fitnessCard.exercises!!, true) }!!
            recycle.adapter = adapter
        }

        val cardName : TextView = binding.CardNameTV
        val cardDuration : TextView = binding.TimeDurationTV
        val cardDescription: TextView = binding.DescriptionTV

        cardName.text = fitnessCard.name
        cardDescription.text = fitnessCard.description
        cardDuration.text = fitnessCard.timeDuration.toString() + " minutes"

        val newCardBT : ExtendedFloatingActionButton = binding.newExerciseBT
        newCardBT.setOnClickListener {

            if(fitnessCard.exercises == null)
                fitnessCard.exercises = ArrayList()

            fitnessCard.exercises?.add(Exercise())

            if(fitnessCard.exercises != null && fitnessCard.exercises?.size!! > 0){
                var adapter = context?.let { FitnessCardExercisesAdapter((activity as MainActivity),fitnessCard,fitnessCard.exercises!!, true) }!!
                recycle.adapter = adapter
            }

        }

        return binding.root
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val a: Animation = object : Animation() {}
        a.duration = 0
        return a
    }

}