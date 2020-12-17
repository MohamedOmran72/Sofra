package com.example.sofra.ui.fragment.home.restaurant.commission;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sofra.R;
import com.example.sofra.data.pojo.restaurant.commission.Commission;
import com.example.sofra.data.pojo.restaurant.commission.CommissionData;
import com.example.sofra.databinding.FragmentRestaurantCommissionBinding;
import com.example.sofra.ui.fragment.BaseFragment;

import java.util.Objects;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;

public class RestaurantCommissionFragment extends BaseFragment {

    private FragmentRestaurantCommissionBinding binding;
    private String apiToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.GONE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_constraintLayout_actionbar).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantCommissionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpActivity();

        if (LoadData(getActivity(), "apiToken") != null) {
            apiToken = LoadData(getActivity(), "apiToken");
            apiToken = "Jptu3JVmDXGpJEaQO9ZrjRg5RuAVCo45OC2AcOKqbVZPmu0ZJPN3T1sm0cWx";
        }

        RestaurantCommissionViewModel restaurantCommissionViewModel = new ViewModelProvider(this).get(RestaurantCommissionViewModel.class);
        restaurantCommissionViewModel.getRestaurantCommission(apiToken);

        restaurantCommissionViewModel.getCommissionMutableLiveData().observe(getViewLifecycleOwner()
                , new Observer<Commission>() {
                    @Override
                    public void onChanged(Commission commission) {
                        if (commission.getStatus() == 1) {
                            setDataToView(commission.getData());
                        }
                    }
                });

        return view;
    }

    private void setDataToView(CommissionData commissionData) {
        binding.restaurantCommissionFragmentTextViewRestaurantSales.setText(Objects.requireNonNull(getContext())
                .getString(R.string.restaurant_sales, commissionData.getPayments()));
        binding.restaurantCommissionFragmentTextViewAppCommission.setText(Objects.requireNonNull(getContext())
                .getString(R.string.application_commission, commissionData.getCommissions()));
        binding.restaurantCommissionFragmentTextViewPaid.setText(Objects.requireNonNull(getContext())
                .getString(R.string.was_paid, commissionData.getPayments()));
        binding.restaurantCommissionFragmentTextViewRest.setText(Objects.requireNonNull(getContext())
                .getString(R.string.the_rest, commissionData.getNetCommissions().toString()));
    }

    @Override
    public void onBack() {
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_bottom_navigation).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_activity_constraintLayout_actionbar).setVisibility(View.VISIBLE);
        super.onBack();
    }
}