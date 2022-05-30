package com.fitterAPP.fitter.ItemsAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.Classes.Exercise
import com.fitterAPP.fitter.Classes.FitnessCard
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R

class imagerGridAdapter(val context2: Context, val images : MutableList<Int>) : RecyclerView.Adapter<imagerGridAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var bgImage : ImageView = itemView.findViewById(R.id.bgImage)
        var card : CardView = itemView.findViewById(R.id.cardbg)
        var sel : RadioButton = itemView.findViewById(R.id.selected_rb)


        fun setCard(id: Int, context: Context){

            bgImage.setImageResource(id!!)
            itemView.setOnClickListener {
                sel.isChecked = true
            }

            //se non è più selezionato deve deselezionarsi

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view: View = LayoutInflater.from(context2).inflate(R.layout.item_image_list, parent, false)
            return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val image: Int = images[position]
        holder.setCard(image, context2)
    }

    override fun getItemCount(): Int {
        return images.size
    }

}