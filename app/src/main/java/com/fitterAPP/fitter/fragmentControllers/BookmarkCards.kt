package com.fitterAPP.fitter.fragmentControllers

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.fitterAPP.fitter.MainActivity
import com.fitterAPP.fitter.R
import com.fitterAPP.fitter.classes.BookmarkCard
import com.fitterAPP.fitter.classes.Exercise
import com.fitterAPP.fitter.classes.ExerciseQueryHelper
import com.fitterAPP.fitter.databinding.FragmentBookmarkCardsBinding
import com.fitterAPP.fitter.itemsAdapter.BookmarkAdapter
import com.fitterAPP.fitter.itemsAdapter.FitnessCardExercisesAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Fragment which manage the recycler view inside the layout
 * @author Daniel Satriano
 * @since 7/08/2022
 */
class BookmarkCards : Fragment() {

    private lateinit var binding : FragmentBookmarkCardsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentBookmarkCardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BookmarkAdapter((activity as MainActivity), BookmarkCard.bookmarkList)
        binding.RVBookmarkedCards.adapter = adapter
    }

}