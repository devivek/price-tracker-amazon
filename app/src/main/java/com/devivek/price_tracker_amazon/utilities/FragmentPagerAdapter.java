package com.devivek.price_tracker_amazon.utilities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class FragmentPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {
    ArrayList<Fragment> frgamentList = new ArrayList();


    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return frgamentList.get(position);
    }

    @Override
    public int getCount() {
        return frgamentList.size();
    }

    public void addFragment(Fragment fragment){
        frgamentList.add(fragment);
    }
}
