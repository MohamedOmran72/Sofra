package com.example.sofra.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerWithFragmentAdapter extends FragmentStateAdapter {


    private final List<Fragment> fragments;
    private final List<String> fragmentsTitle;

    public ViewPagerWithFragmentAdapter(@NonNull Fragment fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragmentsTitle = new ArrayList<>();
    }


    public void addPager(Fragment fragments, String fragmentTitle) {
        this.fragments.add(fragments);
        this.fragmentsTitle.add(fragmentTitle);
    }

    // Returns the fragment to display for that page
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    // Returns total number of pages
    @Override
    public int getItemCount() {
        return fragments.size();
    }

    // Returns page title
    public CharSequence getPageTitle(int position) {
        return fragmentsTitle.get(position);
    }
}
