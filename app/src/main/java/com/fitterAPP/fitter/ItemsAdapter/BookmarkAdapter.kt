package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.BookmarkCard
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.fragmentControllers.BookmarkCards
import com.fitterAPP.fitter.fragmentControllers.BookmarkCardsDirections
import com.fitterAPP.fitter.fragmentControllers.Fragment_ViewOthersProfileDirections

class BookmarkAdapter(private val context : Context, private val bookmarks : MutableList<BookmarkCard>) : RecyclerView.Adapter<BookmarkAdapter.Holder>() {

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val cardName : TextView = itemView.findViewById(R.id.CardName_TV)
        private val cardDuration : TextView = itemView.findViewById(R.id.TimeDuration_TV)
        private val cardExercises : TextView = itemView.findViewById(R.id.ExerciseCount_TV)
        private val bgImage : ImageView = itemView.findViewById(R.id.CardBgImage_IV)

        fun setCard(card : BookmarkCard, context: Context){
            cardName.text = card.name
            cardDuration.text = "Duration: " + card.timeDuration.toString() + " min"

            if(card.exercises != null)
                cardExercises.text = card.exercises?.count().toString() + " exercise"
            else {
                cardExercises.text = "0 exercise"
            }

            itemView.setOnClickListener {
                val action : NavDirections = BookmarkCardsDirections.actionBookmarkCardsToShowOthersCardDialog(card.toFitnessCard(), card.ownerUID, true)
                val containerView : FragmentContainerView = (context as MainActivity).findViewById(R.id.FragmentContainer)
                Navigation.findNavController(containerView).navigate(action)
            }

            val id: Int = context.resources.getIdentifier(
                "com.fitterAPP.fitter:drawable/" + card.imageCover.toString(),
                null,
                null
            )

            bgImage.setImageResource(id)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkAdapter.Holder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_fitnesscard, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: BookmarkAdapter.Holder, position: Int) {
        holder.setCard(bookmarks[position], context)
    }

    override fun getItemCount(): Int {
        return bookmarks.size
    }
}