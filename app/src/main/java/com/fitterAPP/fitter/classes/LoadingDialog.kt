package com.fitterAPP.fitter.classes

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import com.fitterAPP.fitter.R

class LoadingDialog(private var activity : Activity) {
    private lateinit var dialog : AlertDialog

    fun startLoadingAnimation(){
        var builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        var inflater : LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(true)

        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }

}