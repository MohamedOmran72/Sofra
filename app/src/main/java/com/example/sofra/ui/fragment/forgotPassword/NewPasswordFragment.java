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
import com.example.sofra.databinding.FragmentNewPasswordBinding;
import com.example.sofra.ui.fragment.login.LoginFragment;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.utils.CheckInput.isEditTextSet;
import static com.example.sofra.utils.HelperMethod.replaceFragment;

public class NewPasswordFragment extends Fragment {

    private static final String TAG = NewPasswordFragment.class.getName();
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

        return view;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link NewPasswordFragment#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();

        // verify code and change password
        binding.fragmentNewPasswordButtonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEditTextSet(binding.fragmentNewPasswordEditTextCode
                        , binding.fragmentNewPasswordEditTextPassword
                        , binding.fragmentNewPasswordEditTextConfirmPassword)) {

                    if (userType.equals("client")) {
                        clientNewPassword();
                    } else {
                        restaurantNewPassword();
                    }
                }
            }
        });
    }

    private void clientNewPassword() {
        String code = String.valueOf(binding.fragmentNewPasswordEditTextCode.getText());
        String password = String.valueOf(binding.fragmentNewPasswordEditTextPassword.getText()).trim();
        String passwordConfirmation = String.valueOf(binding.fragmentNewPasswordEditTextConfirmPassword.getText()).trim();

        clientNewPasswordViewModel = new ViewModelProvider(this).get(ClientNewPasswordViewModel.class);
        clientNewPasswordViewModel.clientNewPassword(code, password, passwordConfirmation);
        clientNewPasswordViewModel.clientNewPasswordMutableLiveData.observe(this, new Observer<ResetPassword>() {
            @Override
            public void onChanged(ResetPassword resetPassword) {
                if (resetPassword.getStatus() == 1) {
                    Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle();
                    bundle.putString("userType", userType);
                    replaceFragment(getParentFragmentManager()
                            , requireActivity().findViewById(R.id.auth_activity_frame).getId()
                            , new LoginFragment(), TAG, bundle);
                } else {
                    Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void restaurantNewPassword() {
        String code = String.valueOf(binding.fragmentNewPasswordEditTextCode.getText());
        String password = String.valueOf(binding.fragmentNewPasswordEditTextPassword.getText()).trim();
        String passwordConfirmation = String.valueOf(binding.fragmentNewPasswordEditTextConfirmPassword.getText()).trim();

        restaurantNewPasswordViewModel = new ViewModelProvider(this).get(RestaurantNewPasswordViewModel.class);
        restaurantNewPasswordViewModel.restaurantNewPassword(code, password, passwordConfirmation);
        restaurantNewPasswordViewModel.restaurantNewPasswordMutableLiveData.observe(this, new Observer<ResetPassword>() {
            @Override
            public void onChanged(ResetPassword resetPassword) {
                if (resetPassword.getStatus() == 1) {
                    Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle();
                    bundle.putString("userType", userType);
                    replaceFragment(getParentFragmentManager()
                            , requireActivity().findViewById(R.id.auth_activity_frame).getId()
                            , new LoginFragment(), TAG, bundle);
                } else {
                    Toast.makeText(requireActivity(), resetPassword.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}