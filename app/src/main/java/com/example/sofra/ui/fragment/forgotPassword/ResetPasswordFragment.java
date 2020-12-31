package com.example.sofra.ui.fragment.forgotPassword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sofra.R;
import com.example.sofra.data.pojo.client.forgetPassword.ResetPassword;
import com.example.sofra.databinding.FragmentResetPasswordBinding;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.utils.CheckInput.isEditTextSet;
import static com.example.sofra.utils.CheckInput.isEmailValid;
import static com.example.sofra.utils.HelperMethod.replaceFragment;

public class ResetPasswordFragment extends Fragment {

    private static final String TAG = ResetPasswordFragment.class.getName();
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

        if (LoadData(requireActivity(), "userType") != null) {
            userType = LoadData(requireActivity(), "userType");
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // send code to email to change password
        binding.fragmentResetPasswordButtonSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if email is Valid
                if (isEditTextSet(binding.fragmentResetPasswordEditTextEmail)
                        && isEmailValid(binding.fragmentResetPasswordEditTextEmail)) {
                    if (userType.equals("client")) {
                        clientResetPassword();
                    } else {
                        restaurantResetPassword();
                    }
                }

            }
        });
    }

    private void clientResetPassword() {
        String email = String.valueOf(binding.fragmentResetPasswordEditTextEmail.getText()).trim();

        clientResetPasswordViewModel = new ViewModelProvider(this).get(ClientResetPasswordViewModel.class);
        clientResetPasswordViewModel.clientResetPassword(email);
        clientResetPasswordViewModel.clientResetPasswordMutableLiveData.observe(this, new Observer<ResetPassword>() {
            @Override
            public void onChanged(ResetPassword resetPassword) {
                if (resetPassword.getStatus() == 1) {
                    Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle();
                    bundle.putString("userType", userType);
                    replaceFragment(getParentFragmentManager()
                            , requireActivity().findViewById(R.id.auth_activity_frame).getId()
                            , new NewPasswordFragment(), TAG, bundle);
                } else {
                    Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void restaurantResetPassword() {
        String email = String.valueOf(binding.fragmentResetPasswordEditTextEmail.getText());

        restaurantResetPasswordViewModel = new ViewModelProvider(this).get(RestaurantResetPasswordViewModel.class);
        restaurantResetPasswordViewModel.restaurantResetPassword(email);
        restaurantResetPasswordViewModel.RestaurantResetPasswordMutableLiveData.observe(this, new Observer<ResetPassword>() {
            @Override
            public void onChanged(ResetPassword resetPassword) {
                if (resetPassword.getStatus() == 1) {
                    Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle();
                    bundle.putString("userType", userType);
                    replaceFragment(getParentFragmentManager()
                            , requireActivity().findViewById(R.id.auth_activity_frame).getId()
                            , new NewPasswordFragment(), TAG, bundle);
                } else {
                    Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}