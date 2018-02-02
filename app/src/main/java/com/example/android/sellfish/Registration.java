package com.example.android.sellfish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class Registration extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Bundle bundle = new Bundle();
        bundle.putString("PARENT_ACTIVITY", "UserProfile");
        FragmentManager fragmentManager = getSupportFragmentManager();
        RegistrationFragment registrationFragment = new RegistrationFragment();
        registrationFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.registerlayout, registrationFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Registration.this, UserProfile.class);
        startActivity(intent);
        finish();
    }
}
