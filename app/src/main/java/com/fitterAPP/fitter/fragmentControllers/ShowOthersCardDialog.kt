package com.fitterAPP.fitter.fragmentControllers

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieAnimationView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.*
import com.fitterAPP.fitter.databases.StaticBookmarkDatabase
import com.fitterAPP.fitter.databinding.FragmentShowOthersCardDialogBinding
import com.fitterAPP.fitter.itemsAdapter.FitnessCardExercisesAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author Daniel Satriano
 * @since 3/08/2022
 */
class ShowOthersCardDialog : DialogFragment() {

    private lateinit var binding : FragmentShowOthersCardDialogBinding
    private val args by navArgs<ShowOthersCardDialogArgs>()
    private var isChecked : Boolean = false //used to check if the card is already bookmarked

    private var isCheckedFlag : Boolean = false //used for lottie animation
    private lateinit var databaseRef : DatabaseReference


    /**
     * onCreate method which is used to set the dialog style. This method is paired with a WindowManager setting done in [onCreateView]
     * @author Daniel Satriano
     * @since 25/05/2022
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentShowOthersCardDialogBinding.inflate(inflater, container, false)
        //Set transparent status bar
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fitnessCard : FitnessCard = args.fitnesscard
        isChecked = args.isChecked
        val lottieAnimator = binding.lottieBookmark

        val adapter = FitnessCardExercisesAdapter((activity as MainActivity), fitnessCard,false, null,false )
        val bookmark = BookmarkCard(fitnessCard, args.ownerUID)

        binding.CardNameTV.text = fitnessCard.name
        binding.DescriptionTV.text = fitnessCard.description
        binding.TimeDurationTV.text = fitnessCard.timeDuration.toString().plus(" min")
        binding.CardBgImageIV.setImageResource(CardsCover.getResource(fitnessCard.imageCover))
        databaseRef =  StaticBookmarkDatabase.database.getReference(getString(R.string.BookmarkReference))

        screenHeightAdjustment()    //Sets correct height
        checkIfBookmarked(lottieAnimator)
        binding.exerciseListRecycler.adapter = adapter

        binding.backBt.setOnClickListener(onBackButton())
        lottieAnimator.setOnClickListener(bookmarkClickListener(lottieAnimator, bookmark))



    }

    /**
     * @author Claudio Menegotto
     * @since 9/08/2022
     */
    private fun screenHeightAdjustment(){
        var screenHeight = 0
        //getting the screen height in px
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val windowMetrics = activity?.windowManager?.currentWindowMetrics
                val display: Rect = windowMetrics?.bounds!!
                screenHeight = display.height()/3
            } catch (e: NoSuchMethodError) {}

        } else {
            val metrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
            screenHeight = metrics.heightPixels/3
        }

        //setting the height
        val params = FrameLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT, screenHeight)
        binding.Header.layoutParams = params
    }

    /**
     * This method is used to set if the bookmark icon needs to be activated by default or not. whatever or not the user have already bookmarked the card is viewing
     * @author Daniel Satriano
     * @since 3/08/2022
     * @param lottieAnimator The graphical object (View) which is defined inside this fragment layout.
     */
    private fun checkIfBookmarked(lottieAnimator : LottieAnimationView){
        if(isChecked){
            isCheckedFlag = true
            lottieAnimator.frame = 50
        }
    }

    /**
     * Event for when the user presses the back arrow
     * @author Daniel Satriano
     * @since 3/08/2022
     */
    private fun onBackButton(): View.OnClickListener {
        return View.OnClickListener {
            findNavController().navigateUp()
        }
    }

    /**
     * Method that manages all the bookmark add and remove options. This method is connected to the database
     * @author Daniel Satriano
     * @since 3/08/2022
     * @param lottieAnimator The graphical object (View) which is defined inside this fragment layout.
     * @param bookmark The current card that the user is viewing
     */
    private fun bookmarkClickListener(lottieAnimator: LottieAnimationView, bookmark: BookmarkCard): View.OnClickListener {
        return View.OnClickListener{
            if(!isCheckedFlag){

                BookmarkCard.bookmarkList.add(bookmark)
                StaticBookmarkDatabase.updateBookmarkList(databaseRef, Athlete.UID, BookmarkCard.bookmarkList)
                lottieAnimator.speed = 1F
                lottieAnimator.playAnimation()
                isCheckedFlag = true
            }else{

                val index = BookmarkCard.bookmarkList.indexOf(BookmarkCard.bookmarkList.find { it.key == bookmark.key })

                BookmarkCard.bookmarkList.removeAt(index)
                StaticBookmarkDatabase.updateBookmarkList(databaseRef, Athlete.UID, BookmarkCard.bookmarkList)

                lottieAnimator.speed = -1.5F
                lottieAnimator.playAnimation()
                isCheckedFlag = false
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