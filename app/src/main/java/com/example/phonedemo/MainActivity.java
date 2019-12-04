package com.example.phonedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setOnNavigationItemSelectedListener(navListener);

        // attaching bottom sheet behaviour - hide / show on scroll
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
//        layoutParams.setBehavior(new BottomNavigationBehavior());
        // load the store fragment by default
//        loadFragment(new Home_Fragment());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Home_Fragment()).commit();
    }

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment fragment = null;
//            switch (item.getItemId()) {
//                case R.id.navigation_shop:
//                    toolbar.setTitle("Shop");
//                    fragment = new Home_Fragment();
//                    loadFragment(fragment);
//                    break;
//                case R.id.navigation_gifts:
//                    toolbar.setTitle("Notifications");
//                    //fragment = new GiftsFragment();
//                    //loadFragment(fragment);
//                    break;
//                case R.id.navigation_cart:
//                     toolbar.setTitle("Cart");
//                    fragment = new PostPhoneFragment();
//                    loadFragment(fragment);
//                    break;
//                case R.id.navigation_profile:
//                    toolbar.setTitle("Profile");
////                    Profilefragment = new ProfileFragment();
////                    loadFragment(fragment);
//                    break;
//            }
//
//            return true;
//        }
//    };

//    private void loadFragment(Fragment fragment) {
//        // load fragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

    BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = null;

            switch (menuItem.getItemId()) {
                case R.id.navigation_shop:
                    fragment = new Home_Fragment();
                    break;
                case R.id.navigation_gifts:
                    fragment = new GiftsFragment();
                    break;
                case R.id.navigation_cart:
                    fragment = new PostPhoneFragment();
                    break;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    break;
            }

            fragmentTransaction.replace(R.id.frame_container, fragment);
            fragmentTransaction.commit();
            return true;
        }

    };

    public void getFragment(Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getFragment: " + e.getMessage());
        }
    }
}