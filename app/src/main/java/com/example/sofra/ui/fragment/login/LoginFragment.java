package com.example.sofra.ui.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sofra.R;
import com.example.sofra.data.pojo.client.login.Login;
import com.example.sofra.databinding.FragmentLoginBinding;
import com.example.sofra.ui.activity.HomeActivity;
import com.example.sofra.ui.activity.SplashActivity;
import com.example.sofra.ui.fragment.BaseFragment;
import com.example.sofra.ui.fragment.forgotPassword.ResetPasswordFragment;
import com.example.sofra.ui.fragment.register.RegisterFragment;

import java.util.Objects;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;
import static com.example.sofra.utils.CheckInput.isEditTextSet;
import static com.example.sofra.utils.CheckInput.isEmailValid;
import static com.example.sofra.utils.HelperMethod.disappearKeypad;
import static com.example.sofra.utils.HelperMethod.replaceFragment;

public class LoginFragment extends BaseFragment {

    private static final String TAG = LoginFragment.class.getName();
    public static boolean isRegistered = false;
    private ClientLoginViewModel clientLoginViewModel;
    private RestaurantLoginViewModel restaurantLoginViewModel;
    private FragmentLoginBinding binding;
    private String userType;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpActivity();

        if (LoadData(getActivity(), "email") != null) {
            binding.loginFragmentEditTextUsername.setText(LoadData(getActivity(), "email"));
            binding.loginFragmentEditTextPassword.setText(LoadData(getActivity(), "password"));
        }

        if (getArguments() != null) {
            userType = getArguments().getString("userType");

        }

        Log.i(TAG, "onCreateView: userType" + userType);

        binding.loginFragmentEditTextUsername.setFocusable(true);
        return view;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link LoginFragment#onResume()}  Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();

        //  when user click on view to disappear the keyboard
        binding.loginFragmentConstraintLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disappearKeypad(getActivity(), binding.getRoot());
            }
        });

        binding.loginFragmentLinearSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: userType " + userType);
                if (userType.equals("client")) {
                    clientLogin();
                } else {
                    restaurantLogin();
                }

            }
        });

        binding.loginFragmentTextViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userType", userType);

                replaceFragment(getParentFragmentManager()
                        , Objects.requireNonNull(getActivity()).findViewById(R.id.auth_activity_frame).getId()
                        , new ResetPasswordFragment(), TAG, bundle);
            }
        });

        binding.loginFragmentTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userType", userType);

                replaceFragment(getParentFragmentManager()
                        , Objects.requireNonNull(getActivity()).findViewById(R.id.auth_activity_frame).getId()
                        , new RegisterFragment(), TAG, bundle);
            }
        });
    }

    private void restaurantLogin() {
        if (isEditTextSet(binding.loginFragmentEditTextUsername, binding.loginFragmentEditTextPassword)
                && isEmailValid(binding.loginFragmentEditTextUsername)) {

            final String email = binding.loginFragmentEditTextUsername.getText().toString().trim();
            final String password = binding.loginFragmentEditTextPassword.getText().toString();

            restaurantLoginViewModel = new ViewModelProvider(this).get(RestaurantLoginViewModel.class);
            restaurantLoginViewModel.getRestaurantLogin(email, password);
            restaurantLoginViewModel.loginMutableLiveData.observe(this, new Observer<Login>() {
                @Override
                public void onChanged(Login login) {
                    if (login.getStatus() == 1) {
                        SaveData(getActivity(), "userType", "seller");
                        SaveData(getActivity(), "restaurantId", login.getData().getUser().getId());
                        SaveData(getActivity(), "apiToken", login.getData().getApiToken());
                        SaveData(getActivity(), "phone", login.getData().getUser().getPhone());
                        SaveData(getActivity(), "name", login.getData().getUser().getName());
                        SaveData(getActivity(), "email", email);
                        SaveData(getActivity(), "password", password);

                        Log.i("getStatus", login.getStatus().toString());
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.putExtra("userType", "seller");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(baseActivity, login.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void clientLogin() {
        if (isEditTextSet(binding.loginFragmentEditTextUsername, binding.loginFragmentEditTextPassword)
                && isEmailValid(binding.loginFragmentEditTextUsername)) {

            final String email = binding.loginFragmentEditTextUsername.getText().toString().trim();
            final String password = binding.loginFragmentEditTextPassword.getText().toString();

            clientLoginViewModel = new ViewModelProvider(this).get(ClientLoginViewModel.class);
            clientLoginViewModel.getClientLogin(email, password);
            clientLoginViewModel.loginMutableLiveData.observe(this, new Observer<Login>() {
                @Override
                public void onChanged(Login login) {
                    if (login.getStatus() == 1) {
                        SaveData(getActivity(), "userType", "client");
                        SaveData(getActivity(), "userId", login.getData().getUser().getId());
                        SaveData(getActivity(), "apiToken", login.getData().getApiToken());
                        SaveData(getActivity(), "phone", login.getData().getUser().getPhone());
                        SaveData(getActivity(), "name", login.getData().getUser().getName());
                        SaveData(getActivity(), "email", email);
                        SaveData(getActivity(), "password", password);
                        SaveData(getActivity(), "address", login.getData().getUser().getRegion().getName());

                        Log.i("address", login.getData().getUser().getRegion().getName());
                        if (isRegistered) {
                            isRegistered = false;
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            intent.putExtra("userType", "client");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else
                            onBack();
                    }
                }
            });
        }
    }

    @Override
    public void onBack() {
        Intent intent = new Intent(getActivity(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}