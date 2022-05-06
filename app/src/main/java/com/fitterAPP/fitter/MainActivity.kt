package com.fitterAPP.fitter

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import com.google.android.material.bottomsheet.BottomSheetDialog


class MainActivity : AppCompatActivity() {

    private lateinit var ttprofile : LinearLayout;
    private lateinit var menuiv : ImageView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menuiv = findViewById(R.id.MenuBt)
        menuiv?.setOnClickListener {
            val modalBottomSheet = profileMenu()
            modalBottomSheet.show(supportFragmentManager, profileMenu.TAG)
        }
    }

    public fun showSeach(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.searchprofileFragmentContainer, findprofile() )
        transaction.commit()
    }
}