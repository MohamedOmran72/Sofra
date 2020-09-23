package com.example.sofra.ui.fragment.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.sofra.R;
import com.example.sofra.databinding.FragmentEditProfileBinding;
import com.example.sofra.ui.fragment.BaseFragment;
import com.example.sofra.ui.fragment.register.RegisterFragment;
import com.example.sofra.utils.HelperMethod;

public class EditProfileFragment extends BaseFragment {

    FragmentEditProfileBinding binding;

    private String userType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        final View view = binding.getRoot();
        setUpActivity();

        assert getArguments() != null;
        userType = getArguments().getString("userType");

        // find user type {seller or client} to set equivalents view
        assert userType != null;
        if (userType.equals("seller")) {
            restaurantEnableView();
        }

        // disappear keyboard if user click on any empty view in screen
        binding.fragmentEditProfileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperMethod.disappearKeypad(getActivity(), view);
            }
        });


        return view;
    }

    /**
     * This method is to modify {@link RegisterFragment (View)  }
     * from Client to restaurant View.
     */
    private void restaurantEnableView() {
        binding.fragmentEditProfileCustomerEditTextPhoneNumber.setVisibility(View.GONE);
        binding.fragmentEditProfileEditTextName.setHint(R.string.restaurant_name);
        binding.fragmentEditProfileRestaurantDataContainer.setVisibility(View.VISIBLE);
    }
}