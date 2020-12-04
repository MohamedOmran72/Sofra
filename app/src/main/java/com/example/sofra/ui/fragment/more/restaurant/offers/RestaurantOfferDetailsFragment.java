package com.example.sofra.ui.fragment.more.restaurant.offers;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sofra.databinding.FragmentRestaurantOfferDetailsBinding;
import com.example.sofra.ui.fragment.BaseFragment;

import java.util.Objects;

public class RestaurantOfferDetailsFragment extends BaseFragment {
    private FragmentRestaurantOfferDetailsBinding binding;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantOfferDetailsBinding.inflate(inflater, container, false);
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
                    return true;
                }
                return false;
            }
        });
    }
}