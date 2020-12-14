package com.example.sofra.ui.fragment.more.changePassword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sofra.R;
import com.example.sofra.databinding.FragmentChangePasswordBinding;
import com.example.sofra.ui.fragment.BaseFragment;

import java.util.Objects;

public class ChangePasswordFragment extends BaseFragment {

    private FragmentChangePasswordBinding binding;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        setUpActivity();

        return view;
    }

    @Override
    public void onBack() {
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.VISIBLE);
        super.onBack();
    }
}