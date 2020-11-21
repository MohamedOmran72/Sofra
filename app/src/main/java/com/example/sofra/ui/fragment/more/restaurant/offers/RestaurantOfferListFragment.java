package com.example.sofra.ui.fragment.more.restaurant.offers;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.sofra.R;
import com.example.sofra.databinding.FragmentRestaurantOfferListBinding;
import com.example.sofra.ui.fragment.BaseFragment;

import java.util.Objects;

public class RestaurantOfferListFragment extends BaseFragment {
    private FragmentRestaurantOfferListBinding binding;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.GONE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantOfferListBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        setUpActivity();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // handel onBack to set bottom navigation visible
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                    Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
    }
}