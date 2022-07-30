package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.fragmentControllers.MyFitnessCards
import com.fitterAPP.fitter.fragmentControllers.MyFitnessCardsDirections

class FitnessCardAdapter (val context2: Context, private val Cards:MutableList<FitnessCard>, val fitnessCards: MyFitnessCards?) : RecyclerView.Adapter<FitnessCardAdapter.Holder>() {

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val cardName : TextView = itemView.findViewById(R.id.CardName_TV)
        private val cardDuration : TextView = itemView.findViewById(R.id.TimeDuration_TV)
        private val cardExercises : TextView = itemView.findViewById(R.id.ExerciseCount_TV)
        private val bgImage : ImageView = itemView.findViewById(R.id.CardBgImage_IV)
        private val cardView : CardView = itemView.findViewById(R.id.ItemCard_CardView)

        fun setCard(Card:FitnessCard, context: Context){
            cardName.text = Card.name
            cardDuration.text = "Duration: " + Card.timeDuration.toString() + " min"


            if(Card.exercises != null)
                cardExercises.text = Card.exercises?.count().toString() + " exercise"
            else {
                cardExercises.text = "0 exercise"
            }

            if(Card.key == "addCard"){
                if(fitnessCards != null) {
                    cardView.cardElevation = 0F
                    bgImage.scaleType = ImageView.ScaleType.FIT_CENTER
                    val padding = bgImage.resources.getDimensionPixelOffset(R.dimen.button_add_card_padding)
                    bgImage.setPadding(padding, padding, padding, padding)
                    cardExercises.text = ""
                    itemView.findViewById<LottieAnimationView>(R.id.lottie_add).isGone = false
                    itemView.setOnClickListener {
                        itemView.findViewById<LottieAnimationView>(R.id.lottie_add).playAnimation()
                        fitnessCards.showAlertDialogFitnessCard()

                    }
                }
            }else{
                itemView.setOnClickListener {
                    val action : NavDirections = MyFitnessCardsDirections.actionMyFitnessCardsToFragmentShowCardDialog(Card)
                    it.findNavController().navigate(action)
                }
            }

            val id: Int = context.resources.getIdentifier( "com.fitterAPP.fitter:drawable/" + Card.imageCover.toString(),null,null)
            bgImage.setImageResource(id)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(context2).inflate(R.layout.item_fitnesscard, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val Card: FitnessCard = Cards[position]
        holder.setCard(Card, context2)
    }

    override fun getItemCount(): Int {
        return Cards.size
    }

}


