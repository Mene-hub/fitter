package com.fitterAPP.fitter.classes

import android.content.Context
import android.graphics.Canvas
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*

/**
 * Class used to manage swipes and movements of listItems inside the exercise recyclerview
 * @author Daniel Satriano
 * @since 23/07/2022
 */
class SwipeGesture(){

    /**
     * Used to manage the swipe left action in [com.fitterAPP.fitter.fragmentControllers.ModifyCard]
     * @author Daniel Satriano
     * @since 27/07/2022
     */
    abstract  class SwipeGestureLeft(private val context : Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        private val deleteColor = ContextCompat.getColor(context, R.color.red)
        private val deleteIcon = R.drawable.delete_24
        private val typeface = ResourcesCompat.getFont(context, R.font.roboto_regular)

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

            RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(deleteColor)
                .addSwipeLeftLabel(context.getString(R.string.delete))
                .addSwipeLeftActionIcon(deleteIcon)
                .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
                .setSwipeLeftLabelTypeface(typeface)
                .create()
                .decorate()

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    /**
     * Used to manage the swipe right action in [com.fitterAPP.fitter.fragmentControllers.Fragment_showCardDialog]
     * @author Daniel Satriano
     * @since 27/07/2022
     */
    abstract  class SwipeGestureRight(private val context : Context) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or  ItemTouchHelper.END, ItemTouchHelper.RIGHT) {

        private val recapColor = ContextCompat.getColor(context, R.color.green)
        private val recapIcon : Int = R.drawable.ic_check
        private val typeface = ResourcesCompat.getFont(context, R.font.roboto_regular)

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

            return false
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

            RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeRightBackgroundColor(recapColor)
                .addSwipeRightLabel(context.getString(R.string.done))
                .addSwipeRightActionIcon(recapIcon)
                .setSwipeRightLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
                .setSwipeRightLabelTypeface(typeface)
                .create()
                .decorate()

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

}



