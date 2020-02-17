package com.example.helloworld_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.helloworld_java.utilities.NetworkUtils;
import com.fullstory.FS;
import com.fullstory.FSSessionData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView mBottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new MarketFragment());//default frag

        mBottomNavView = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
//                Fragment currentFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_market:
                        Log.d("Mainactivity","Market");
                        loadFragment(new MarketFragment());
                        break;
                    case R.id.nav_cart:
                        Log.d("Mainactivity","Cart");
                        break;
                }
                return true;
            }
        });

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}

