package com.fitterAPP.fitter.fragmentControllers

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.*
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.fitterAPP.fitter.databinding.FragmentViewOthersProfileBinding
import com.fitterAPP.fitter.itemsAdapter.FitnessCardFindUserAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.Query
import com.squareup.picasso.Picasso
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class Fragment_ViewOthersProfile : DialogFragment() {

    private lateinit var binding : FragmentViewOthersProfileBinding
    private lateinit var adapter : FitnessCardFindUserAdapter
    private var cardList : MutableList<FitnessCard> = mutableListOf()
    private val args by navArgs<Fragment_ViewOthersProfileArgs>()
    private lateinit var shimmerFrameLayout : ShimmerFrameLayout

    /**
     * onCreate method which is used to set the dialog style. This mathod is paired with a WindowManager setting done in [onCreateView]
     * @author Daniel Satriano
     * @since 25/05/2022
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)
        retrieveUserCards(args.bundleAthlete.UID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentViewOthersProfileBinding.inflate(inflater,container,false)

        shimmerFrameLayout = binding.RVCardsShimmer
        shimmerFrameLayout.startShimmer()

        //Set transparent status bar
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        //Get FitnessCard by bundle passed via navigation controller in [FitnessCardAdapter.kt] (the bundle is also set in fragment_navigation.xml)
        val athlete : Athlete = args.bundleAthlete

        //Setting up top bar information, name, profile picture and bio
        binding.TVUsername.text = athlete.username
        binding.TVBiography.text = athlete.profileBio
        if(athlete.profilePic != "") {
            val image = binding.profilepicIV
            Picasso.get()
                .load(athlete.profilePic)
                .resize(100, 100)
                .centerCrop()
                .into(image)
        }

        when(Locale.getDefault().language){
            "en" ->{
                binding.TVUsernameCards.text = athlete.username.plus("'s cards")
            }
            "it" ->{
                binding.TVUsernameCards.text = "Schede di ".plus(athlete.username)
            }
            else ->{
                binding.TVUsernameCards.text = athlete.username.plus("'s cards")
            }
        }

        adapter = context?.let { FitnessCardFindUserAdapter((activity as MainActivity), cardList, athlete.UID) }!!
        binding.RVCards.adapter = adapter


        return binding.root
    }

    /**
     * Method to retrieve targeted user fitness cards, this method doesn't utilize listeners
     * @author Daniel Satriano
     * @since 13/06/2022
     */
    private fun retrieveUserCards(UID : String) {
        val reference = StaticFitnessCardDatabase.database.getReference(getString(R.string.FitnessCardsReference)).child(UID)
        val usernameOrdered : Query  = reference.orderByKey()
        usernameOrdered.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result
                result?.let { snapShot ->
                    Log.d("Count", snapShot.childrenCount.toString())
                    for (cardSnap in snapShot.children) {
                        val item = cardSnap.getValue(FitnessCard::class.java)!!
                        cardList.add(item)
                        adapter.notifyItemInserted(cardList.indexOf(item))
                        Log.d("CountCard", cardList.size.toString())
                    }
                    //Stop shimmer effect, make shimmer template gone and RecyclerView visible
                    binding.RVCardsShimmer.visibility = View.GONE
                    binding.RVCardsShimmer.stopShimmer()
                    binding.RVCards.visibility = View.VISIBLE
                }

            }
        }
    }

    fun showExerciseinformation( ex: Exercise){
        // Create an alert builder
        val builder = MaterialAlertDialogBuilder(requireContext(),  R.style.ThemeOverlay_App_MaterialAlertDialog)
        // set the custom layout

        val customLayout: View = layoutInflater.inflate(R.layout.dialog_open_exercise, null)


        val imageList : ImageSlider = customLayout.findViewById(R.id.exercise_image_slider)
        val list : MutableList<SlideModel> = ArrayList<SlideModel>()

        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var imageUri : MutableList<String> ?

        executor.execute {
            executor.run() {
                imageUri = ExerciseQueryHelper.getImageFromExercise(ex.wgerBaseId!!)
            }

            handler.post {

                if (imageUri != null && imageUri?.size!! > 0) {

                    for (i in 1..imageUri?.size!!)
                        list.add(SlideModel(imageUri?.get(i - 1)))

                    imageList.setImageList(list, ScaleTypes.CENTER_INSIDE)
                    customLayout.findViewById<ImageView>(R.id.placeHolder).isGone = true
                } else
                    customLayout.findViewById<ImageView>(R.id.placeHolder).isGone = false
            }
        }

        builder.setView(customLayout)

        val exName : TextView = customLayout.findViewById(R.id.ExName_TV)
        exName.setText(ex.exerciseName)

        val exDescription : TextView = customLayout.findViewById(R.id.exDescription_TV)
        exDescription.setText(ex.description)

        // add a button
        builder

            .setPositiveButton("OK") { _, _ -> // send data from the
            }

            .setOnDismissListener {

            }
            .show()
    }
}