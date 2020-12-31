package com.example.sofra.ui.fragment.forgotPassword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.sofra.R;
import com.example.sofra.databinding.FragmentResetPasswordBinding;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.utils.CheckInput.isEditTextSet;
import static com.example.sofra.utils.CheckInput.isEmailValid;

public class ResetPasswordFragment extends Fragment {

    private FragmentResetPasswordBinding binding;
    private ClientResetPasswordViewModel clientResetPasswordViewModel;
    private RestaurantResetPasswordViewModel restaurantResetPasswordViewModel;
    private String userType;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        restaurantResetPasswordViewModel = new ViewModelProvider(this).get(RestaurantResetPasswordViewModel.class);
        clientResetPasswordViewModel = new ViewModelProvider(this).get(ClientResetPasswordViewModel.class);

        if (LoadData(requireActivity(), "userType") != null) {
            userType = LoadData(requireActivity(), "userType");
        }

        restaurantResetPasswordViewModel.RestaurantResetPasswordMutableLiveData.observe(getViewLifecycleOwner(), resetPassword -> {
            if (resetPassword.getStatus() == 1) {
                Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_resetPasswordFragment_to_newPasswordFragment);
            } else {
                Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });

        clientResetPasswordViewModel.clientResetPasswordMutableLiveData.observe(getViewLifecycleOwner(), resetPassword -> {
            if (resetPassword.getStatus() == 1) {
                Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();

                Navigation.findNavController(view).navigate(R.id.action_resetPasswordFragment_to_newPasswordFragment);
            } else {
                Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // send code to email to change password
        binding.fragmentResetPasswordButtonSendCode.setOnClickListener(v -> {
            // check if email is Valid
            if (isEditTextSet(binding.fragmentResetPasswordEditTextEmail)
                    && isEmailValid(binding.fragmentResetPasswordEditTextEmail)) {
                if (userType.equals("client")) {
                    clientResetPassword();
                } else {
                    restaurantResetPassword();
                }
            }
        });
    }

    private void clientResetPassword() {
        String email = String.valueOf(binding.fragmentResetPasswordEditTextEmail.getText()).trim();

        clientResetPasswordViewModel.clientResetPassword(email);
    }

    private void restaurantResetPassword() {
        String email = String.valueOf(binding.fragmentResetPasswordEditTextEmail.getText()).trim();

        restaurantResetPasswordViewModel.restaurantResetPassword(email);
    }
}