package com.example.sofra.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sofra.ui.fragment.BaseFragment;


public class BaseActivity extends AppCompatActivity {

    public BaseFragment baseFragment;
    
    public void superBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        baseFragment.onBack();
    }
}
