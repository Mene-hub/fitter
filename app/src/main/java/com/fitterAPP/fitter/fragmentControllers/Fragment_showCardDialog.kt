package com.fitterAPP.fitter.fragmentControllers

import com.fitterAPP.fitter.R
import android.app.Dialog
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.*
import android.view.animation.Animation
import android.widget.*
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.classes.*
import com.fitterAPP.fitter.databases.StaticFitnessCardDatabase
import com.fitterAPP.fitter.databinding.FragmentShowCardDialogBinding
import com.fitterAPP.fitter.itemsAdapter.FitnessCardExercisesAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class Fragment_showCardDialog() : DialogFragment() {

    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */

    private lateinit var binding : FragmentShowCardDialogBinding
    private val args by navArgs<Fragment_showCardDialogArgs>()
    private lateinit var newFitnessCard : FitnessCard
    private lateinit var adapter : FitnessCardExercisesAdapter
    private var monthlyRecap : MonthlyRecap? = null
    private var swiped : Boolean = false    //Used as a check to see if the user swiped (flagged as done) an exercise.

    /**
     * onCreate method which is used to set the dialog style. This method is paired with a WindowManager setting done in [onCreateView]
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
        monthlyRecap = args.monthlyRecap

        binding.backBt.setOnClickListener { findNavController().navigateUp() }

        val recycle : RecyclerView = binding.exercisesListRV

        if(newFitnessCard.exercises == null){ newFitnessCard.exercises = ArrayList() }

        //adapter for the exercises
        adapter = FitnessCardExercisesAdapter((activity as MainActivity),newFitnessCard,false, monthlyRecap)
        recycle.adapter = adapter

        //Swipe manager
        val swipeGesture = object : SwipeGesture.SwipeGestureRight(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.RIGHT -> {
                        swiped = true
                        showAlertDialog(viewHolder)
                    }
                }
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeGesture)
        itemTouchHelper.attachToRecyclerView(recycle)

        //setting the card properties
        binding.CardNameTV.text = newFitnessCard.name
        binding.DescriptionTV.text = newFitnessCard.description
        val text = newFitnessCard.timeDuration.toString() +" "+ getString(R.string.minutes)
        binding.TimeDurationTV.text = text

        //setting the image cover
        val id: Int = CardsCover.getResource(newFitnessCard.imageCover)

        binding.CardBgImageIV.setImageResource(id)

        //open edith view for card
        val edithBtn : FloatingActionButton = binding.edithCardView

        screenHeightAdjustment()

        edithBtn.setOnClickListener {
            if(swiped){
                showModifyAlertDialog()
            }else{
                val action : NavDirections = Fragment_showCardDialogDirections.actionFragmentShowCardDialogToModifyCard(newFitnessCard)
                findNavController().navigate(action)
            }

        }

        //gat database update
        StaticFitnessCardDatabase.setFitnessCardValueListener(
            StaticFitnessCardDatabase.database.getReference(getString(R.string.FitnessCardsReference)),
            Athlete.UID,newFitnessCard,
            cardValueEventListener()
        )

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun showModifyAlertDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext(),  R.style.ThemeOverlay_App_MaterialAlertDialog)
        builder.setTitle(getString(R.string.warning))
        builder.setMessage(getString(R.string.warning_message_modify_alert))
            .setPositiveButton("OK") { _, _ ->
                val action : NavDirections = Fragment_showCardDialogDirections.actionFragmentShowCardDialogToModifyCard(newFitnessCard)
                findNavController().navigate(action)
            }.setNegativeButton(getString(R.string.Cancel)) { _, _ -> }.setOnDismissListener {}
            .show()
    }


    /**
     * @author Claudio Menegotto
     * @since 1/08/2022
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

    private fun cardValueEventListener(): ValueEventListener {
        return object :  ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    newFitnessCard = snapshot.getValue(FitnessCard::class.java)!!

                    val id: Int = CardsCover.getResource(newFitnessCard.imageCover)

                    binding.CardBgImageIV.setImageResource(id)

                    binding.CardNameTV.text = newFitnessCard.name
                    binding.DescriptionTV.text = newFitnessCard.description
                    try {
                        binding.TimeDurationTV.text =
                            newFitnessCard.timeDuration.toString().plus(" ")
                                .plus(getString(R.string.minutes))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    if (newFitnessCard.exercises != null) {
                        //Hypothetically the new item will always be the last one in the list, unless we do some swapping manually.
                        adapter.notifyItemInserted(newFitnessCard.exercises!!.size)
                    }

                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),error.message, Toast.LENGTH_LONG).show()
            }

        }
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

    private fun showAlertDialog(viewHolder : RecyclerView.ViewHolder){
        // set the custom layout
        val customLayout: View = layoutInflater.inflate(R.layout.improvement_dialog_showfitnesscard, null)
        var cancelCheck = false   //Used to check if the user pressed on .setNegativeButton, cause there was a problem with the reset of the swipe since setOnDismiss gets called right after in any case

        //Check if the exercise is a warmup exercise or a normal exercise, and then set the correct hint for both cases
        val editText = customLayout.findViewById<TextInputEditText>(R.id.et_improvement)
        if(newFitnessCard.exercises!![viewHolder.absoluteAdapterPosition].type == ExerciseType.warmup){
            editText.hint = getString(R.string.minutesSpent)
        }else{
            //If its not warmup then it should be something that uses kg, so I change the hint accordingly
            editText.hint = getString(R.string.kilogramsUsed)
        }
        // Create an alert builder
        val builder = MaterialAlertDialogBuilder(requireContext(),  R.style.ThemeOverlay_App_MaterialAlertDialog)
        builder.setView(customLayout)
        // add a button
        builder
            .setPositiveButton("OK") { _, _ -> // send data from the
                // AlertDialog to the Activity
                val recap = editText.text.toString()
                if(recap.isNotBlank() && recap.isNotEmpty()){
                    adapter.addRecap(viewHolder.absoluteAdapterPosition, Integer.parseInt(recap))
                }
                cancelCheck = true
            }.setNegativeButton(getString(R.string.Cancel)) { _, _ ->
                adapter.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                cancelCheck = true

            }.setOnDismissListener {
                if(!cancelCheck)
                    adapter.notifyItemChanged(viewHolder.absoluteAdapterPosition)
            }
            .show()
    }

    fun showExerciseinformation( ex:Exercise ){
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