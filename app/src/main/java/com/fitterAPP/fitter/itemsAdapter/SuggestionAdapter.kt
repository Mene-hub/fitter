package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Athlete
import com.squareup.picasso.Picasso

/**
 * @author DanielSatriano
 */
class SuggestionAdapter(val context2: Context, val users:MutableList<Athlete>) : RecyclerView.Adapter<SuggestionAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val username : TextView = itemView.findViewById(R.id.TV_suggestionText)
        val image : ImageView = itemView.findViewById(R.id.IV_profileImage)

        fun setCard(user:Athlete){
            username.text = user.username
            if(user.profilePic != null && user.profilePic != "")
                Picasso.get().load(user.profilePic).resize(100, 100).centerCrop().into(image)

            itemView.setOnClickListener {
                Toast.makeText(itemView.context, username.text.toString(), Toast.LENGTH_LONG).show()

                /*val action : NavDirections = MyFitnessCardsDirections.actionMyFitnessCardsToFragmentShowCardDialog(Card)
                it.findNavController().navigate(action)*/
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(context2).inflate(R.layout.suggestion_adapter_row, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user: Athlete = users[position]
        holder.setCard(user)
    }

    override fun getItemCount(): Int {
        return users.size
    }



}