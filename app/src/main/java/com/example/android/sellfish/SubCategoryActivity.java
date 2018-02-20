package com.example.android.sellfish;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.ButterKnife;

public class SubCategoryActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub_category);
        ButterKnife.inject(this);
        loadFragment(new FishFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_fish:

                fragment = new FishFragment();
                break;
            case R.id.navigation_poultry:
                fragment = new PoultryFragment();
                break;

            case R.id.navigation_mutton:
                fragment = new MuttonFragment();
                break;

            case R.id.navigation_deals:
                fragment = new DealsFragment();
                break;
        }

        return loadFragment(fragment);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SubCategoryActivity.this, HomeActivity.class));
        finish();
    }
}
