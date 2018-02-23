package com.example.android.sellfish;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SubCategoryActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    VolleyRequest volleyRequest;
    SharedPreferences sp;
    String user_id;
    @InjectView(R.id.img_viewCart)
    ImageView imgCart;
    @InjectView(R.id.img_back)
    ImageView imgBack;
    Intent intent;
    @InjectView(R.id.cartCount1)
    TextView txtCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub_category);

        ButterKnife.inject(this);
        sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        user_id = sp.getString("USER_ID", "");
        getItemCount1();
        loadFragment(new FishFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SubCategoryActivity.this, ViewCart.class);
                finish();
                startActivity(intent);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

    public void getItemCount1() {
        volleyRequest = VolleyRequest.getObject();
        volleyRequest.setContext(getApplicationContext());
        Log.d("checkData: ", "http://192.168.0.110:8001/routes/server/app/totalCartItems.rfa.php?user_id=" + user_id);
        volleyRequest.setUrl("http://192.168.0.110:8001/routes/server/app/totalCartItems.rfa.php?user_id=" + user_id);
        volleyRequest.getResponse(new ServerCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(response, "count");
                int count = Integer.parseInt(response);
                Log.d(response, "rcount");
                txtCount.setText(response);
                if (count > 9) {
                    txtCount.setPadding(4, 0, 0, 0);
                } else {
                    txtCount.setPadding(14, 0, 0, 0);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SubCategoryActivity.this, HomeActivity.class));
        finish();
    }

}
