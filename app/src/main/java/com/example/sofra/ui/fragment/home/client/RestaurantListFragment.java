package com.example.sofra.ui.fragment.home.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sofra.databinding.FragmentRestaurantListBinding;
import com.example.sofra.ui.fragment.BaseFragment;

public class RestaurantListFragment extends BaseFragment {
    FragmentRestaurantListBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantListBinding.inflate(inflater, container, false);
        final View view = binding.getRoot();
        setUpActivity();

        return view;
    }
}