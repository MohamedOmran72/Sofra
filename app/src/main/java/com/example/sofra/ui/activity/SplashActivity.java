package com.example.sofra.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sofra.databinding.ActivitySplashBinding;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;

public class SplashActivity extends BaseActivity {

    // add viewBinding reference to use
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inflate Layout with viewBinding
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        choseType();
    }

    /**
     * This method use to choose customer type {OrderFood, SellFood}.
     */
    private void choseType() {

        // if the user login go to Home direct
        // else wait user chosen
        if (LoadData(this, "userType") != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            binding.splashActivityButtonOrderFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.putExtra("userType", "client");
                    startActivity(intent);
                }
            });

            binding.splashActivityButtonSellFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                    intent.putExtra("userType", "seller");
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}