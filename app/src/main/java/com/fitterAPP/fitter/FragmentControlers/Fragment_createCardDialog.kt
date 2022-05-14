package com.fitterAPP.fitter.FragmentControlers

import android.R
import com.fitterAPP.fitter.R.layout
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.fitterAPP.fitter.Classes.FitnessCard
import com.fitterAPP.fitter.databinding.FragmentCreateCardDialogBinding
import com.fitterAPP.fitter.databinding.FragmentMyFitnessCardsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class Fragment_createCardDialog : DialogFragment() {

    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */

    private lateinit var binding : FragmentCreateCardDialogBinding
    private var newFitnessCard : FitnessCard ? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateCardDialogBinding.inflate(inflater, container, false)

        showAlertDialogButtonClicked()

        binding.backBt.setOnClickListener {
            activity?.onBackPressed()
        }

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

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val a: Animation = object : Animation() {}
        a.duration = 0
        return a
    }

    fun showAlertDialogButtonClicked() {

        // Create an alert builder
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle("Card Name")

        // set the custom layout
        val customLayout: View = layoutInflater.inflate( com.fitterAPP.fitter.R.layout.dialog_input_text, null)
        builder.setView(customLayout)

        // add a button
        builder
            .setPositiveButton(
                "OK",
                DialogInterface.OnClickListener { dialog, which -> // send data from the
                    // AlertDialog to the Activity
                    val editText = customLayout.findViewById<EditText>(com.fitterAPP.fitter.R.id.CardName_ET)
                    newFitnessCard = FitnessCard()
                    newFitnessCard?.name = editText.text.toString()
                })

            .setNegativeButton(
                "BACK",
                DialogInterface.OnClickListener{dialog, which ->
                }
            )

            .setOnDismissListener {
                if(newFitnessCard == null)
                    activity?.onBackPressed()
                else
                    if(newFitnessCard?.name?.replace(" ","")?.length == 0)
                    {
                        Toast.makeText(activity, "insert a valid name", Toast.LENGTH_SHORT).show()
                        activity?.onBackPressed()
                    }
            }

        // create and show
        // the alert dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}