package com.fitterAPP.fitter.FragmentControlers

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.fitterAPP.fitter.Classes.DayRecap
import com.fitterAPP.fitter.CustomView.DrawView
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import kotlin.math.nextDown
import kotlin.random.Random

class TimeRacap : Fragment() {

    private var drawView :DrawView?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v : View = inflater.inflate(R.layout.fragment_time_racap, container, false)

        var recap : ArrayList<DayRecap> = ArrayList<DayRecap>()

        for (i in 0..10)
            recap.add(DayRecap(i, 2, 2022, 10F,  500-Random.nextInt(10, 100).toFloat()))

        graphFromArray(recap, v, activity as MainActivity)
        return v
    }

    fun graphFromArray(recap : ArrayList<DayRecap>, view : View, context : MainActivity){
        drawView = view.findViewById(R.id.myCanvas_DV)
        drawView!!.paint!!.color = ContextCompat.getColor(context, R.color.primary)
        drawView!!.dots = recap
    }
}