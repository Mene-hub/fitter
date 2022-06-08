package com.fitterAPP.fitter.fragmentControllers

import android.app.Dialog
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.FitnessCard
import com.fitterAPP.fitter.databinding.FragmentNewExercieFormDialogBinding
import com.fitterAPP.fitter.databinding.FragmentShowCardDialogBinding


class newExercieFormDialog : DialogFragment() {

    private lateinit var binding : FragmentNewExercieFormDialogBinding
    private val args by navArgs<newExercieFormDialogArgs>()
    private lateinit var fitnessCard : FitnessCard

    /**
     * onCreate method which is used to set the dialog style. This mathod is paired with a WindowManager setting done in [onCreateView]
     * @author Daniel Satriano
     * @since 1/06/2022
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Fitter_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Set transparent status bar
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


        binding = FragmentNewExercieFormDialogBinding.inflate(inflater, container, false)

        //Get FitnessCard by bundle passed via navigation controller in [FitnessCardAdapter.kt] (the bundle is also set in fragment_navigation.xml
        fitnessCard = args.fitnessCard

        var screenHeight = 0

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

        val params = FrameLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT, screenHeight)

        binding.Header.layoutParams = params

        binding.backBt.setOnClickListener {
            findNavController().navigateUp()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}