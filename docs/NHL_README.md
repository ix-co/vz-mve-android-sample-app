# Verizon Multi-View Experience

Latest version is 1.1.42.93

Verizon Multi-View Experience is library which includes such functionality:
 * Multi-camera live streaming (several simultaneous video streams)
 * Multi-camera replay streaming
 * Data Visualization

Requirements:
 * min Android SDK version: 21 (Android 5.0)
 * AndroidX (Android Support Library isn't supported)
 * Multidex should be enabled
 * Java 8 language feature support

General steps to prepare and launch Multi-View Experience SDK:
  1) Integration MultiViewExperience SDK in the host app.
  2) Initialization with gameKey and product.
     This step should be first because we use these parameters for loading bootstrap config from server.
  3) Optional checking entitlements (`isSuperfan` and `isInVenue`)
     or forcing inStadium/outOfStadium mode (`forceInVenue`, `forceOutOfVenue`)
  4) Add listeners for receiving analytics data (optional)
  5) Launching Multi-View Experience screen.

## How to integrate Verizon Multi-View Experience in the Android Native app:

Add dependencies in the project:
 * Add `verizon-multi-view-experience` and `PhenixRTS` repositories to root `build.gradle`
 ```
         maven {
            url 'https://artifactory.ixinternal.com/artifactory/repo'
            credentials {
                username = "verizon-multi-view-experience"
                password = "8muUGdg9hQcjnFpu"
            }
         }
         maven {
            url 'https://artifactory.ixinternal.com/artifactory/verizon-multi-view-experience'
            credentials {
                username = "verizon-multi-view-experience"
                password = "8muUGdg9hQcjnFpu"
            }
         }
         maven {
            url "https://artifactory.ixinternal.com/artifactory/PhenixRTS"
            credentials {
                username "verizon-multi-view-experience"
                password "8muUGdg9hQcjnFpu"
            }
         }
         maven {
            url 'https://artifactory.ixinternal.com/artifactory/jitpack'
            credentials {
                username "verizon-multi-view-experience"
                password "8muUGdg9hQcjnFpu"
            }
        }
```
* Declare library dependency in `app/build.gradle` file:
```
  implementation ("co.ix.vzmve:vzmve-nhl:$version") {
    exclude group: "com.google.auto.value", module: "auto-value"
  }
```
* Current library supports x86, x86_64, armeabi-v7a, arm84-v8a CPU architectures. To remove unnecessary ones need to
determine supported architectures in `ndk` section which can be placed to `android -> defaultConfig`,
`android -> buildTypes -> debug`, `android -> buildTypes -> release`, `android -> productFlavors -> [flavour]`:
```
ndk {
    abiFilters "armeabi-v7a", "arm64-v8a"
}
```

* Set Java compatibility to Java 8 into `android` section of `app/build.gradle`:
```
    compileOptions {
        targetCompatibility JavaVersion.VERSION_1_8
        sourceCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
```
* Enable multiDex
Declare library dependency in `app/build.gradle` file: `implementation 'androidx.multidex:multidex:2.0.1'`
Add `multiDexEnabled true` to into `android` -> `defaultConfig` section of `app/build.gradle`:
```
android {
    ...
    defaultConfig {
       ...
       multiDexEnabled true
    }
}
```

* Add `VzmveActivity`in the application section of `AndroidManifest.xml`:
```
        <activity
            android:name="co.ix.vzmve.screen.VzmveActivity"
            android:configChanges="screenLayout|screenSize|orientation"
            android:screenOrientation="portrait"
            android:theme="@style/VzmveTheme"
            tools:ignore="LockedOrientationActivity" />
```

* Add `INTERNET` and `ACCESS_NETWORK_STATE` permissions to `AndroidManifest.xml`:
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```


* Init  Multi-View Experience SDK:
```
import co.ix.vzmve.sdk.MultiViewExperienceSdk
...
MultiViewExperienceSdk.init(activity, gameKey, product)
```

* Check supporting of current device:

  Kotlin:
```
MultiViewExperienceSdk.isSuperfan(
           { isSuperfan ->
               //use isSuperfan value
           },
           { error -> /* log throwable error */ }
)
```

  Java:
```
MultiViewExperienceSdk.isSuperfan(
                isSuperfan -> {
                    //use isSuperfan value
                    return Unit.INSTANCE;
                },
                error -> {
                    // log throwable error
                    return Unit.INSTANCE;
                }
        );
)
```

* Force `inVenue` value or enable geolocation checking of user position is in or out of venue (optional):
```
MultiViewExperienceSdk.forceInVenue(), MultiViewExperienceSdk.forceOutOfVenue()
or MultiViewExperienceSdk.enableInVenueChecking()
```

* Check `inVenue` value, it returns forced value or value based on geolocation checking depends on settings (optional):
  Kotlin:
```
MultiViewExperienceSdk.isInVenue(
           { isInVenue ->
               // use isInVenue value
           }
)
```
Java:
```
 MultiViewExperienceSdk.isInVenue(
                isInVenue -> {
                        // use isInVenue value
                        return Unit.INSTANCE;
                }
        );
```

* Check `isDataSimulation` value (optional):
```
MultiViewExperienceSdk.isDataSimulation()
```

* Set `isDataSimulation` value if required (optional, default value is `false`):
```
MultiViewExperienceSdk.setDataSimulation(true) or MultiViewExperienceSdk.setDataSimulation(false)
```
* Add listeners for receiving analytics data (optional).

Kotlin:
```
val analyticsActionListener: (String, Map<String, String>) -> Unit = { action, params ->
        //track action to analytics
        //Example for AEP: MobileCore.trackAction(action, params)
}
MultiViewExperienceSdk.addAnalyticsActionListener(analyticsActionListener)

val analyticsStateListener: (String, Map<String, String>) -> Unit = { state, params ->
        //track state to analytics
        //Example for AEP: MobileCore.trackState(state, params)
}
MultiViewExperienceSdk.addAnalyticsStateListener(analyticsStateListener)
```

Java:
```
Function2<String, Map<String, String>, Unit> analyticsActionListener = (action, paramsMap) -> {
        //track action to analytics
        //Example for AEP: MobileCore.trackAction(action, params)
        return Unit.INSTANCE;
};
MultiViewExperienceSdk.addAnalyticsActionListener(analyticsActionListener);

Function2<String, Map<String, String>, Unit> analyticsStateListener = (state, paramsMap) -> {
        //track state to analytics
        //Example for AEP: MobileCore.trackState(state, params)
        return Unit.INSTANCE;
};
MultiViewExperienceSdk.addAnalyticsStateListener(analyticsStateListener);
```

* Remove analytics listeners for receiving analytics data when Multi-View Experience is closed:

Kotlin:
```
MultiViewExperienceSdk.removeAnalyticsActionListener(analyticsActionListener)
MultiViewExperienceSdk.removeAnalyticsStateListener(analyticsStateListener)
```

Java:
```
MultiViewExperienceSdk.removeAnalyticsActionListener(analyticsActionListener);
MultiViewExperienceSdk.removeAnalyticsStateListener(analyticsStateListener);
```

* Add listener for closing Multi-View Experience SDK (optional). This listener can be used to remove
  analytics listeners.
Kotlin:
```
val onCloseSdkListener = {
    ...
}
MultiViewExperienceSdk.addCloseSdkListener(onCloseSdkListener)
```

Java:
```
Function0 onCloseSdkListener = () -> {
    ...
    return null;
};
MultiViewExperienceSdk.addCloseSdkListener(onCloseSdkListener);
```

* Remove listener for closing Multi-View Experience SDK if listener was added.
Kotlin:
```
MultiViewExperienceSdk.removeCloseSdkListener(onCloseSdkListener)
```

Java:
```
MultiViewExperienceSdk.removeCloseSdkListener(onCloseSdkListener);
```

* Launch Multi-View Experience SDK
```
MultiViewExperienceSdk.launchMultiViewExperience()
```

## How to integrate Verizon Multi-View Experience in the Android ReactNative app:

 * Add `verizon-multi-view-experience` and `PhenixRTS` repositories to root `build.gradle`
 ```
         maven {
            url 'https://artifactory.ixinternal.com/artifactory/repo'
            credentials {
                username = "verizon-multi-view-experience"
                password = "8muUGdg9hQcjnFpu"
            }
         }
         maven {
            url 'https://artifactory.ixinternal.com/artifactory/verizon-multi-view-experience'
            credentials {
                username = "verizon-multi-view-experience"
                password = "8muUGdg9hQcjnFpu"
            }
         }
         maven {
            url "https://artifactory.ixinternal.com/artifactory/PhenixRTS"
            credentials {
                username "verizon-multi-view-experience"
                password "8muUGdg9hQcjnFpu"
            }
         }
         maven {
            url 'https://artifactory.ixinternal.com/artifactory/jitpack'
            credentials {
                username "verizon-multi-view-experience"
                password "8muUGdg9hQcjnFpu"
            }
        }
```
* Add `verizon-multi-view-experience` dependency to the `app/build.gradle`:
```
  implementation ("co.ix.vzmve:vzmve-nhl:$version") {
    exclude group: "com.google.auto.value", module: "auto-value"
  }
```

* Current library supports x86, x86_64, armeabi-v7a, arm84-v8a CPU architectures. To remove unnecessary ones need to
determine supported architectures in `ndk` section which can be placed to `android -> defaultConfig`,
`android -> buildTypes -> debug`, `android -> buildTypes -> release`, `android -> productFlavors -> [flavour]`:
```
ndk {
    abiFilters "armeabi-v7a", "arm64-v8a"
}
```

* Set Java compatibility to Java 8 into `android` section of `app/build.gradle`:
```
    compileOptions {
        targetCompatibility JavaVersion.VERSION_1_8
        sourceCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
```

* Add `VzmveActivity` in the application section of `AndroidManifest.xml`:
```
        <activity
            android:name="co.ix.vzmve.screen.VzmveActivity"
            android:configChanges="screenLayout|screenSize|orientation"
            android:screenOrientation="portrait"
            android:theme="@style/VzmveTheme"
            tools:ignore="LockedOrientationActivity" />
```

* Add `INTERNET` and `ACCESS_NETWORK_STATE` permissions `AndroidManifest.xml`:
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

* Create `VzmveModule`:
Kotlin:
```
import android.content.Intent
import co.ix.vzmve.sdk.MultiViewExperienceSdk
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class VzmveModule(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "MultiViewExperienceSdk"
    }

    @ReactMethod
    fun init(gameKey: String, product: String) {
        MultiViewExperienceSdk.init(reactContext.applicationContext, gameKey, product)
    }

    @ReactMethod
    fun isSuperfan(promise: Promise) {
        MultiViewExperienceSdk.isSuperfan(
            { isSuperfan ->
                promise.resolve(isSuperfan)
            },
            { error ->
                promise.reject(error)
            }
        )
    }

    @ReactMethod
    fun isInVenue(promise: Promise) {
        MultiViewExperienceSdk.isInVenue(
            { isInVenue ->
                promise.resolve(isInVenue)
            }
        )
    }

    @ReactMethod
    fun forceInVenue() {
        MultiViewExperienceSdk.forceInVenue();
    }

    @ReactMethod
    fun forceOutOfVenue() {
        MultiViewExperienceSdk.forceOutOfVenue();
    }

    @ReactMethod
    fun enableInVenueChecking() {
        MultiViewExperienceSdk.enableInVenueChecking();
    }

    @ReactMethod
    fun isDataSimulation(promise: Promise) {
        promise.resolve(MultiViewExperienceSdk.isDataSimulation());
    }

    @ReactMethod
    fun setDataSimulation(isDataSimulation: Boolean) {
        MultiViewExperienceSdk.setDataSimulation(isDataSimulation);
    }

    @ReactMethod
    fun launchMultiViewExperience() {
        MultiViewExperienceSdk.launchMultiViewExperience()
    }
}
```
Java:
```
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import javax.annotation.Nonnull;

import co.ix.vzmve.sdk.MultiViewExperienceSdk;

public class VzmveModule extends ReactContextBaseJavaModule {

    private static ReactApplicationContext reactContext;

    VzmveModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Nonnull
    @Override
    public String getName() {
        return "MultiViewExperienceSdk";
    }

    @ReactMethod
    public void init(String gameKey, String product) {
        MultiViewExperienceSdk.init(
            getReactApplicationContext().getApplicationContext(),
            gameKey,
            product
        );
    }

    @ReactMethod
    public void isSuperfan(Promise promise) {
        MultiViewExperienceSdk.isSuperfan(
                isSuperfan -> {
                        promise.resolve(isSuperfan);
                        return Unit.INSTANCE;
                },
                throwable -> {
                        promise.reject(throwable);
                        return Unit.INSTANCE;
                }
        );
    }

    @ReactMethod
    public void isInVenue(Promise promise) {
        MultiViewExperienceSdk.isInVenue(
                isInVenue -> {
                        promise.resolve(isInVenue);
                        return Unit.INSTANCE;
                }
        );
    }

    @ReactMethod
    public void forceInVenue() {
        MultiViewExperienceSdk.forceInVenue();
    }

    @ReactMethod
    public void forceOutOfVenue() {
        MultiViewExperienceSdk.forceOutOfVenue();
    }

    @ReactMethod
    public void enableInVenueChecking() {
        MultiViewExperienceSdk.enableInVenueChecking();
    }

    @ReactMethod
    public void isDataSimulation(Promise promise) {
        promise.resolve(MultiViewExperienceSdk.isDataSimulation());
    }

    @ReactMethod
    public void setDataSimulation(boolean isDataSimulation) {
        MultiViewExperienceSdk.setDataSimulation(isDataSimulation);
    }

    @ReactMethod
    public void launchMultiViewExperience() {
        MultiViewExperienceSdk.launchMultiViewExperience();
    }

}
```

* create `VzmvePackage`:
Kotlin:
```
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

import java.util.ArrayList

class VzmvePackage : ReactPackage {

    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        val modules = ArrayList<NativeModule>()
        modules.add(VzmveModule(reactContext))
        return modules
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }

}
```
Java:
```
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

public class VzmvePackage implements ReactPackage {

    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new VzmveModule(reactContext));
        return modules;
    }

    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

}

```
* add `VzmvePackage` to `MainApplication` -> `ReactNativeHost` -> `getPackages()`:
Kotlin:
```
override fun getPackages(): List<ReactPackage> {
            return Arrays.asList(
                MainReactPackage(),
                ReanimatedPackage(),
                RNGestureHandlerPackage(),
                RNScreensPackage(),
                ModuleRegistryAdapter(mModuleRegistryProvider),
                VzmvePackage()
            )
        }
```
Java:
```
        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage(),
                    new ReanimatedPackage(),
                    new RNGestureHandlerPackage(),
                    new RNScreensPackage(),
                    new ModuleRegistryAdapter(mModuleRegistryProvider),
                    new VzmvePackage()
            );
        }
```

* Create `MultiViewExperienceSdk.js`:
```
import {NativeModules} from 'react-native';
module.exports = NativeModules.MultiViewExperienceSdk;
```

* Import `MultiViewExperienceSdk` to place where it should be launched:
```
import MultiViewExperienceSdk from './MultiViewExperienceSdk';
```

* Init  Multi-View Experience SDK (please replace {gameKey} and {product} to real string values):
```
MultiViewExperienceSdk.init(activity, {gameKey}, {product});
```

* Check supporting of current device:
```
MultiViewExperienceSdk.isSuperfan(promise);
```

* Check `inVenue` value, it returns forced value or value based on geolocation checking depends on settings (optional):
```
MultiViewExperienceSdk.isInVenue(promise);
```

* Force `inVenue` value or enable geolocation checking of user position is in or out of venue (optional):
```
MultiViewExperienceSdk.forceInVenue(), MultiViewExperienceSdk.forceOutOfVenue()
or MultiViewExperienceSdk.enableInVenueChecking()
```

* Check `isDataSimulation` value (optional):
```
MultiViewExperienceSdk.isDataSimulation(promise)
```

* Set `isDataSimulation` value if required (optional, default value is `false`):
```
MultiViewExperienceSdk.setDataSimulation(true) or MultiViewExperienceSdk.setDataSimulation(false)
```

* Launch Multi-View Experience SDK
```
MultiViewExperienceSdk.launchMultiViewExperience();
```
