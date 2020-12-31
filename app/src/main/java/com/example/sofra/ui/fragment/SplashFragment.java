package com.example.sofra.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.sofra.R;
import com.example.sofra.databinding.FragmentSplashBinding;

import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;

public class SplashFragment extends Fragment {

    private FragmentSplashBinding binding;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.splashFragmentButtonSellFood.setOnClickListener(v -> {
            SaveData(getActivity(), "userType", "seller");
            Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_authFragment);
        });
    }
}