package com.fitterAPP.fitter.itemsAdapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.CardsCover
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.InsetDialogOnTouchListener

class imagerGridAdapter(val context2: Context, val images : MutableList<Int>, var card : FitnessCard, val dialog: BottomSheetDialogFragment) : RecyclerView.Adapter<imagerGridAdapter.Holder>() {

    var radioButton : RadioButton? = null

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var bgImage : ImageView = itemView.findViewById(R.id.bgImage)
        var card : CardView = itemView.findViewById(R.id.cardbg)
        var radioButton : RadioButton = itemView.findViewById(R.id.selected_rb)

        fun setCard(id: Int, context: Context, card: FitnessCard, dialog: BottomSheetDialogFragment) {
            bgImage.setImageResource(id)
            if(CardsCover.getResource(card.imageCover) == id)
                radioButton.isChecked = true

            bgImage.setOnClickListener{
                card.imageCover = CardsCover.getFromResource(id)
                StaticFitnessCardDatabase.setFitnessCardItem(StaticFitnessCardDatabase.database.getReference(context.getString(R.string.FitnessCardsReference)), Athlete.UID, card)
                dialog.dismiss()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(context2).inflate(R.layout.item_image_list, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val image: Int = images[position]
        holder.setCard(image, context2, card, dialog)

        //Set listener on radio button
        holder.radioButton.setOnCheckedChangeListener{ _, isChecked ->
            //if radioButton != holder.radioButton -> radioButton.isChecked = false
            if(radioButton != holder.radioButton) {
                radioButton?.isChecked = false
            }
            if (isChecked) {
                radioButton = holder.radioButton
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

}