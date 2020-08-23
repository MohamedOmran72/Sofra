package com.example.sofra.ui.fragment.forgotPassword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sofra.databinding.FragmentForgotPasswordEnterEmailBinding;
import com.example.sofra.ui.fragment.BaseFragment;

public class ForgotPasswordEnterEmailFragment extends BaseFragment {

    private FragmentForgotPasswordEnterEmailBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordEnterEmailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpActivity();


        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}