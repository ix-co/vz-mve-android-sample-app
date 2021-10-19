package com.example.containerapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

const val MY_REQUEST_CODE = 1001

class MainActivity : AppCompatActivity() {

    lateinit var splitInstallManager: SplitInstallManager
    var mySessionId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        splitInstallManager = SplitInstallManagerFactory.create(this)

        findViewById<Button>(R.id.button_first).setOnClickListener {
            if (!splitInstallManager.installedModules.contains(getString(R.string.title_dynamicfeature))) {
                installDynamicFeature()
            } else {
                launchMve()
            }
        }
    }

    private fun installDynamicFeature() {
        val request = SplitInstallRequest.newBuilder()
            .addModule(getString(R.string.title_dynamicfeature))
            .build()

        splitInstallManager
            .startInstall(request)
                //these listeners are optional
            .addOnCompleteListener {
                Log.i("SplitInstallManager", "dynamicfeature installed")
            }
            .addOnSuccessListener { mySessionId = it}
            .addOnFailureListener { exception ->
                Log.i("SplitInstallManager", "error starting installation of dynamicfeature $exception")
            }

    }

    override fun onResume() {
        splitInstallManager.registerListener { state ->
            if (state.status() == SplitInstallSessionStatus.FAILED) {
                Log.i("SplitInstallManager", "error downloading dynamicfeature: ${state.errorCode()}")
            }
            if (state.sessionId() == mySessionId) {
                when (state.status()) {
                    SplitInstallSessionStatus.DOWNLOADING -> {
                        val total = state.totalBytesToDownload()
                        val progress = state.bytesDownloaded()
                        //update progress bar
                    }
                    SplitInstallSessionStatus.INSTALLED -> {
                        launchMve()
                    }
                    SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                        // startIntentSender(state.resolutionIntent()?.intentSender, null, 0,0,0)
                        splitInstallManager.startConfirmationDialogForResult(
                            state,
                            /* activity = */ this,
                            // You use this request code to later retrieve the user's decision.
                            /* requestCode = */ MY_REQUEST_CODE)
                    }
                }
            }
        }
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            // Handle the user's decision. For example, if the user selects "Cancel",
            // you may want to disable certain functionality that depends on the module.
            if (resultCode == RESULT_OK) {
                if (!splitInstallManager.installedModules.contains(getString(R.string.title_dynamicfeature))) {
                    installDynamicFeature()
                } else {
                    launchMve()
                }
            }
        }

    }

    private fun launchMve() {
        startActivity(
            Intent(Intent.ACTION_VIEW).setClassName(
                "com.example.containerapp",
                "com.example.containerapp.dynamicfeature.ProxyActivity"
            )
        )
    }
}