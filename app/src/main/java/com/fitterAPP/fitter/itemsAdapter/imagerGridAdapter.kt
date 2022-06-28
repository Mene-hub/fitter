package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.R

class imagerGridAdapter(val context2: Context, val images : MutableList<Int>) : RecyclerView.Adapter<imagerGridAdapter.Holder>() {

    var radioButton : RadioButton? = null

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var bgImage : ImageView = itemView.findViewById(R.id.bgImage)
        var card : CardView = itemView.findViewById(R.id.cardbg)
        var radioButton : RadioButton = itemView.findViewById(R.id.selected_rb)

        fun setCard(id: Int){
            bgImage.setImageResource(id)
            bgImage.setOnClickListener{
                //checked radio button
                radioButton.isChecked = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(context2).inflate(R.layout.item_image_list, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val image: Int = images[position]
        holder.setCard(image)

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