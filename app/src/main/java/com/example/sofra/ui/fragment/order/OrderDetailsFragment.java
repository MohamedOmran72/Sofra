package com.example.sofra.ui.fragment.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sofra.R;
import com.example.sofra.data.pojo.order.OrderData;
import com.example.sofra.databinding.FragmentOrderDetailsBinding;
import com.example.sofra.ui.fragment.BaseFragment;

import java.util.Objects;

public class OrderDetailsFragment extends BaseFragment {
    private final OrderData orderData;
    View view;
    private FragmentOrderDetailsBinding binding;


    public OrderDetailsFragment(OrderData orderData) {
        this.orderData = orderData;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.GONE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        setUpActivity();

        return view;
    }

}