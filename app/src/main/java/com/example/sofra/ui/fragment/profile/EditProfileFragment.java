package com.example.sofra.ui.fragment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.pojo.client.login.Login;
import com.example.sofra.data.pojo.client.login.User;
import com.example.sofra.data.pojo.general.city.CityData;
import com.example.sofra.databinding.FragmentEditProfileBinding;
import com.example.sofra.ui.activity.AuthActivity;
import com.example.sofra.ui.fragment.BaseFragment;
import com.example.sofra.ui.fragment.register.RegisterFragment;
import com.example.sofra.utils.HelperMethod;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.utils.GeneralResponse.getCityList;
import static com.example.sofra.utils.GeneralResponse.getRegionList;

public class EditProfileFragment extends BaseFragment {

    FragmentEditProfileBinding binding;

    private ArrayList<CityData> cityDataArrayList = new ArrayList<>();
    private ArrayList<CityData> regionDataArrayList = new ArrayList<>();

    private String userType;
    private String apiToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        final View view = binding.getRoot();
        setUpActivity();

        assert getArguments() != null;
        userType = getArguments().getString("userType");

        if (LoadData(getActivity(), "apiToken") != null) {
            apiToken = LoadData(getActivity(), "apiToken");
        } else {
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            intent.putExtra("userType", userType);
            startActivity(intent);
            return null;
        }

        // find user type {seller or client} to set equivalents view
        assert userType != null;
        if (userType.equals("seller")) {
            restaurantEnableView();
            getRestaurantProfileFromServer();
        } else {
            getClientProfileFromServer();
        }

        // disappear keyboard if user click on any empty view in screen
        binding.fragmentEditProfileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperMethod.disappearKeypad(getActivity(), view);
            }
        });

        getCityList(getActivity(), cityDataArrayList, binding.fragmentEditProfileCitySpinner, getString(R.string.choose_city));
        binding.fragmentEditProfileCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    regionDataArrayList.clear();
                    // get region list from server
                    getRegionList(getActivity(), regionDataArrayList, position, binding.fragmentEditProfileDistrictSpinner, getString(R.string.choose_region));

                } else {
                    getRegionList(getActivity(), regionDataArrayList, position, binding.fragmentEditProfileDistrictSpinner, getString(R.string.choose_city_first));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getRegionList(getActivity(), regionDataArrayList, 0, binding.fragmentEditProfileDistrictSpinner, getString(R.string.choose_city_first));
            }
        });

        return view;
    }

    /**
     * This method is to modify {@link RegisterFragment (View)  }
     * from Client to restaurant View.
     */
    private void restaurantEnableView() {
        binding.fragmentEditProfileCustomerEditTextPhoneNumber.setVisibility(View.GONE);
        binding.fragmentEditProfileEditTextName.setHint(R.string.restaurant_name);
        binding.fragmentEditProfileRestaurantDataContainer.setVisibility(View.VISIBLE);
    }

    /**
     * to get client data from server
     */
    private void getClientProfileFromServer() {

    }

    /**
     * to get restaurant data from server
     */
    private void getRestaurantProfileFromServer() {
        RestaurantGetProfileViewModel restaurantGetProfileViewModel =
                new ViewModelProvider(Objects.requireNonNull(getActivity())).get(RestaurantGetProfileViewModel.class);
        restaurantGetProfileViewModel.getRestaurantProfile(apiToken);
        restaurantGetProfileViewModel.getRestaurantProfileMutableLiveData.observe(getActivity(), new Observer<Login>() {
            @Override
            public void onChanged(Login login) {
                if (login.getStatus() == 1) {
                    setRestaurantDataToView(login.getData().getUser());
                } else {
                    Toast.makeText(baseActivity, login.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Update profile data with restaurant data.
     *
     * @param restaurant the data of restaurant
     */
    private void setRestaurantDataToView(final User restaurant) {

        Glide.with(Objects.requireNonNull(getActivity())).load(restaurant.getPhotoUrl()).into(binding.fragmentEditProfileImageView);
        binding.fragmentEditProfileEditTextName.setText(restaurant.getName());
        binding.fragmentEditProfileEditTextEmail.setText(restaurant.getEmail());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.fragmentEditProfileCitySpinner.setSelection(restaurant.getRegion().getCity().getId());
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.fragmentEditProfileDistrictSpinner.setSelection(restaurant.getRegion().getId());
            }
        }, 2000);

        binding.fragmentEditProfileRestaurantEditTextMinimumCharge.setText(restaurant.getMinimumCharger());
        binding.fragmentEditProfileRestaurantEditTextDeliveryCost.setText(restaurant.getDeliveryCost());
        binding.fragmentEditProfileRestaurantEditTextDeliveryTime.setText(restaurant.getDeliveryTime());
        binding.fragmentEditProfileRestaurantEditTextPhoneNumber.setText(restaurant.getPhone());
        binding.fragmentEditProfileRestaurantEditTextWhatsappNumber.setText(restaurant.getWhatsapp());

        if (restaurant.getAvailability().equals("open")) {
            binding.fragmentEditProfileSwitch.setChecked(true);
        }

    }

}