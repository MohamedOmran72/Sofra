package com.example.sofra.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.sofra.databinding.ActivityAuthBinding;
import com.example.sofra.ui.fragment.login.LoginFragment;

import static com.example.sofra.utils.HelperMethod.replaceFragment;

public class AuthActivity extends BaseActivity {

    private static final String TAG = AuthActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inflate Layout with viewBinding
        ActivityAuthBinding binding = ActivityAuthBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        String sessionId = getIntent().getStringExtra("userType");
        Bundle bundle = new Bundle();
        bundle.putString("userType", sessionId);
        Log.i(TAG, "onCreate: sessionId" + sessionId);

        replaceFragment(getSupportFragmentManager(), binding.authActivityFrame.getId()
                , new LoginFragment(), TAG, bundle);
    }
}