package com.example.sofra.ui.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.sofra.R;
import com.example.sofra.data.pojo.client.login.Login;
import com.example.sofra.databinding.FragmentLoginBinding;
import com.example.sofra.ui.activity.HomeActivity;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;
import static com.example.sofra.utils.CheckInput.isEditTextSet;
import static com.example.sofra.utils.CheckInput.isEmailValid;
import static com.example.sofra.utils.HelperMethod.disappearKeypad;

public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getName();
    public static boolean isRegistered = false;
    private ClientLoginViewModel clientLoginViewModel;
    private RestaurantLoginViewModel restaurantLoginViewModel;
    private FragmentLoginBinding binding;
    private String userType;
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        if (LoadData(getActivity(), "email") != null) {
            binding.loginFragmentEditTextUsername.setText(LoadData(getActivity(), "email"));
            binding.loginFragmentEditTextPassword.setText(LoadData(getActivity(), "password"));
        }

        if (LoadData(requireActivity(), "userType") != null) {
            userType = LoadData(requireActivity(), "userType");
        }

        Log.i(TAG, "onCreateView: userType" + userType);

        binding.loginFragmentEditTextUsername.setFocusable(true);
        return view;
    }


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

        binding.loginFragmentTextViewForgotPassword.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_resetPasswordFragment);
        });

        binding.loginFragmentTvRegister.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
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
                        Toast.makeText(requireActivity(), login.getMsg(), Toast.LENGTH_SHORT).show();
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
                        }
                        //onBack();
                    }
                }
            });
        }
    }
}