package com.fitterAPP.fitter.CustomView

import android.R.attr.startX
import android.R.attr.startY
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.fitterAPP.fitter.Classes.DayRecap
import com.fitterAPP.fitter.R


@SuppressLint("ResourceAsColor")
class DrawView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var dots : MutableList<DayRecap>?=null
    var paint = Paint()
    var paint_circle = Paint()

    init {
       paint.strokeWidth = 10f
       paint.isAntiAlias = true

        paint_circle.strokeWidth = 10f
        paint_circle.color = Color.BLUE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if(dots!=null && dots!!.size>0) {
            val metrics : DisplayMetrics = DisplayMetrics()
            var percentageX : Float = (display.width/dots!!.size-1).toFloat()
            for (i in 0..dots!!.size - 2) {
                canvas.drawLine(
                    i * percentageX,
                    dots!![i].currentWeight.toFloat(),
                    (i + 1) * percentageX,
                    dots!![i + 1].currentWeight.toFloat(),
                    paint
                )

                canvas.drawCircle(i * percentageX, dots!![i].currentWeight.toFloat(),10F, paint_circle )
            }

        }
    }
}