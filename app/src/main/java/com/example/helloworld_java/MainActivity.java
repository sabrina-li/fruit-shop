package com.example.helloworld_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.helloworld_java.ui.CartFragment;
import com.example.helloworld_java.ui.CheckoutFragment;
import com.example.helloworld_java.ui.MarketFragment;
import com.fullstory.FS;
import com.fullstory.FSOnReadyListener;
import com.fullstory.FSSessionData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements FSOnReadyListener{


    private BottomNavigationView mBottomNavView;


    @Override
    public void onReady(FSSessionData sessionData){
        Log.d("MainActivity","fullstory sessionurl "+sessionData.getCurrentSessionURL());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FS.setReadyListener(this);

        final Fragment marketFragment = new MarketFragment();
        final Fragment cartFragment = new CartFragment();

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

    public boolean loadFragment(Fragment fragment) {
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

