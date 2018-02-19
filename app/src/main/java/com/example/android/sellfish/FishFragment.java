package com.example.android.sellfish;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FishFragment extends Fragment {
    public TabLayout tabLayout;
    View view;
    MarineFragment marineFragment;
    ShellFragment shellFragment;
    FreshWaterFragment freshWaterFragment;
    FrozenFragment frozenFragment;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fish, container, false);
        marineFragment = new MarineFragment();
        shellFragment = new ShellFragment();
        freshWaterFragment = new FreshWaterFragment();
        frozenFragment = new FrozenFragment();
        viewPager = view.findViewById(R.id.viewpager);

        setupViewPager(viewPager);
      /*  toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        tabLayout = view.findViewById(R.id.tabs);
      /*  tabLayout.setSelectedTabIndicatorHeight(5);*/
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        adapter.addFragment(marineFragment, "Marine");
        adapter.addFragment(shellFragment, "Shell");
        adapter.addFragment(freshWaterFragment, "FreshWater");
        adapter.addFragment(frozenFragment, "Frozen");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
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
/*all,marine,shell,freshwater,frozen*/
