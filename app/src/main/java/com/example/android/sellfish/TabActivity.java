package com.example.android.sellfish;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class TabActivity extends AppCompatActivity {

    public TabLayout tabLayout;
    Intent intent;
    LoginFragment loginFragment;
    RegistrationFragment registrationFragment;
    ViewPager viewPager;
    android.support.v7.widget.Toolbar toolbar;
    TextView txtSkip;
    String parent_activity;
    SharedPreferences sp;

    // @InjectView(R.id.txtSkipLogin)TextView txtSkip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
        if (sp.getBoolean("LOGGED_IN", false)) {
            intent = new Intent(TabActivity.this, HomeActivity.class);
            finish();
            startActivity(intent);
        }
        setContentView(R.layout.activity_tab);

        ButterKnife.inject(this);
        toolbar = findViewById(R.id.toolbar11);
        setSupportActionBar(toolbar);
        txtSkip = findViewById(R.id.txtSkipLogin);
        Bundle bundle = new Bundle();
        bundle.putString("PARENT_ACTIVITY", "TabActivity");
        loginFragment = new LoginFragment();
        registrationFragment = new RegistrationFragment();
        registrationFragment.setArguments(bundle);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
      /*  toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        tabLayout = findViewById(R.id.tabs);
      /*  tabLayout.setSelectedTabIndicatorHeight(5);*/
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#EF2440"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#EF2440"));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#EF2440"));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(TabActivity.this, HomeActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(loginFragment, "Login");
        adapter.addFragment(registrationFragment, "Register");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
