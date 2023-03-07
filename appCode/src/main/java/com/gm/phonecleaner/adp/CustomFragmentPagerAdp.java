package com.gm.phonecleaner.adp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class CustomFragmentPagerAdp extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragment = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();

    public CustomFragmentPagerAdp(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragment.add(fragment);
        mTitles.add(title);
    }

    public ArrayList<Fragment> getmFragment() {
        return mFragment;
    }

    public void setmFragment(ArrayList<Fragment> mFragment) {
        this.mFragment = mFragment;
    }

    public ArrayList<String> getmTitles() {
        return mTitles;
    }

    public void setmTitles(ArrayList<String> mTitles) {
        this.mTitles = mTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }
}
