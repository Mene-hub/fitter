package com.fitterAPP.fitter.classes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

/**
 * Class used to manage swipes and movements of listItems inside the exercise recyclerview
 * @author Daniel Satriano
 * @since 23/07/2022
 * @param context context of the caller
 */
abstract class SwipeGesture(context : Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deleteColor = ContextCompat.getColor(context, R.color.red)
    private val recapColor = ContextCompat.getColor(context, R.color.green)
    private val deleteIcon = R.drawable.delete_24
    private val recapIcon = R.drawable.ic_check
    private val typeface = ResourcesCompat.getFont(context, R.font.roboto_regular)


    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {


        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftLabel("Delete")
            .addSwipeLeftActionIcon(deleteIcon)

            .addSwipeRightBackgroundColor(recapColor)
            .addSwipeRightLabel("Done")
            .addSwipeRightActionIcon(recapIcon)

            .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
            .setSwipeRightLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)

            .setSwipeLeftLabelTypeface(typeface)
            .setSwipeRightLabelTypeface(typeface)

            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}