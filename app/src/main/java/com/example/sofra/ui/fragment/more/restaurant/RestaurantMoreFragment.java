package com.example.sofra.ui.fragment.more.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sofra.databinding.FragmentRestaurantMoreBinding;
import com.example.sofra.ui.fragment.BaseFragment;

public class RestaurantMoreFragment extends BaseFragment {
    private FragmentRestaurantMoreBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantMoreBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpActivity();

        binding.restaurantMoreFragmentTextViewMyOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.restaurantMoreFragmentTextViewContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.restaurantMoreFragmentTextViewAboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.restaurantMoreFragmentTextViewCommentsAndRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.restaurantMoreFragmentTextViewChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.restaurantMoreFragmentTextViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}