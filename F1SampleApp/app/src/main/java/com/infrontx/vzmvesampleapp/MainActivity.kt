package com.infrontx.vzmvesampleapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import co.ix.vzmve.sdk.MultiViewExperienceSdk
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

private const val EVENT_KEY = "58283"
private const val PRODUCT_NAME = "F1"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MultiViewExperienceSdk.init(this, EVENT_KEY, PRODUCT_NAME)

        run_vzmve_button.setOnClickListener {
            MultiViewExperienceSdk.launchMultiViewExperience()
        }

        vzmve_cta_button.isVisible = false
        MultiViewExperienceSdk.isSuperfan(
            { isSuperfan ->
                vzmve_cta_button.isVisible = isSuperfan
                Toast.makeText(this@MainActivity, "Superfan is $isSuperfan", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Timber.e(error)
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
                vzmve_cta_button.isVisible = false
            }
        )
        vzmve_cta_button.setOnClickListener {
            MultiViewExperienceSdk.launchMultiViewExperience()
        }
    }

}
