package com.silencedaemon.seta.ProgramFragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerControler extends FragmentPagerAdapter {

    int numTabsItems;

    public PagerControler(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numTabsItems = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0: return new FragmentEscaleras();
            case 1: return new FragmentAndamios();
            case 2: return new FragmentEQAltura();
            default: return null;
        }

    }

    @Override
    public int getCount() {
        return numTabsItems;
    }
}
