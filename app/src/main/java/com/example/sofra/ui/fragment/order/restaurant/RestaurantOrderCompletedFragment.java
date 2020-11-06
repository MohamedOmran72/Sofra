package com.example.sofra.ui.fragment.order.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.sofra.databinding.FragmentRestaurantOrderCompletedBinding;
import com.example.sofra.ui.fragment.BaseFragment;

public class RestaurantOrderCompletedFragment extends BaseFragment {
    private FragmentRestaurantOrderCompletedBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantOrderCompletedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpActivity();

        return view;
    }
}