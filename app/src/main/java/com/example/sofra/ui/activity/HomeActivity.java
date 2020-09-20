package com.example.sofra.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.example.sofra.data.local.SharedPreferencesManger;
import com.example.sofra.databinding.ActivityHomeBinding;
import com.example.sofra.ui.fragment.home.client.RestaurantListFragment;
import com.example.sofra.ui.fragment.home.restaurant.categories.RestaurantCategoriesFragment;

import static com.example.sofra.utils.HelperMethod.addFragment;

public class HomeActivity extends BaseActivity {
    private static final String TAG = HomeActivity.class.getName();
    // add viewBinding reference to use
    ActivityHomeBinding binding;
    private String userType;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflate Layout with viewBinding
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();

        // find user type {seller or client} to set equivalents view
        userType = SharedPreferencesManger.LoadData(this, "userType");
        switch (userType) {
            case "seller":
                restaurantEnableView();
                addFragment(getSupportFragmentManager(), binding.homeActivityFragmentContainerView.getId()
                        , new RestaurantCategoriesFragment(), null);
                break;
            case "client":
                addFragment(getSupportFragmentManager(), binding.homeActivityFragmentContainerView.getId()
                        , new RestaurantListFragment(), null);
                break;

        }


        setContentView(view);

    }

    private void restaurantEnableView() {
        binding.homeActivityActionbarButtonCart.setVisibility(View.GONE);
        binding.homeActivityActionbarButtonCalculator.setVisibility(View.VISIBLE);
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();

    }
}