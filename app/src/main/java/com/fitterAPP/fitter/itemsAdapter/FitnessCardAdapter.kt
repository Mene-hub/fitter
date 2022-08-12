package com.fitterAPP.fitter.itemsAdapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.classes.MonthlyRecap
import com.fitterAPP.fitter.databases.StaticRecapDatabase
import com.fitterAPP.fitter.fragmentControllers.MyFitnessCards
import com.fitterAPP.fitter.fragmentControllers.MyFitnessCardsDirections
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate


class FitnessCardAdapter (val context2: Context, private val Cards:MutableList<FitnessCard>, val fitnessCards: MyFitnessCards?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        private const val TAG = "FITNESS_CARDS"
        private const val VIEW_TYPE_CONTENT = 0
        private const val VIEW_TYPE_AD = 1
    }

    inner class HolderCards(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val cardName : TextView = itemView.findViewById(R.id.CardName_TV)
        private val cardDuration : TextView = itemView.findViewById(R.id.TimeDuration_TV)
        private val cardExercises : TextView = itemView.findViewById(R.id.ExerciseCount_TV)
        private val bgImage : ImageView = itemView.findViewById(R.id.CardBgImage_IV)
        private val cardView : CardView = itemView.findViewById(R.id.ItemCard_CardView)

        fun setCard(Card:FitnessCard, context: Context){
            cardName.text = Card.name
            cardDuration.text = context.getString(R.string.duration).plus(": " + Card.timeDuration.toString() + " min")


            if(Card.exercises != null)
                cardExercises.text = Card.exercises?.count().toString().plus(" "+context.getString(R.string.exercises))
            else {
                cardExercises.text = "0".plus(context.getString(R.string.exercises))
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

                    StaticRecapDatabase.setSingleListenerForMonth(
                        StaticRecapDatabase.database.getReference(context.getString(R.string.RecapReference)),
                        Athlete.UID,
                        Card.key,
                        LocalDate.now().month.toString(),
                        currentMonthRecapListener(Card)
                    )

                }
            }

            val id: Int = context.resources.getIdentifier( "com.fitterAPP.fitter:drawable/" + Card.imageCover.toString(),null,null)
            bgImage.setImageResource(id)

        }
    }

    inner class HolderNativeAd(itemView: View): RecyclerView.ViewHolder(itemView){

        val app_ad_background : ImageView = itemView.findViewById(R.id.ad_icon)
        val ad_headline : TextView = itemView.findViewById(R.id.ad_headline)
        val ad_description : TextView = itemView.findViewById(R.id.ad_description)
        val ad_price : TextView = itemView.findViewById(R.id.ad_price)
        val ad_store : TextView = itemView.findViewById(R.id.ad_store)
        val call_to_action : CardView = itemView.findViewById(R.id.ad_call_to_action)
        val ad_advertiser : TextView = itemView.findViewById(R.id.ad_advertiser)
        val nativeAdView : NativeAdView = itemView.findViewById(R.id.nativeAdView)

        fun createAD(context : Context){
            val adLoader  = AdLoader.Builder(context, context.getString(R.string.native_ad_id_test))
                .forNativeAd { nativeAd ->
                    Log.d(TAG, "onNativeAdLoaded: ")
                    displayNativeAd(this@HolderNativeAd, nativeAd)
                }.withNativeAdOptions(NativeAdOptions.Builder().build()).build()
            adLoader.loadAd(AdRequest.Builder().build())
        }

    }

    private fun currentMonthRecapListener(Card: FitnessCard): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(MonthlyRecap::class.java)
                val action : NavDirections = MyFitnessCardsDirections.actionMyFitnessCardsToFragmentShowCardDialog(Card, item)
                val containerView : FragmentContainerView = (context2 as MainActivity).findViewById(R.id.FragmentContainer)
                Navigation.findNavController(containerView).navigate(action)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context2,error.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun displayNativeAd(holderNativeAd: FitnessCardAdapter.HolderNativeAd, nativeAd: NativeAd) {
        /* Get Ad assets from the NativeAd Object  */
        val headline = nativeAd.headline
        val body = nativeAd.body
        val background = nativeAd.icon
        val callToAction = nativeAd.callToAction
        val price = nativeAd.price
        val store = nativeAd.store
        val advertiser = nativeAd.advertiser

        /* Some assets aren't guaranteed to be in every NativeAd, so we need to check before displaying them   */
        if(headline == null){
            //no content, hide view
            holderNativeAd.ad_headline.visibility = View.INVISIBLE
        }else{
            //have content
            holderNativeAd.ad_headline.visibility = View.VISIBLE
            //set data
            holderNativeAd.ad_headline.text = headline
        }

        if(body == null) {
            //no content, hide view
            holderNativeAd.ad_description.visibility = View.INVISIBLE
        }else{
            //have content
            holderNativeAd.ad_description.visibility = View.VISIBLE
            //set data
            holderNativeAd.ad_description.text = body
        }

        if (background == null) {
            //no content, hide view
            holderNativeAd.app_ad_background.visibility = View.GONE
        }else{
            //have content
            holderNativeAd.app_ad_background.visibility = View.VISIBLE
            holderNativeAd.app_ad_background.setImageDrawable(nativeAd.images[0].drawable)
        }

        if (price == null) {
            //no content, hide view
            holderNativeAd.ad_price.visibility = View.INVISIBLE
        }else{ //have content
            holderNativeAd.ad_price.visibility = View.VISIBLE
            //set data
            holderNativeAd.ad_price.text = price
        }

        if(store == null) {
            //no content, hide view
            holderNativeAd.ad_store.visibility = View.INVISIBLE
        }else{
            //have content
            holderNativeAd.ad_store.visibility = View.VISIBLE
            //set data
            holderNativeAd.ad_store.text = store
        }

        if (advertiser == null) {
            //no content, hide view
            holderNativeAd.ad_advertiser.visibility = View.INVISIBLE
        }else{
            //have content
            holderNativeAd.ad_advertiser.visibility = View.VISIBLE
            //set data
            holderNativeAd.ad_advertiser.text = advertiser
        }

        if(callToAction != null) {
            //have content, handle click
            holderNativeAd.nativeAdView.callToActionView = holderNativeAd.call_to_action
        }

        holderNativeAd.nativeAdView.setNativeAd(nativeAd)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        if(viewType == VIEW_TYPE_CONTENT){
            view = LayoutInflater.from(context2).inflate(R.layout.item_fitnesscard, parent, false)
            return HolderCards(view)
        }else{
            view = LayoutInflater.from(context2).inflate(R.layout.native_ad_card, parent, false)
            return HolderNativeAd(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == VIEW_TYPE_CONTENT) {
            val model: FitnessCard = Cards[position]
            (holder as HolderCards).setCard(model, context2)
        } else if (getItemViewType(position) == VIEW_TYPE_AD) {
            (holder as HolderNativeAd).createAD(context2)

        }
    }


    override fun getItemViewType(position: Int): Int {
        //logic to display Native Ad between content
        if(position != 0) {
            return if (position % 2 == 0) {
                //after 2 items, show native ad
                VIEW_TYPE_AD
            } else {
                VIEW_TYPE_CONTENT
            }
        }
        return VIEW_TYPE_CONTENT
    }


    override fun getItemCount(): Int {
        return Cards.size
    }

}


