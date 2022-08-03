package com.fitterAPP.fitter.fragmentControllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieAnimationView
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.databinding.FragmentShowOthersCardDialogBinding
import com.fitterAPP.fitter.itemsAdapter.FitnessCardExercisesAdapter

/**
 * @author Daniel Satriano
 * @since 3/08/2022
 */
class ShowOthersCardDialog : DialogFragment() {

    private lateinit var binding : FragmentShowOthersCardDialogBinding
    private val args by navArgs<ShowOthersCardDialogArgs>()
    private var isChecked : Boolean = false

    /**
     * onCreate method which is used to set the dialog style. This mathod is paired with a WindowManager setting done in [onCreateView]
     * @author Daniel Satriano
     * @since 25/05/2022
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentShowOthersCardDialogBinding.inflate(inflater, container, false)
        //Set transparent status bar
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fitnessCard : FitnessCard = args.fitnesscard
        val lottieAnimator = binding.lottieBookmark

        val adapter = FitnessCardExercisesAdapter((activity as MainActivity), fitnessCard,false)
        binding.exerciseListRecycler.adapter = adapter
        adapter.notifyDataSetChanged()

        binding.backBt.setOnClickListener(onBackButton())
        lottieAnimator.setOnClickListener(bookmarkClickListener(fitnessCard, lottieAnimator))

    }

    private fun checkIfBookmarked(){
        TODO("Da implementare")
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
     * @author Daniel Satriano
     * @since 3/08/2022
     */
    //TODO("Salvare fitness cards")
    private fun bookmarkClickListener(fitnessCard: FitnessCard, lottieAnimator: LottieAnimationView): View.OnClickListener {
        return View.OnClickListener{
            if(!isChecked){
                lottieAnimator.speed = 1F
                lottieAnimator.playAnimation()
                isChecked = true
            }else{
                lottieAnimator.speed = -1.5F
                lottieAnimator.playAnimation()
                isChecked = false
            }
        }
    }


}