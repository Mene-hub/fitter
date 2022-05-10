package com.fitterAPP.fitter.FragmentControlers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fitterAPP.fitter.Classes.DayRecap
import com.fitterAPP.fitter.R

class TimeRacap : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v : View = inflater.inflate(R.layout.fragment_time_racap, container, false)

        var Recap : ArrayList<DayRecap> = ArrayList<DayRecap>()

        for (i in 1..10)
            Recap.add(DayRecap(i, 2, 2022, 1F ))


        return v
    }

    fun graphFromArray(Recap : ArrayList<DayRecap>){

    }
}