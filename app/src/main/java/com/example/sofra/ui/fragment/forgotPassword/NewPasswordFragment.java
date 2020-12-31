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
import com.example.sofra.databinding.FragmentNewPasswordBinding;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.utils.CheckInput.isEditTextSet;

public class NewPasswordFragment extends Fragment {

    private FragmentNewPasswordBinding binding;
    private ClientNewPasswordViewModel clientNewPasswordViewModel;
    private RestaurantNewPasswordViewModel restaurantNewPasswordViewModel;
    private String userType;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewPasswordBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (LoadData(requireActivity(), "userType") != null) {
            userType = LoadData(requireActivity(), "userType");
        }


        restaurantNewPasswordViewModel = new ViewModelProvider(this).get(RestaurantNewPasswordViewModel.class);
        restaurantNewPasswordViewModel.restaurantNewPasswordMutableLiveData.observe(getViewLifecycleOwner(), resetPassword -> {
            if (resetPassword.getStatus() == 1) {
                Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_newPasswordFragment_to_loginFragment);
            } else {
                Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });

        clientNewPasswordViewModel = new ViewModelProvider(this).get(ClientNewPasswordViewModel.class);
        clientNewPasswordViewModel.clientNewPasswordMutableLiveData.observe(getViewLifecycleOwner(), resetPassword -> {
            if (resetPassword.getStatus() == 1) {
                Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_newPasswordFragment_to_loginFragment);
            } else {
                Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        // verify code and change password
        binding.fragmentNewPasswordButtonChangePassword.setOnClickListener(v -> {

            if (isEditTextSet(binding.fragmentNewPasswordEditTextCode
                    , binding.fragmentNewPasswordEditTextPassword
                    , binding.fragmentNewPasswordEditTextConfirmPassword)) {

                if (userType.equals("client")) {
                    clientNewPassword();
                } else {
                    restaurantNewPassword();
                }
            }
        });
    }

    private void clientNewPassword() {
        String code = String.valueOf(binding.fragmentNewPasswordEditTextCode.getText());
        String password = String.valueOf(binding.fragmentNewPasswordEditTextPassword.getText()).trim();
        String passwordConfirmation = String.valueOf(binding.fragmentNewPasswordEditTextConfirmPassword.getText()).trim();

        clientNewPasswordViewModel.clientNewPassword(code, password, passwordConfirmation);
    }

    private void restaurantNewPassword() {
        String code = String.valueOf(binding.fragmentNewPasswordEditTextCode.getText());
        String password = String.valueOf(binding.fragmentNewPasswordEditTextPassword.getText()).trim();
        String passwordConfirmation = String.valueOf(binding.fragmentNewPasswordEditTextConfirmPassword.getText()).trim();

        restaurantNewPasswordViewModel.restaurantNewPassword(code, password, passwordConfirmation);
    }
}