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

import com.example.helloworld_java.ui.CartFragment;
import com.example.helloworld_java.ui.CheckoutFragment;
import com.example.helloworld_java.ui.MarketFragment;
import com.fullstory.FS;
import com.fullstory.FSOnReadyListener;
import com.fullstory.FSSessionData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements FSOnReadyListener{


    private BottomNavigationView mBottomNavView;


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

        instance.setCustomKey("FSSessionURL", sessionData.getCurrentSessionURL());
        instance.log(sessionData.getCurrentSessionURL());
        instance.setUserId(userVar.get("userID"));

        FS.identify(userVar.get("userID"));
        FS.setUserVars(userVar);
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
    }

    private boolean loadFragment(Fragment fragment) {
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

