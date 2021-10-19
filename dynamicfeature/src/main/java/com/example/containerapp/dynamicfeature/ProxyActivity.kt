package com.example.containerapp.dynamicfeature

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.ix.vzmve.sdk.MultiViewExperienceSdk
import com.google.android.play.core.splitcompat.SplitCompat

class ProxyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MultiViewExperienceSdk.init(this, "58283", "NFL")
        MultiViewExperienceSdk.launchMultiViewExperience()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }
}