package com.example.sofra.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sofra.R;
import com.example.sofra.data.local.SharedPreferencesManger;
import com.example.sofra.databinding.ActivityHomeBinding;
import com.example.sofra.ui.fragment.home.client.RestaurantListFragment;
import com.example.sofra.ui.fragment.home.restaurant.categories.RestaurantCategoriesFragment;
import com.example.sofra.ui.fragment.profile.EditProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.sofra.utils.HelperMethod.addFragment;
import static com.example.sofra.utils.HelperMethod.replaceFragment;

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

        binding.homeActivityBottomNavigation.setSelectedItemId(R.id.bottom_navigation_home_item);

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

        BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment = null;
                        Bundle bundle = new Bundle();
                        switch (item.getItemId()) {
                            case R.id.bottom_navigation_home_item:
                                switch (userType) {
                                    case "seller":
                                        restaurantEnableView();
                                        fragment = new RestaurantCategoriesFragment();
                                        break;
                                    case "client":
                                        fragment = new RestaurantListFragment();
                                        break;
                                }
                                break;
                            case R.id.bottom_navigation_person_item:
                                bundle.putString("userType", userType);
                                fragment = new EditProfileFragment();
                                break;
                            case R.id.bottom_navigation_order_item:
                            case R.id.bottom_navigation_more_item:
                                return false;
                        }

                        assert fragment != null;
                        replaceFragment(getSupportFragmentManager(), binding.homeActivityFragmentContainerView.getId()
                                , fragment, getClass().getName(), bundle);

                        return true;
                    }
                };

        binding.homeActivityBottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {
        if (binding.homeActivityBottomNavigation.getSelectedItemId() == R.id.bottom_navigation_home_item) {
            super.onBackPressed();
        } else {
            binding.homeActivityBottomNavigation.setSelectedItemId(R.id.bottom_navigation_home_item);
        }
    }

}