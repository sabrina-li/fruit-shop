package com.example.helloworld_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.helloworld_java.ui.CartFragment;
import com.example.helloworld_java.ui.CheckoutFragment;
import com.example.helloworld_java.ui.MarketFragment;
import com.fullstory.FS;
import com.fullstory.FSOnReadyListener;
import com.fullstory.FSSessionData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements FSOnReadyListener{


    private BottomNavigationView mBottomNavView;
    FirebaseRemoteConfig mFirebaseRemoteConfig;


    @Override
    public void onReady(FSSessionData sessionData) {
        Log.d("MainActivity", "adding fullstory sessionurl " + sessionData.getCurrentSessionURL());

        // This normally happens on the next time launch, which sends to the wrong FS session
//        if (FirebaseCrashlytics.getInstance().didCrashOnPreviousExecution()) {
//            Log.d("Crashlytics","didCrashOnPreviousExecution");
//            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//            String sessionURL = sharedPref.getString("FSSessionURL","unknown");
//
//            Map<String,String> crashCustomEvent = new HashMap<>();
//            crashCustomEvent.put("CrashedSession",sessionURL);
//            Log.d("fullstory","sending event"+sessionURL);
//            FS.event("CrashlyticsTest",crashCustomEvent);
//        }

        //use shared pref to persist session URL
//        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("FSSessionURL", sessionData.getCurrentSessionURL());
//        editor.commit();

        FirebaseCrashlytics instance = FirebaseCrashlytics.getInstance();
        Map<String, String> userVar = new HashMap<>();
        userVar.put("userID", "testuser1");
        userVar.put("displayName","crashlytics user");
        userVar.put("crashlyticsURL","https://console.firebase.google.com/u/0/project/fs-crashlytics/crashlytics/app/android:com.example.helloworld_java/search?time=last-twenty-four-hours&type=crash&q=testuser1");
        //send userVar to FS
        FS.identify(userVar.get("userID"),userVar);

        //add FS links to userVar
        userVar.put("FSSessionURL",sessionData.getCurrentSessionURL());
        userVar.put("FSUserSearchURL","https://app.staging.fullstory.com/ui/56EM/segments/everyone/people:search:(:((UserAppKey:==:%22testuser1%22)):():(((EventType:==:%22crashed%22))):():)/0");
        //send selected userVar fields to FB
        instance.setCustomKey("FSSessionURL", userVar.get("FSSessionURL") );
        instance.setCustomKey("FSUserSearchURL", userVar.get("FSUserSearchURL"));
        instance.log(sessionData.getCurrentSessionURL());
        instance.setUserId(userVar.get("userID"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FS.setReadyListener(this);

        final Fragment marketFragment = new MarketFragment();
        final Fragment cartFragment = new CartFragment();
        final Fragment checkoutFragment = new CheckoutFragment();

        loadFragment(marketFragment);//default fragment to market

        mBottomNavView = (BottomNavigationView) findViewById(R.id.navigation);
        FS.addClass(mBottomNavView,FS.UNMASK_CLASS);
        mBottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                switch (item.getItemId()) {
                    case R.id.nav_market:
                        Log.d("Mainactivity","Market");
                        loadFragment(marketFragment);
                        break;
                    case R.id.nav_cart:
                        Log.d("Mainactivity","Cart");
                        loadFragment(cartFragment);
                        break;
                }
                return true;
            }
        });

        FirebaseCrashlytics.getInstance().checkForUnsentReports().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                //previouse crash
//                Log.d("crashlytics")
            }
        });

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(30)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        if(mFirebaseRemoteConfig!=null){
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) {
                                boolean updated = task.getResult();
                                Log.d("firebase", "Config params updated: " + updated);
                                Toast.makeText(MainActivity.this, "Fetch and activate succeeded",
                                        Toast.LENGTH_SHORT).show();
                                HashMap<String,String> hm = new HashMap<>();
                                hm.put("new",mFirebaseRemoteConfig.getString("new"));
                                FS.event("FirebaseRemoteConfig",hm);
                            } else {
                                Toast.makeText(MainActivity.this, "Fetch failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if(mFirebaseRemoteConfig!=null) {
            Log.d("firebase", mFirebaseRemoteConfig.getString("new"));
        }
        //switching fragment
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return false;
    }

}

