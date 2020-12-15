package com.example.sofra.ui.fragment.more.changePassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sofra.R;
import com.example.sofra.data.pojo.client.login.Login;
import com.example.sofra.databinding.FragmentChangePasswordBinding;
import com.example.sofra.ui.activity.AuthActivity;
import com.example.sofra.ui.fragment.BaseFragment;

import java.util.Objects;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.utils.CheckInput.isEditTextSet;
import static com.example.sofra.utils.CheckInput.isPasswordMatched;
import static com.example.sofra.utils.HelperMethod.disappearKeypad;

public class ChangePasswordFragment extends BaseFragment {

    private FragmentChangePasswordBinding binding;
    private ChangePasswordViewModel changePasswordViewModel;
    private String apiToken;

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
        View view = binding.getRoot();
        setUpActivity();

        if (LoadData(getActivity(), "apiToken") != null) {
            apiToken = LoadData(getActivity(), "apiToken");
        }

        binding.fragmentChangePasswordContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disappearKeypad(getActivity(), view);
            }
        });

        changePasswordViewModel = new ViewModelProvider(this).get(ChangePasswordViewModel.class);

        changePasswordViewModel.getRestaurantChangePasswordMutableLiveData().observe(getViewLifecycleOwner()
                , new Observer<Login>() {
                    @Override
                    public void onChanged(Login login) {
                        if (login.getStatus() == 1) {
                            Toast.makeText(getActivity(), login.getMsg(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), AuthActivity.class);
                            intent.putExtra("userType", "seller");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), login.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return view;
    }

    @Override
    public void onResume() {

        binding.fragmentChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditTextSet(binding.fragmentChangePasswordTextInputEditTextOldPassword
                        , binding.fragmentChangePasswordTextInputEditTextNewPassword
                        , binding.fragmentChangePasswordTextInputEditTextConfirmPassword)
                        && isPasswordMatched(binding.fragmentChangePasswordTextInputEditTextNewPassword
                        , binding.fragmentChangePasswordTextInputEditTextConfirmPassword)) {

                    if (apiToken != null) {
                        if (LoadData(getActivity(), "userType").equals("seller")) {
                            changeRestaurantPassword();
                        } else {
                            changeClientPassword();
                        }
                    } else {
                        Intent intent = new Intent(getActivity(), AuthActivity.class);
                        intent.putExtra("userType", "client");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }
        });

        super.onResume();
    }

    private void changeClientPassword() {
    }

    private void changeRestaurantPassword() {
        changePasswordViewModel.restaurantChangePassword(apiToken
                , Objects.requireNonNull(binding.fragmentChangePasswordTextInputEditTextOldPassword.getText()).toString()
                , Objects.requireNonNull(binding.fragmentChangePasswordTextInputEditTextNewPassword.getText()).toString()
                , Objects.requireNonNull(binding.fragmentChangePasswordTextInputEditTextConfirmPassword.getText()).toString());
    }

    @Override
    public void onBack() {
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.VISIBLE);
        super.onBack();
    }
}