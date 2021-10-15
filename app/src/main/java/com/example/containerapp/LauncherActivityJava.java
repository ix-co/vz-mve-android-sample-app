package com.example.containerapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import co.ix.vzmve.sdk.MultiViewExperienceSdk;
import kotlin.Unit;

public class LauncherActivityJava extends AppCompatActivity {

    private View buttonLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLaunch = findViewById(R.id.button_first);

        buttonLaunch.setOnClickListener(view -> {
            MultiViewExperienceSdk.init(this, "58283", "NFL");

            MultiViewExperienceSdk.isSuperfan(
                    isSuperfan -> {
                        Log.d("Container", "isSuperfan " + isSuperfan);
                        return Unit.INSTANCE;
                    },
                    error -> {
                        // log throwable error
                        return Unit.INSTANCE;
                    }
            );

            MultiViewExperienceSdk.forceInVenue();

            MultiViewExperienceSdk.isInVenue(
                    isInVenue -> {
                        Log.d("Container", "isInVenue " + isInVenue);
                        return Unit.INSTANCE;
                    }
            );

            boolean isDataSimulation = MultiViewExperienceSdk.isDataSimulation();
            Log.d("Container", "isDataSimulation " + isDataSimulation);
            MultiViewExperienceSdk.setDataSimulation(true);

            MultiViewExperienceSdk.launchMultiViewExperience();
        });

    }
}
