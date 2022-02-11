package com.example.financetest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter{
    private int numOfTabs;
    public PagerAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;

    }

    @NonNull
    @Override
    //Pozisyona göre Gelir veya gider fragmentı döndürür
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Gelir();
            case 1:
                return new Gider();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
