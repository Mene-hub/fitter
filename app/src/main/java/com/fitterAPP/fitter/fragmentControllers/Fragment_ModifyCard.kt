package com.fitterAPP.fitter.fragmentControllers

import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.classes.Exercise
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.itemsAdapter.FitnessCardExercisesAdapter
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.CardsCover
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.fitterAPP.fitter.databinding.FragmentModifyCardBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class ModifyCard() : DialogFragment() {
    private lateinit var fitnessCard: FitnessCard
    private val args by navArgs<ModifyCardArgs>()
    private lateinit var binding : FragmentModifyCardBinding
    lateinit var recycle : RecyclerView

    /**
     * onCreate method which is used to set the dialog style. This mathod is paired with a WindowManager setting done in [onCreateView]
     * @author Daniel Satriano
     * @author Claudio Menegotto
     * @since 1/06/2022
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentModifyCardBinding.inflate(inflater, container, false)

        //Set transparent status bar
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        //Get FitnessCard by bundle passed via navigation controller in [FitnessCardAdapter.kt] (the bundle is also set in fragment_navigation.xml
        fitnessCard = args.cardBundle

        binding.backBt.setOnClickListener {
            activity?.onBackPressed()
        }

        var screenHeight = 0

        //get screen height
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

        //set the image height
        val params = FrameLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT, screenHeight)

        binding.Header.layoutParams = params


        //exercises adapter
        recycle = binding.exercisesListRV

        if(fitnessCard.exercises != null && fitnessCard.exercises?.size!! > 0){
            val adapter = FitnessCardExercisesAdapter((activity as MainActivity),fitnessCard,fitnessCard.exercises!!, true)
            recycle.adapter = adapter
        }

        //binding the card properties
        val cardName : TextView = binding.CardNameTV
        val cardDuration : TextView = binding.TimeDurationTV
        val cardDescription: TextView = binding.DescriptionTV

        //edit image cover on click
        val editCover : ImageView = binding.EditCoverIV
        editCover.setOnClickListener {
            val modalBottomSheet = ImageSelector("Card image cover", fitnessCard)
            modalBottomSheet.show(activity?.supportFragmentManager!!, profileMenu.TAG)
        }

        val bgimage : ImageView = binding.CardBgImageIV

        //setting the card cover
        val id: Int? = context?.resources?.getIdentifier("com.fitterAPP.fitter:drawable/" + fitnessCard.imageCover.toString(), null, null )

        bgimage.setImageResource(id!!)

        //setting the card properties
        cardName.text = fitnessCard.name
        cardDescription.text = fitnessCard.description
        cardDuration.text = fitnessCard.timeDuration.toString() + " minutes"

        //nre Exercise button clicked
        val newExerciseBT : ExtendedFloatingActionButton = binding.newExerciseBT
        newExerciseBT.setOnClickListener {

            if(fitnessCard.exercises == null)
                fitnessCard.exercises = ArrayList()

            //fitnessCard.exercises?.add(Exercise())

            val action : NavDirections = ModifyCardDirections.actionModifyCardToSelectExerciseGroup(fitnessCard, fitnessCard.exercises?.size!!)
            findNavController().navigate(action)
        }

        //edit card name
        binding.editCardName.setOnClickListener {
            val modalBottomSheet = StringEditMenu("Card name", fitnessCard.name!!, fitnessCard)
            modalBottomSheet.show(activity?.supportFragmentManager!!, profileMenu.TAG)
        }

        //edit card description
        binding.editCardDescription.setOnClickListener {
            val modalBottomSheet = StringEditMenu("Card description", fitnessCard.description!!, fitnessCard)
            modalBottomSheet.show(activity?.supportFragmentManager!!, profileMenu.TAG)
        }

        //get database update
        StaticFitnessCardDatabase.setFitnessCardChildListener(StaticFitnessCardDatabase.database.getReference(getString(R.string.FitnessCardsReference)), Athlete.UID, cardChildEventListener())

        return binding.root
    }

    private fun cardChildEventListener(): ChildEventListener {
        return object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                //settings new properties from the database
                val cardName : TextView = binding.CardNameTV
                val cardDuration : TextView = binding.TimeDurationTV
                val cardDescription: TextView = binding.DescriptionTV
                val bgimage : ImageView = binding.CardBgImageIV

                val id: Int? = CardsCover.getResource(fitnessCard.imageCover)

                bgimage.setImageResource(id!!)

                cardName.text = fitnessCard.name
                cardDescription.text = fitnessCard.description
                cardDuration.text = fitnessCard.timeDuration.toString() + " s"

                if(fitnessCard.exercises != null && fitnessCard.exercises?.size!! > 0){
                    val adapter = context?.let { FitnessCardExercisesAdapter((activity as MainActivity),fitnessCard,fitnessCard.exercises!!, true) }!!
                    recycle.adapter = adapter
                }

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            }
        }
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val a: Animation = object : Animation() {}
        a.duration = 0
        return a
    }

}