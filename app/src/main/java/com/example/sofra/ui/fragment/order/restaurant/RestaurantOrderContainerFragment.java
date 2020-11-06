package com.example.sofra.ui.fragment.order.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.sofra.R;
import com.example.sofra.adapter.ViewPagerWithFragmentAdapter;
import com.example.sofra.databinding.FragmentRestaurantOrderContainerBinding;
import com.example.sofra.ui.fragment.BaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class RestaurantOrderContainerFragment extends BaseFragment {

    private FragmentRestaurantOrderContainerBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantOrderContainerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpActivity();

        setUpTab();

        return view;
    }

    private void setUpTab() {
        if (binding.fragmentRestaurantOrderContainerViewPager != null) {
            setUpViewPager();
        }

        binding.fragmentRestaurantOrderContainerTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.fragmentRestaurantOrderContainerViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setUpViewPager() {
        ViewPagerWithFragmentAdapter adapter = new ViewPagerWithFragmentAdapter(this);

        adapter.addPager(new RestaurantOrderPendingFragment(), getString(R.string.pending_order));
        adapter.addPager(new RestaurantOrderCurrentFragment(), getString(R.string.current_order));
        adapter.addPager(new RestaurantOrderCompletedFragment(), getString(R.string.completed_order));

        binding.fragmentRestaurantOrderContainerViewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.fragmentRestaurantOrderContainerTabLayout
                , binding.fragmentRestaurantOrderContainerViewPager
                , (tab, position) -> tab.setText(adapter.getPageTitle(position))).attach();
    }
}