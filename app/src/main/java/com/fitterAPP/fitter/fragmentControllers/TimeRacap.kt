package com.fitterAPP.fitter.fragmentControllers

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fitterAPP.fitter.classes.DayRecap
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.random.Random


class TimeRacap : Fragment() {

    private var lineChart :LineChart ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v : View = inflater.inflate(R.layout.fragment_time_racap, container, false)

        var recap : ArrayList<DayRecap> = ArrayList<DayRecap>()

        for (i in 0..10)
            //recap.add(DayRecap(i, 2, 2022, 10F,  500-Random.nextInt(10, 100).toFloat()))

        graphFromArray(recap, v, activity as MainActivity)
        return v
    }

    fun graphFromArray(recap : ArrayList<DayRecap>, view : View, context : MainActivity){
        lineChart = view.findViewById(R.id.lineChartRecap)
        //lineChart?.setDrawGridBackground(true)

        lineChart?.setDrawBorders(true)
        lineChart?.description?.isEnabled = false
        lineChart?.setPinchZoom(false)
        lineChart?.axisRight?.isEnabled = false

        lineChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        lineChart?.setBorderWidth(0f)


        var l : Legend = lineChart?.legend!!
        l.isEnabled = false

        var weightRecap : ArrayList<Entry> = ArrayList()

        for (i in 0 until recap.size)
            break
            //weightRecap.add(Entry(recap[i].dayOfMonth.toFloat(), recap[i].currentWeight))

        var line : LineDataSet = LineDataSet(weightRecap, "Weight")
        line.setAxisDependency(YAxis.AxisDependency.LEFT)
        line.setDrawCircles(true)
        line.setCircleColor(ContextCompat.getColor(requireActivity(), R.color.primary))
        line.color = ContextCompat.getColor(requireActivity(), R.color.primary)

        line.fillAlpha = 125
        line.setDrawFilled(true)

        val bgDrawable : Drawable = ContextCompat.getDrawable(view.context, R.drawable.gradient_chart_background)!!

        line.cubicIntensity = 0f
        line.setMode(LineDataSet.Mode.CUBIC_BEZIER)

        line.setFillDrawable(bgDrawable)
        var paint : Paint = Paint()
        line.lineWidth = 3f

        var lineData : LineData = LineData(line)
        lineData.setDrawValues(false)

        lineChart?.data = lineData
    }


}