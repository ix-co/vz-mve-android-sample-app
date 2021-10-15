package com.example.containerapp

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import co.ix.vzmve.sdk.MultiViewExperienceSdk
import co.ix.vzmve.sdk.MultiViewSdkModule

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_first).setOnClickListener { _ ->
            MultiViewExperienceSdk.init(this, "58283", "NFL")
            MultiViewExperienceSdk.launchMultiViewExperience()
        }
    }
}