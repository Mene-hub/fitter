package com.fitterAPP.fitter.fragmentControllers

import com.fitterAPP.fitter.R
import android.app.Dialog
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.classes.Athlete
import com.fitterAPP.fitter.classes.CardsCover
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.classes.SwipeGesture
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.fitterAPP.fitter.databinding.FragmentShowCardDialogBinding
import com.fitterAPP.fitter.itemsAdapter.FitnessCardExercisesAdapter
import kotlin.concurrent.thread


class Fragment_showCardDialog() : DialogFragment() {

    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */


    private lateinit var binding : FragmentShowCardDialogBinding
    private val args by navArgs<Fragment_showCardDialogArgs>()
    private lateinit var newFitnessCard : FitnessCard

    /**
     * onCreate method which is used to set the dialog style. This mathod is paired with a WindowManager setting done in [onCreateView]
     * @author Daniel Satriano
     * @author Menegotto Claudio
     * @since 25/05/2022
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShowCardDialogBinding.inflate(inflater, container, false)

        //Set transparent status bar
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


        //Get FitnessCard by bundle passed via navigation controller in [FitnessCardAdapter.kt] (the bundle is also set in fragment_navigation.xml
        newFitnessCard = args.cardBundle

        binding.backBt.setOnClickListener {
            findNavController().navigateUp()
        }

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

        val recycle : RecyclerView = binding.exercisesListRV

        //adapter for the exercises
        if(newFitnessCard.exercises != null && newFitnessCard.exercises?.size!! > 0){
            val adapter = FitnessCardExercisesAdapter((activity as MainActivity), newFitnessCard,  newFitnessCard.exercises!!,false)
            recycle.adapter = adapter

            //Inserisco il gestore dello SWIPE della listview
            val swipeGesture = object : SwipeGesture(requireContext()){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    when(direction){
                        ItemTouchHelper.LEFT -> {
                            adapter.deleteItem(requireContext(), viewHolder.absoluteAdapterPosition)

                        }
                        ItemTouchHelper.RIGHT -> {
                            adapter.addRecap(viewHolder.absoluteAdapterPosition)
                        }
                    }
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeGesture)
            itemTouchHelper.attachToRecyclerView(recycle)

        }

        //binding the card properties
        val cardName : TextView = binding.CardNameTV
        val cardDuration : TextView = binding.TimeDurationTV
        val cardDescription: TextView = binding.DescriptionTV

        //setting the card properties
        cardName.text = newFitnessCard.name
        cardDescription.text = newFitnessCard.description
        val text = newFitnessCard.timeDuration.toString() +" "+ getString(R.string.minutes)
        cardDuration.text = text
        val bgimage : ImageView = binding.CardBgImageIV

        //setting the image cover
        val id: Int = CardsCover.getResource(newFitnessCard.imageCover)

        bgimage.setImageResource(id)

        // Inflate the layout for this fragment
        return binding.root
    }

    /** The system calls this only when creating the layout in a dialog. */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.

        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }



    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation {
        val a: Animation = object : Animation() {}
        a.duration = 0
        return a
    }

}