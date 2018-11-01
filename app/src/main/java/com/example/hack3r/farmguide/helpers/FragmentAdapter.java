package com.example.hack3r.farmguide.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> fragmentTitles;

    //constructor for the fragment adapter
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragmentTitles = new ArrayList<>();
    }

    //get the fragment of the current position
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    //get the number of fragments in the activity
    @Override
    public int getCount() {
        return fragmentTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }

    //This methods is used to add more fragments to an activity
    public void addFragments(Fragment fragment, String title){
        fragments.add(fragment);
        fragmentTitles.add(title);
    }
}
