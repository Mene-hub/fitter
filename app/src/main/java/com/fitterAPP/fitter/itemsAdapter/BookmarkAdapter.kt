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

/**
 * Adapter which manages how the cards are shown inside the recycler view and the onClickListener
 * @author Daniel Satriano
 * @since 7/08/2022
 */
class BookmarkAdapter(private val context : Context, private val bookmarks : MutableList<BookmarkCard>) : RecyclerView.Adapter<BookmarkAdapter.Holder>() {

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val name : TextView = itemView.findViewById(R.id.bookmark_CardName)
        private val duration : TextView = itemView.findViewById(R.id.bookmark_CardDuration)
        private val numberOfExercise : TextView = itemView.findViewById(R.id.bookmark_CardExerciseCount)
        private val image : ImageView = itemView.findViewById(R.id.bookmark_background)

        fun setCard(card : BookmarkCard, context: Context){
            name.text = card.name
            duration.text = context.getString(R.string.time).plus(": " + card.timeDuration.toString()).plus(" min")

            if(card.exercises != null)
                numberOfExercise.text = card.exercises!!.size.toString().plus(" ").plus(context.getString(R.string.exercises).lowercase())
            else {
                numberOfExercise.text = "0 ".plus(context.getString(R.string.exercises))
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
            image.setImageResource(id)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkAdapter.Holder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.bookmark_card, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: BookmarkAdapter.Holder, position: Int) {
        holder.setCard(bookmarks[position], context)
    }

    override fun getItemCount(): Int {
        return bookmarks.size
    }
}