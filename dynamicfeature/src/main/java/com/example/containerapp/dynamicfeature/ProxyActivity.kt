package com.example.containerapp.dynamicfeature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.ix.vzmve.sdk.MultiViewExperienceSdk

class ProxyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MultiViewExperienceSdk.init(this, "58283", "NFL")
        MultiViewExperienceSdk.launchMultiViewExperience()
    }
}