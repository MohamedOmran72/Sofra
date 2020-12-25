package com.example.sofra.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sofra.R;

public class SplashActivity extends AppCompatActivity {

//    // add viewBinding reference to use
//    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        // if the user login go to Home direct
//        // else wait user chosen
//        if (LoadData(this, "userType") != null) {
//            Intent intent = new Intent(this, HomeActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        }

        super.onCreate(savedInstanceState);
//        // inflate Layout with viewBinding
//        binding = ActivitySplashBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
        setContentView(R.layout.activity_splash);
    }

//    /**
//     * Dispatch onResume() to fragments.  Note that for better inter-operation
//     * with older versions of the platform, at the point of this call the
//     * fragments attached to the activity are <em>not</em> resumed.
//     */
//    @Override
//    protected void onResume() {
//        super.onResume();
//        binding.splashActivityButtonOrderFood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
//                intent.putExtra("userType", "client");
//                startActivity(intent);
//            }
//        });
//
//        binding.splashActivityButtonSellFood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
//                intent.putExtra("userType", "seller");
//                startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        System.exit(0);
//    }
}