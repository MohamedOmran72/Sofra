package com.example.sofra.ui.fragment.more.reviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.sofra.R;
import com.example.sofra.databinding.FragmentReviewsBinding;
import com.example.sofra.ui.fragment.BaseFragment;

import java.util.Objects;

public class ReviewsFragment extends BaseFragment {

    private FragmentReviewsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.GONE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_constraintLayout_actionbar).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReviewsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpActivity();


        return view;
    }

    @Override
    public void onBack() {
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_constraintLayout_actionbar).setVisibility(View.VISIBLE);
        super.onBack();
    }
}